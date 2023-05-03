package com.ricklovato.erudio.model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = -8651896004219876858L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String author;
    @Column(name = "launch_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date launchDate;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String title;

    public Book(){

    }
    public Book(Long id, String author, Date launchDate, double price, String title) {
        this.id = id;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price;
        this.title = title;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && Double.compare(book.price, price) == 0 && Objects.equals(author, book.author) && Objects.equals(launchDate, book.launchDate) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, launchDate, price, title);
    }
}
