package com.korukmez.podium.platform.user.service;

import com.korukmez.podium.platform.core.enums.UserRole;
import com.korukmez.podium.platform.core.exception.ResourceNotFoundException;
import com.korukmez.podium.platform.user.dto.UserCreateDTO;
import com.korukmez.podium.platform.user.dto.UserResponseDTO;
import com.korukmez.podium.platform.user.entity.User;
import com.korukmez.podium.platform.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Bağımlılıkları enjekte etmenin en iyi yolu (Constructor Injection)
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Yeni bir kullanıcı oluşturur.
     */
    @Transactional // Bu metot bir veritabanı işlemi (transaction) olarak çalışır
    public UserResponseDTO createUser(UserCreateDTO dto) {

        // 1. İş Kuralı: Email zaten var mı?
        userRepository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new IllegalStateException("Bu email adresi zaten kayıtlı: " + dto.getEmail());
        });

        // 2. Yeni User Entity'si oluştur
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        // 3. İş Kuralı: Şifreyi HASH'le!
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // 4. Veritabanına kaydet
        User savedUser = userRepository.save(user);

        // 5. Güvenli DTO'yu döndür
        return UserResponseDTO.fromEntity(savedUser);
    }

    /**
     * ID ile tek bir kullanıcıyı bulur.
     */
    @Transactional(readOnly = true) // Bu metot sadece okuma yapacağı için daha performanslıdır
    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı, ID: " + id));

        return UserResponseDTO.fromEntity(user);
    }

    /**
     * Sistemdeki tüm JÜRİ'leri listeler.
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllJudges() {
        // 1. Repository'den JUDGE rolündeki kullanıcıları çek
        List<User> judges = userRepository.findAllByRole(UserRole.JUDGE);

        // 2. Bu listeyi UserResponseDTO listesine dönüştür (şifreleri gizle)
        return judges.stream()
                .map(UserResponseDTO::fromEntity) // (user -> UserResponseDTO.fromEntity(user)) ile aynı
                .collect(Collectors.toList());
    }

    /**
     * Sistemdeki tüm YARIŞMACI'ları listeler.
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllContestants() {
        // 1. Repository'den CONTESTANT rolündeki kullanıcıları çek
        List<User> contestants = userRepository.findAllByRole(UserRole.CONTESTANT);

        // 2. DTO listesine dönüştür
        return contestants.stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
    // getAllJudges() ve getAllContestants() gibi diğer metotlar da
    // buraya eklenecek...
}