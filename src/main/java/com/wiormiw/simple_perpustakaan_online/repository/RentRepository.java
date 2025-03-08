package com.wiormiw.simple_perpustakaan_online.repository;

import com.wiormiw.simple_perpustakaan_online.models.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface RentRepository extends JpaRepository<Rent, UUID> {
    Optional<Rent> findByUserId(UUID userId);
    @Query("SELECT COUNT(r) > 0 FROM Rent r WHERE r.user.id = :userId AND r.status = 'ACTIVE'")
    boolean existsByUserIdAndStatus(UUID userId);
    @Query("SELECT COUNT(r) FROM Rent r WHERE r.book.id = :bookId AND r.status = 'ACTIVE'")
    int countActiveRentsByBookId(UUID bookId);
    long countByStatus(Rent.RentStatus status);
    boolean existsByUserIdAndBookIdAndStatus(UUID userId, UUID bookId, Rent.RentStatus status);
}
