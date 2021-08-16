package com.example.yishabookstoremybatis.entity;

//不在数据库中，只做一个实体类使用
public class Searchbookmodel {

    //书源
    public String Searchbooksource;
    //书名
    public String Searchbookname;
    //最新章节
    public String Searchbooknewpage;
    //作者
    public String Searchbookauthor;
    //阅读进度条
    public String Searchbookoneurl;

    public String getSearchbooksource() {
        return Searchbooksource;
    }

    public void setSearchbooksource(String searchbooksource) {
        Searchbooksource = searchbooksource;
    }

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
