package com.example.yishabookstoremybatis.entity;

public class Bookshelf {
    private int shelfid;
    private String username;
    private String bookname;

    public int getShelfid() {
        return shelfid;
    }

    public void setShelfid(int shelfid) {
        this.shelfid = shelfid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
}
