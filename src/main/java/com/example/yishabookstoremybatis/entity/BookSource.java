package com.example.yishabookstoremybatis.entity;

public class BookSource {
    //id
    private int sourceid;
    //书源名
    private String sourcename;
    //网站名
    private String sourceheaderurl;
    //搜索网址
    private String sourceurl;
    //get或post
    private String howto;
    //请求参数
    private String howtoparameter;

    public int getSourceid() {
        return sourceid;
    }

    public void setSourceid(int sourceid) {
        this.sourceid = sourceid;
    }

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    public String getSourceheaderurl() {
        return sourceheaderurl;
    }

    public void setSourceheaderurl(String sourceheaderurl) {
        this.sourceheaderurl = sourceheaderurl;
    }

    public String getSourceurl() {
        return sourceurl;
    }

    public void setSourceurl(String sourceurl) {
        this.sourceurl = sourceurl;
    }

    public String getHowto() {
        return howto;
    }

    public void setHowto(String howto) {
        this.howto = howto;
    }

    public String getHowtoparameter() {
        return howtoparameter;
    }

    public void setHowtoparameter(String howtoparameter) {
        this.howtoparameter = howtoparameter;
    }
}
