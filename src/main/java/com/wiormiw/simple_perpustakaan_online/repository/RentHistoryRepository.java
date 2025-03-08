package com.wiormiw.simple_perpustakaan_online.repository;

import com.wiormiw.simple_perpustakaan_online.models.RentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RentHistoryRepository extends JpaRepository<RentHistory, UUID> {
    List<RentHistory> findByUserId(UUID userId);
    @Query("SELECT rh FROM RentHistory rh ORDER BY rh.endDate DESC")
    List<RentHistory> findAllOrderByEndDateDesc();
}
