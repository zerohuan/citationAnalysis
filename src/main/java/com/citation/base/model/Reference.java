package com.citation.base.model;

import java.util.Arrays;

/**
 * The reference in document
 * Created by yjh on 16-4-4.
 */
public class Reference {
    private int number;
    private String title;
    private String[] authors;
    private int count;
    private String journal;
    private String publisher;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Reference{" +
                "authors=" + Arrays.toString(authors) +
                ", number='" + number + '\'' +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", journal='" + journal + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}
