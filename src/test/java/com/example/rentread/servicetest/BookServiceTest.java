package com.example.rentread.servicetest;

import com.exceptions.rentread.dto.BookDTO;
import com.exceptions.rentread.entity.Book;
import com.exceptions.rentread.entity.Rental;
import com.exceptions.rentread.entity.User;
import com.exceptions.rentread.exceptions.ResourceNotFoundException;
import com.exceptions.rentread.repository.BookRepository;
import com.exceptions.rentread.repository.RentalRepository;
import com.exceptions.rentread.service.BookService;
import com.exceptions.rentread.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserService userService;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBook() {
        // Given
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Author");
        bookDTO.setGenre("Fiction");

        Book book = new Book();
        book.setId(1L);
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setGenre(bookDTO.getGenre());
        book.setAvailable(true);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // When
        Book createdBook = bookService.createBook(bookDTO);

        // Then
        assertNotNull(createdBook);
        assertEquals("Test Book", createdBook.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testRentBook_successful() {
        // Given
        Long bookId = 1L;
        String userEmail = "user@test.com";

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAvailable(true);

        User user = new User();
        user.setEmail(userEmail);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userService.loadUserByUsername(userEmail)).thenReturn(user);
        when(rentalRepository.countByUserAndReturnDateIsNull(user)).thenReturn(0L);

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBook(book);

        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        // When
        Rental rentedBook = bookService.rentBook(bookId, userEmail);

        // Then
        assertNotNull(rentedBook);
        assertEquals(bookId, rentedBook.getBook().getId());
        verify(bookRepository, times(1)).findById(bookId);
        verify(rentalRepository, times(1)).save(any(Rental.class));
    }

    @Test
    void testRentBook_limitExceeded() {
        // Given
        Long bookId = 1L;
        String userEmail = "user@test.com";

        Book book = new Book();
        book.setId(bookId);
        book.setAvailable(true);

        User user = new User();
        user.setEmail(userEmail);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userService.loadUserByUsername(userEmail)).thenReturn(user);
        when(rentalRepository.countByUserAndReturnDateIsNull(user)).thenReturn(2L); // User already rented 2 books

        // When
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            bookService.rentBook(bookId, userEmail);
        });

        // Then
        assertEquals("You cannot rent more than 2 books at a time", exception.getMessage());
        verify(rentalRepository, times(0)).save(any(Rental.class)); // No save operation should occur
    }
}

