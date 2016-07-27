package com.aupadhyay.classandloader;

/**
 * Created by aupadhyay on 7/26/16.
 */

public class BookBean {

    String name, price, author;

    public BookBean(String price, String author, String name) {
        this.price = price;
        this.author = author;
        this.name = name;
    }

    public BookBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "BookBean{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
