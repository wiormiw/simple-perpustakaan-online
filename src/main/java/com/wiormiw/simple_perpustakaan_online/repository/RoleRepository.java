package com.wiormiw.simple_perpustakaan_online.repository;

import com.wiormiw.simple_perpustakaan_online.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(Role.RoleType name);
}
