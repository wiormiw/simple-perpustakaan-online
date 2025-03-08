package com.wiormiw.simple_perpustakaan_online.controller;

import com.wiormiw.simple_perpustakaan_online.models.dto.book.BookCreateDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.book.BookDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.book.BookUpdateDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.book.GenreDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.image.ImageDTO;
import com.wiormiw.simple_perpustakaan_online.service.implementation.BookServiceImpl;
import com.wiormiw.simple_perpustakaan_online.service.implementation.CloudinaryServiceImpl;
import com.wiormiw.simple_perpustakaan_online.utils.ImageMimeValidator;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookServiceImpl bookServiceImpl;
    private final CloudinaryServiceImpl cloudinaryServiceImpl;

    public BookController(
        BookServiceImpl bookServiceImpl,
        CloudinaryServiceImpl cloudinaryServiceImpl
    ) {
        this.bookServiceImpl = bookServiceImpl;
        this.cloudinaryServiceImpl = cloudinaryServiceImpl;
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<BookDTO>> getBooks(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(bookServiceImpl.getBooks(search, page, size));
    }

    @GetMapping("/get-all/genres")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Set<GenreDTO>> getAllGenres() {
        return ResponseEntity.ok(bookServiceImpl.getAllGenres());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> createBook(
            @Valid @RequestBody BookCreateDTO bookCreateDTO
    ) {
        return ResponseEntity.ok(bookServiceImpl.createBook(bookCreateDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> getBookById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(bookServiceImpl.getBookById(id));
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getBookCount() {
        return ResponseEntity.ok(bookServiceImpl.getTotalBooks());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable UUID id,
            @Valid @RequestBody BookUpdateDTO bookUpdateDTO
    ) {
        return ResponseEntity.ok(bookServiceImpl.updateBook(id, bookUpdateDTO));
    }

    @PutMapping("/{id}/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageDTO> updateBookCover(
            @PathVariable UUID id,
            @RequestPart("image")MultipartFile image
    ) {
        if (ImageMimeValidator.isNotValidImage(image)) {
            throw new IllegalArgumentException("Invalid image format. Only PNG, JPG, JPEG, and WEBP are allowed.");
        }
        String imageUrl = cloudinaryServiceImpl.uploadImage(image, "e-library");
        ImageDTO imageDTO = new ImageDTO(imageUrl);

        return ResponseEntity.ok(bookServiceImpl.updateBookCover(id, imageDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> deleteBook(
            @PathVariable UUID id
    ) {
        bookServiceImpl.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
