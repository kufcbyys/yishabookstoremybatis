package com.example.yishabookstoremybatis.entity;


public class Book {
    private int  bookid;
    private String bookname;
    private String booksummary;

    public String getBooksummary() {
        return booksummary;
    }

    public void setBooksummary(String booksummary) {
        this.booksummary = booksummary;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }


    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

}
