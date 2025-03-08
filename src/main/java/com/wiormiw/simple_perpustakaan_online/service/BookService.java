package com.wiormiw.simple_perpustakaan_online.service;

import com.wiormiw.simple_perpustakaan_online.models.dto.book.BookCreateDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.book.BookDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.book.BookUpdateDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.book.GenreDTO;
import com.wiormiw.simple_perpustakaan_online.models.dto.image.ImageDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public interface BookService {
    public BookDTO createBook(BookCreateDTO dto);
    public Page<BookDTO> getBooks(String search, int page, int size);
    public Set<GenreDTO> getAllGenres();
    public BookDTO getBookById(UUID id);
    public long getTotalBooks();
    public BookDTO updateBook(UUID id, BookUpdateDTO dto);
    public ImageDTO updateBookCover(UUID id, ImageDTO img);
    public void deleteBook(UUID id);
}
