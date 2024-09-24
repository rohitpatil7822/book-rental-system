package com.exceptions.rentread.repository;

import com.exceptions.rentread.entity.Book;
import com.exceptions.rentread.entity.Rental;
import com.exceptions.rentread.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental,Long> {

    long countByUserAndReturnDateIsNull(User user);

    Optional<Rental> findByUserAndBookAndReturnDateIsNull(User user, Book book);

    List<Rental> findByUserAndReturnDateIsNull(User user);

    boolean existsByBookIdAndReturnDateIsNull(Long bookId);

    List<Rental> findAllByBookId(Long bookId);

}
