package com.wiormiw.simple_perpustakaan_online.service;

import com.wiormiw.simple_perpustakaan_online.models.dto.rent.RentDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.rent.RentHistoryResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RentService {
    public RentDTO rentBook(UUID userId, UUID bookId);
    public RentHistoryResponseDTO returnBook(UUID rentId);
    public List<RentDTO> getAllRents();
    public List<RentHistoryResponseDTO> getAllRentsHistories();
    public long getTotalActiveRents();
    public List<RentHistoryResponseDTO> getRecentRentHistory(int limit);
    public RentDTO getUserRent(UUID userId);
    public List<RentHistoryResponseDTO> getUserRentHistories(UUID userId);
}
