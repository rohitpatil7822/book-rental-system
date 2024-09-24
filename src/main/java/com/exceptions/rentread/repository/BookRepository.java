package com.exceptions.rentread.repository;

import com.exceptions.rentread.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findAllByAvailableTrue();
}
