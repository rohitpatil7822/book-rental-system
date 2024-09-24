package com.exceptions.rentread.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {

    private String title;
    private String author;
    private String genre;
    private boolean available;

    // Constructor
    public BookDTO(String title, String author, String genre , boolean available) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.available = available;
    }
}
