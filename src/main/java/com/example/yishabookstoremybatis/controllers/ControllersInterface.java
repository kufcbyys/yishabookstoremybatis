package com.example.yishabookstoremybatis.controllers;

import com.example.yishabookstoremybatis.entity.Allpages;
import com.example.yishabookstoremybatis.entity.BookSource;
import com.example.yishabookstoremybatis.entity.Bookshelf;
import com.example.yishabookstoremybatis.entity.Searchbookmodel;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public interface ControllersInterface {
    //通过搜索查询书籍
    public Vector<Vector<Searchbookmodel>> searchbook(Map<String,String> map) throws Exception;
    //更新阅读进度
    public String upbookreadwhatControllers(Map<String,String> map) throws Exception;
    //获取小说所有章节
    //public List<Allpages> allthepagescontrollers(Map<String,String> map2) throws Exception;
    //获取章节小说
    public String readbookControllers(Map<String,String> map) throws Exception;
    //下一章
    public Map<String,String> nextpageControllers(Map<String,String> map) throws Exception;
    //上一章
    public Map<String,String> bakconepageControllers(Map<String,String> map) throws Exception;
    //是否存在用户
    public int finCountUser();
    //获取某个用户书架,参数是token，判断一下是否为真
    public List<Bookshelf> findoneuserbooks(Map<String,String> username);
    //将书本添加到书架中
    public int postupbookdata(Map<String,String> data) throws Exception;
    //在书架中去除书籍
    public void deleteuserbook(Map<String,String> data);
    //注册
    public Object selectishaveuser(Map<String,String> data);
    //登录
    public Object userlogin(Map<String,String> data);
    //注销账号
    public void deleteUserController(Map<String,String> data);
    //查找所有书源
    public  List<BookSource>  allBookSource();


}
