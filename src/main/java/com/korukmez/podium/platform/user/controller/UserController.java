package com.korukmez.podium.platform.user.controller;

import com.korukmez.podium.platform.user.dto.UserCreateDTO;
import com.korukmez.podium.platform.user.dto.UserResponseDTO;
import com.korukmez.podium.platform.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Bu sınıfın bir API kontrolörü olduğunu belirtir
@RequestMapping("/api/users") // Bu kontrolördeki tüm endpoint'lerin /api/users altında olacağını belirtir
public class UserController {

    private final UserService userService;

    // Servisi enjekte etmenin en iyi yolu (Constructor Injection)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/users
     * Yeni bir kullanıcı (Jüri veya Yarışmacı) oluşturur.
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        // @Valid anotasyonu, UserCreateDTO'daki @NotEmpty, @Email gibi
        // validasyon kurallarını tetikler. Hata varsa Spring 400 Bad Request döner.

        // @RequestBody, gelen JSON'u UserCreateDTO nesnesine çevirir.

        UserResponseDTO createdUser = userService.createUser(createDTO);

        // Best Practice: 201 Created durum kodu ile yeni oluşturulan
        // kaynağı (kullanıcıyı) body'de geri döneriz.
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * GET /api/users/{id}
     * ID'si verilen kullanıcıyı getirir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        // @PathVariable, URL'deki {id} değerini Long id değişkenine atar.

        UserResponseDTO user = userService.findUserById(id);

        // Best Practice: 200 OK durum kodu ile bulunan kullanıcıyı döneriz.
        // (Eğer bulunamazsa, UserService'deki @ResponseStatus(HttpStatus.NOT_FOUND)
        // sayesinde Spring otomatik 404 Not Found dönecektir).
        return ResponseEntity.ok(user);
    }

    /**
     * GET /api/users/judges
     * Tüm jürileri listeler.
     * Sadece ADMIN rolündeki kullanıcılar erişebilir.
     */
    @GetMapping("/judges")
    @PreAuthorize("hasRole('ADMIN')") // <-- YETKİLENDİRME
    public ResponseEntity<List<UserResponseDTO>> getAllJudges() {
        List<UserResponseDTO> judges = userService.getAllJudges();
        return ResponseEntity.ok(judges);
    }

    /**
     * GET /api/users/contestants
     * Tüm yarışmacıları listeler.
     * Sadece ADMIN rolündeki kullanıcılar erişebilir.
     */
    @GetMapping("/contestants")
    @PreAuthorize("hasRole('ADMIN')") // <-- YETKİLENDİRME
    public ResponseEntity<List<UserResponseDTO>> getAllContestants() {
        List<UserResponseDTO> contestants = userService.getAllContestants();
        return ResponseEntity.ok(contestants);
    }
}