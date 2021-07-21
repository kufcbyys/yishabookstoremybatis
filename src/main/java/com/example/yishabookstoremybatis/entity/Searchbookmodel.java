package com.example.yishabookstoremybatis.entity;

//不在数据库中，只做一个实体类使用
public class Searchbookmodel {
    public String Searchbookname;
    public String Searchbooknewpage;
    public String Searchbookauthor;
    public String Searchbookoneurl;

    public String getSearchbookname() {
        return Searchbookname;
    }

    public void setSearchbookname(String searchbookname) {
        Searchbookname = searchbookname;
    }

    public String getSearchbooknewpage() {
        return Searchbooknewpage;
    }

    public void setSearchbooknewpage(String searchbooknewpage) {
        Searchbooknewpage = searchbooknewpage;
    }

    public String getSearchbookauthor() {
        return Searchbookauthor;
    }

    public void setSearchbookauthor(String searchbookauthor) {
        Searchbookauthor = searchbookauthor;
    }

    public String getSearchbookoneurl() {
        return Searchbookoneurl;
    }

    public void setSearchbookoneurl(String searchbookoneurl) {
        Searchbookoneurl = searchbookoneurl;
    }
}
