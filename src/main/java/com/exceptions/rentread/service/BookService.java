package com.exceptions.rentread.service;

import com.exceptions.rentread.dto.BookDTO;
import com.exceptions.rentread.entity.Book;
import com.exceptions.rentread.entity.Rental;
import com.exceptions.rentread.entity.User;
import com.exceptions.rentread.exceptions.ResourceNotFoundException;
import com.exceptions.rentread.repository.BookRepository;
import com.exceptions.rentread.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final UserService userService;
    private final RentalRepository rentalRepository;

    public Book createBook(BookDTO bookDTO) {
        log.info("Creating new book: {}", bookDTO.getTitle());

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setGenre(bookDTO.getGenre());
        book.setAvailable(true);

        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully with ID: {}", savedBook.getId());

        return savedBook;
    }

    public Book updateBook(Long bookId, BookDTO bookDTO) {
        log.info("Updating book with ID: {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND,"Book not found with ID: " + bookId));

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setGenre(bookDTO.getGenre());

        Book updatedBook = bookRepository.save(book);
        log.info("Book updated successfully with ID: {}", updatedBook.getId());

        return updatedBook;
    }

    public void deleteBook(Long bookId) {
        log.info("Deleting book with ID: {}", bookId);

        if (!bookRepository.existsById(bookId)) {
            log.error("Book not found with ID: {}", bookId);
            throw new ResourceNotFoundException(HttpStatus.NOT_FOUND,"Book not found with ID: " + bookId);
        }

        boolean isBookRented = rentalRepository.existsByBookIdAndReturnDateIsNull(bookId);
        if (isBookRented) {
            log.error("Cannot delete book with ID {} because it is currently rented", bookId);
            throw new IllegalStateException("Cannot delete book that is currently rented.");
        }else {

            List<Rental> rentals = rentalRepository.findAllByBookId(bookId);
            if (!rentals.isEmpty()) {
                rentalRepository.deleteAll(rentals);
                log.info("Deleted rental entries associated with book ID: {}", bookId);
            }
        }


        bookRepository.deleteById(bookId);
        log.info("Book deleted successfully with ID: {}", bookId);
    }

    public List<Book> getAllAvailableBooks() {
        log.info("Fetching all available books");
        return bookRepository.findAllByAvailableTrue();
    }

    public Rental rentBook(Long bookId, String userEmail) {
        log.info("User with email {} is attempting to rent book with ID: {}", userEmail, bookId);

        User user = userService.loadUserByUsername(userEmail);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND,"Book not found with ID: " + bookId));

        if (!book.isAvailable()) {
            log.error("Book with ID {} is not available", bookId);
            throw new IllegalStateException("Book is not available for rental");
        }

        long activeRentals = rentalRepository.countByUserAndReturnDateIsNull(user);
        if (activeRentals >= 2) {
            log.error("User with email {} has reached the rental limit", userEmail);
            throw new IllegalStateException("You cannot rent more than 2 books at a time");
        }

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBook(book);
        rental.setRentalDate(LocalDateTime.now());

        Rental savedRental =  rentalRepository.save(rental);

        book.setAvailable(false);
        bookRepository.save(book);

        log.info("Book with ID {} successfully rented by user {}", bookId, userEmail);

        return savedRental;
    }

    public String returnBook(Long bookId, String userEmail) {
        log.info("User with email {} is attempting to return book with ID: {}", userEmail, bookId);

        User user = userService.loadUserByUsername(userEmail);
        Rental rental = rentalRepository.findByUserAndBookAndReturnDateIsNull(user, bookRepository.findById(bookId)
                        .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND,"Book not found with ID: " + bookId)))
                .orElseThrow(() -> new IllegalStateException("Rental not found or already returned"));

        rental.setReturnDate(LocalDateTime.now());
        rentalRepository.save(rental);

        Book book = rental.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        log.info("Book with ID {} successfully returned by user {}", bookId, userEmail);

        return "Book with ID: "+ bookId+" successfully returned by user "+userEmail;
    }
}

