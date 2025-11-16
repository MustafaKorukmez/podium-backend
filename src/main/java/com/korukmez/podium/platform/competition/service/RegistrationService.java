package com.korukmez.podium.platform.competition.service;

import com.korukmez.podium.platform.competition.dto.RegistrationResponseDTO;
import com.korukmez.podium.platform.competition.entity.Competition;
import com.korukmez.podium.platform.competition.entity.CompetitionRegistration;
import com.korukmez.podium.platform.competition.repository.CompetitionRepository;
import com.korukmez.podium.platform.competition.repository.CompetitionRegistrationRepository;
import com.korukmez.podium.platform.core.enums.RegistrationStatus;
import com.korukmez.podium.platform.core.enums.UserRole;
import com.korukmez.podium.platform.core.exception.ResourceNotFoundException;
import com.korukmez.podium.platform.user.entity.User;
import com.korukmez.podium.platform.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private final CompetitionRegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;

    public RegistrationService(CompetitionRegistrationRepository registrationRepository,
                               UserRepository userRepository,
                               CompetitionRepository competitionRepository) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.competitionRepository = competitionRepository;
    }

    /**
     * Bir kullanıcıyı (Jüri veya Yarışmacı) bir yarışmaya kaydeder.
     * ADMIN'ler yarışmalara katılamaz.
     */
    @Transactional
    public RegistrationResponseDTO registerUserToCompetition(Long userId, Long competitionId) {

        // 1. Kullanıcıyı bul
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı, ID: " + userId));

        // 2. Yarışmayı bul
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Yarışma bulunamadı, ID: " + competitionId));

        // 3. İŞ KURALI: Admin'ler yarışmaya katılamaz
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalStateException("ADMIN rolündeki kullanıcılar yarışmalara katılamaz.");
        }

        // TODO: Buraya "kullanıcı zaten kayıtlı mı?" kontrolü eklenebilir.

        // 4. Yeni kayıt nesnesini oluştur
        CompetitionRegistration registration = new CompetitionRegistration();
        registration.setUser(user);
        registration.setCompetition(competition);

        // Jüri ve Yarışmacılar için varsayılan durum PENDING (Onay Bekliyor)
        registration.setStatus(RegistrationStatus.PENDING);

        // 5. Kaydet ve dön
        CompetitionRegistration savedRegistration = registrationRepository.save(registration);
        return RegistrationResponseDTO.fromEntity(savedRegistration);
    }

    /**
     * Verilen ID listesindeki tüm kullanıcıları bir yarışmaya toplu olarak kaydeder.
     * Bu, tek bir veritabanı işlemi (transaction) içinde yapılır.
     */
    @Transactional
    public List<RegistrationResponseDTO> registerUsersInBatch(List<Long> userIds, Long competitionId) {

        // 1. Yarışmayı SADECE BİR KEZ bul
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new ResourceNotFoundException("Yarışma bulunamadı, ID: " + competitionId));

        // 2. Tüm kullanıcıları SADECE BİR KEZ (tek sorguda) bul
        List<User> users = userRepository.findAllById(userIds);

        // 3. Bulunan kullanıcı sayısı ile istenen ID sayısı eşleşmiyorsa hata fırlat
        if (users.size() != userIds.size()) {
            // (Hangi ID'lerin eksik olduğunu bulan daha detaylı bir mantık eklenebilir)
            throw new ResourceNotFoundException("Listeden bazı kullanıcılar bulunamadı.");
        }

        // 4. Kaydedilecekler listesini hazırla
        List<CompetitionRegistration> registrationsToSave = users.stream()
                .map(user -> {
                    // İş kuralını (ADMIN kontrolü) burada da uygula
                    if (user.getRole() == UserRole.ADMIN) {
                        throw new IllegalStateException("ID " + user.getId() + " (ADMIN) yarışmalara katılamaz.");
                    }

                    CompetitionRegistration registration = new CompetitionRegistration();
                    registration.setUser(user);
                    registration.setCompetition(competition);
                    registration.setStatus(RegistrationStatus.PENDING);
                    return registration;
                })
                .collect(Collectors.toList());

        // 5. TÜM kayıtları veritabanına TEK BİR 'saveAll' komutuyla (batch insert) kaydet
        List<CompetitionRegistration> savedRegistrations = registrationRepository.saveAll(registrationsToSave);

        // 6. Kaydedilen nesneleri DTO listesine çevirip dön
        return savedRegistrations.stream()
                .map(RegistrationResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}