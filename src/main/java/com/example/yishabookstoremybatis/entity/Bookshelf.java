package com.example.yishabookstoremybatis.entity;

public class Bookshelf {
    private int shelfid;
    private String username;
    private String bookname;
    private String bookoneurl;
    private String bookauthor;
    private String bookcover;

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

    public String getBookoneurl() {
        return bookoneurl;
    }

    public void setBookoneurl(String bookoneurl) {
        this.bookoneurl = bookoneurl;
    }

    public String getBookauthor() {
        return bookauthor;
    }

    public void setBookauthor(String bookauthor) {
        this.bookauthor = bookauthor;
    }

    public String getBookcover() {
        return bookcover;
    }

    public void setBookcover(String bookcover) {
        this.bookcover = bookcover;
    }
}
