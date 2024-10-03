package org.example;

import lombok.Getter;

@Getter
public class Book {
    String name;
    String author;
    int publishingYear;
    String isbn;
    String publisher;

    public String getName() {
        return name;
    }

    public int getPublishingYear() {
        return publishingYear;
    }
}