package com.exceptions.rentread.controller;

import com.exceptions.rentread.dto.BookDTO;
import com.exceptions.rentread.entity.Book;
import com.exceptions.rentread.entity.Rental;
import com.exceptions.rentread.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // Create a new book
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> createBook(@RequestBody BookDTO bookDTO) {
        Book createdBook = bookService.createBook(bookDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    // Update an existing book
    @PutMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @RequestBody BookDTO bookDTO) {
        Book updatedBook = bookService.updateBook(bookId, bookDTO);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    // Delete a book
    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all available books
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAllAvailableBooks() {
        List<Book> availableBooks = bookService.getAllAvailableBooks();
        return new ResponseEntity<>(availableBooks, HttpStatus.OK);
    }

    // Rent a book
    @PostMapping("/rent/{bookId}")
    public ResponseEntity<Rental> rentBook(@PathVariable Long bookId, @RequestParam String userEmail) {

        return new ResponseEntity<>(bookService.rentBook(bookId, userEmail),HttpStatus.OK);
    }

    // Return a book
    @PostMapping("/return/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId, @RequestParam String userEmail) {

        return new ResponseEntity<>(bookService.returnBook(bookId, userEmail),HttpStatus.OK);
    }
}
