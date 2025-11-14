package com.korukmez.podium.platform.core.enums;

/**
 * Bir kullanıcının yarışmaya kayıt durumunu belirtir.
 */
public enum RegistrationStatus {
    PENDING,  // Onay bekliyor
    APPROVED, // Onaylandı, yarışmaya dahil edildi
    REJECTED  // Reddedildi
}