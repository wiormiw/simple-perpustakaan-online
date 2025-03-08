package com.wiormiw.simple_perpustakaan_online.service.implementation;

import com.wiormiw.simple_perpustakaan_online.config.exception.BadRequestException;
import com.wiormiw.simple_perpustakaan_online.config.exception.ConflictException;
import com.wiormiw.simple_perpustakaan_online.config.exception.ResourceNotFoundException;
import com.wiormiw.simple_perpustakaan_online.config.exception.UnprocessableEntityException;
import com.wiormiw.simple_perpustakaan_online.models.Book;
import com.wiormiw.simple_perpustakaan_online.models.Rent;
import com.wiormiw.simple_perpustakaan_online.models.RentHistory;
import com.wiormiw.simple_perpustakaan_online.models.User;
import com.wiormiw.simple_perpustakaan_online.models.dto.rent.RentDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.rent.RentHistoryResponseDTO;
import com.wiormiw.simple_perpustakaan_online.repository.BookRepository;
import com.wiormiw.simple_perpustakaan_online.repository.RentHistoryRepository;
import com.wiormiw.simple_perpustakaan_online.repository.RentRepository;
import com.wiormiw.simple_perpustakaan_online.repository.UserRepository;
import com.wiormiw.simple_perpustakaan_online.service.RentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RentServiceImpl implements RentService {
    private final RentRepository rentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RentHistoryRepository rentHistoryRepository;

    public RentServiceImpl(
        RentRepository rentRepository,
        BookRepository bookRepository,
        UserRepository userRepository,
        RentHistoryRepository rentHistoryRepository
    ) {
        this.rentRepository = rentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.rentHistoryRepository = rentHistoryRepository;
    }

    @Transactional
    public RentHistoryResponseDTO rentBook(UUID userId, UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getAvailableQuantity() <= 0) {
            throw new BadRequestException("No available copies to rent");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean alreadyRented = rentRepository.existsByUserIdAndBookIdAndStatus(userId, bookId, Rent.RentStatus.ACTIVE);
        if (alreadyRented) {
            throw new ConflictException("You have already rented this book and must return it before renting again.");
        }

        Rent rent = new Rent();
        rent.setUser(user);
        rent.setBook(book);
        rent.setStatus(Rent.RentStatus.ACTIVE);

        rent = rentRepository.save(rent);

        return new RentHistoryResponseDTO(
                rent.getId(),
                rent.getBook().getTitle(),
                rent.getUser().getProfile().getFullName(),
                rent.getStartDate(),
                rent.getEndDate(),
                rent.getEndDate(),
                rent.getStatus().name()
        );
    }

    @Transactional
    public RentHistoryResponseDTO returnBook(UUID rentId) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new ResourceNotFoundException("Rent record not found"));

        if (rent.getStatus() != Rent.RentStatus.ACTIVE && rent.getStatus() != Rent.RentStatus.OVERDUE) {
            throw new UnprocessableEntityException("This book was never rented or has already been returned.");
        }

        LocalDateTime now = LocalDateTime.now();
        try {
            boolean isOverdue = now.isAfter(rent.getEndDate());
            Rent.RentStatus newStatus = isOverdue ? Rent.RentStatus.COMPLETED_OVERDUE : Rent.RentStatus.COMPLETED;
            rent.setStatus(newStatus);

            RentHistory rentHistory = new RentHistory();
            rentHistory.setUser(rent.getUser());
            rentHistory.setBook(rent.getBook());
            rentHistory.setStartDate(rent.getStartDate());
            rentHistory.setEndDate(now);
            rentHistory.setDeadline(rent.getEndDate());
            rentHistory.setStatus(newStatus);

            rentHistoryRepository.save(rentHistory);

            User user = rent.getUser();
            user.setRent(null);
            userRepository.save(user);

            rentRepository.delete(rent);

            return mapToRentHistoryResponseDTO(rentHistory);
        } catch (DateTimeException e) {
            throw new UnprocessableEntityException("Invalid date format encountered");
        }
    }

    public List<RentDTO> getAllRents() {
        return rentRepository.findAll().stream().map(this::mapToRentDTO).toList();
    }

    public List<RentHistoryResponseDTO> getAllRentsHistories() {
        return rentHistoryRepository.findAll().stream().map(this::mapToRentHistoryResponseDTO).toList();
    }

    public long getTotalActiveRents() {
        return rentRepository.countByStatus(Rent.RentStatus.ACTIVE);
    }

    public List<RentHistoryResponseDTO> getRecentRentHistory(int limit) {
        return rentHistoryRepository.findAllOrderByEndDateDesc()
                .stream()
                .limit(limit)
                .map(this::mapToRentHistoryResponseDTO)
                .collect(Collectors.toList());
    }

    public RentHistoryResponseDTO getUserRent(UUID userId) {
        Rent rent = rentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User rent not found, please check whether user currently rented!"));

        return new RentHistoryResponseDTO(
                rent.getId(),
                rent.getBook().getTitle(),
                rent.getUser().getProfile().getFullName(),
                rent.getStartDate(),
                rent.getEndDate(),
                rent.getEndDate(),
                rent.getStatus().name()
        );
    }

    public List<RentHistoryResponseDTO> getUserRentHistories(UUID userId) {
        return rentHistoryRepository.findByUserId(userId).stream().map(this::mapToRentHistoryResponseDTO).toList();
    }

    private RentDTO mapToRentDTO(Rent rent) {
        LocalDateTime now = LocalDateTime.now();
        boolean isOverdue = now.isAfter(rent.getEndDate());
        Rent.RentStatus currentStatus = rent.getStatus();

        if (currentStatus != Rent.RentStatus.COMPLETED && currentStatus != Rent.RentStatus.COMPLETED_OVERDUE) {
            currentStatus = isOverdue ? Rent.RentStatus.OVERDUE : Rent.RentStatus.ACTIVE;
        }

        return new RentDTO(
                rent.getId(),
                rent.getUser().getId(),
                rent.getBook().getId(),
                currentStatus.name()
        );
    }

    private RentHistoryResponseDTO mapToRentHistoryResponseDTO(RentHistory rentHistory) {
        return new RentHistoryResponseDTO(
                rentHistory.getId(),
                rentHistory.getBook().getTitle(),
                rentHistory.getUser().getProfile().getFullName(),
                rentHistory.getStartDate(),
                rentHistory.getEndDate(),
                rentHistory.getDeadline(),
                rentHistory.getStatus().name()
        );
    }
}
