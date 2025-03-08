package com.wiormiw.simple_perpustakaan_online.service.implementation;

import com.wiormiw.simple_perpustakaan_online.config.exception.ResourceNotFoundException;
import com.wiormiw.simple_perpustakaan_online.models.Book;
import com.wiormiw.simple_perpustakaan_online.models.Genre;
import com.wiormiw.simple_perpustakaan_online.models.dto.book.BookCreateDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.book.BookDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.book.BookUpdateDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.book.GenreDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.image.ImageDTO;
import com.wiormiw.simple_perpustakaan_online.repository.BookRepository;
import com.wiormiw.simple_perpustakaan_online.repository.GenreRepository;
import com.wiormiw.simple_perpustakaan_online.repository.RentRepository;
import com.wiormiw.simple_perpustakaan_online.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final RentRepository rentRepository;

    public BookServiceImpl(
            BookRepository bookRepository,
            GenreRepository genreRepository,
            RentRepository rentRepository
    ) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.rentRepository = rentRepository;
    }

    @Transactional
    public BookDTO createBook(BookCreateDTO dto) {
        Set<Genre> genres = new HashSet<>(genreRepository.findAllById(dto.genreIds()));

        if (genres.size() != dto.genreIds().size()) {
            throw new IllegalArgumentException("Some genres do not exist");
        }

        Book book = new Book();
        book.setTitle(dto.title());
        book.setQuantity(dto.quantity());
        book.setGenres(genres);

        book = bookRepository.save(book);
        return mapToBookDTO(book);
    }

    public Page<BookDTO> getBooks(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Book> books = (search == null || search.isBlank())
                ? bookRepository.findAll(pageable)
                : bookRepository.findByTitleContainingIgnoreCase(search, pageable);

        return books.map(this::mapToBookDTO);
    }

    public Set<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genre -> new GenreDTO(
                        genre.getId(),
                        genre.getName()
                ))
                .collect(Collectors.toSet());
    }

    public BookDTO getBookById(UUID id) {
        return bookRepository.findById(id)
                .map(this::mapToBookDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    public long getTotalBooks() {
        return bookRepository.count();
    }

    @Transactional
    public BookDTO updateBook(UUID id, BookUpdateDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (dto.title() != null) book.setTitle(dto.title());
        if (dto.quantity() != null) book.setQuantity(dto.quantity());
        if (dto.genreIds() != null) book.setGenres(getGenresByIds(dto.genreIds()));

        return mapToBookDTO(book);
    }

    @Transactional
    public ImageDTO updateBookCover(UUID id, ImageDTO img) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (img.imageUrl() != null) book.setCoverUrl(img.imageUrl());
        return new ImageDTO(book.getCoverUrl());
    }

    @Transactional
    public void deleteBook(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id.toString() + " not found");
        }
        int activeRents = rentRepository.countActiveRentsByBookId(id);

        if (activeRents > 0) {
            throw new IllegalStateException("Cannot delete book. It is currently rented.");
        }
        bookRepository.deleteById(id);
    }

    private Set<Genre> getGenresByIds(Set<UUID> genreIds) {
        return new HashSet<>(genreRepository.findAllById(genreIds));
    }

    private BookDTO mapToBookDTO(Book book) {
        int activeRents = rentRepository.countActiveRentsByBookId(book.getId());
        int availableQuantity = Math.max(0, book.getQuantity() - activeRents);

        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getQuantity(),
                availableQuantity,
                book.getCoverUrl(),
                book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
        );
    }
}

