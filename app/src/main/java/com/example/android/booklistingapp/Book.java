package com.example.android.booklistingapp;

/**
 * Created by da7th on 7/21/2016.
 */
public class Book {

    //this variable holds the title of the book
    private String mTitle;

    //this variable holds the author of the book
    private String mAuthor;

    public Book(String title, String author) {
        mTitle = title;
        mAuthor = author;
    }

    //method to get the title of the book
    public String getTitle() {
        return mTitle;
    }

    //method to get the author of the book
    public String getAuthor() {
        return mAuthor;
    }
}
