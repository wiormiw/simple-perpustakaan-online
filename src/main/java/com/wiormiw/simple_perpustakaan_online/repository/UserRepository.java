package com.wiormiw.simple_perpustakaan_online.repository;

import com.wiormiw.simple_perpustakaan_online.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<User> findByEmailContainingIgnoreCase(@Param("email") String email, Pageable pageable);
}
