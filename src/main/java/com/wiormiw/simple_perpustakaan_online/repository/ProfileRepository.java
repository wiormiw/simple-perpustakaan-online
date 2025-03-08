package com.wiormiw.simple_perpustakaan_online.repository;

import com.wiormiw.simple_perpustakaan_online.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<Profile> findByUserId(UUID userId);
}
