package com.example.yishabookstoremybatis.controllers;

import com.example.yishabookstoremybatis.entity.Allpages;
import com.example.yishabookstoremybatis.entity.BookSource;
import com.example.yishabookstoremybatis.entity.Bookshelf;
import com.example.yishabookstoremybatis.entity.Searchbookmodel;

import com.example.yishabookstoremybatis.service.Bookservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//默认返回 json数据 controller 以及 responsebody合体
//用于标注控制层组件(如struts中的action)，
//表示这是个控制器bean,并且是将函数的返回值直接填入HTTP响应体中,是REST风格的控制器；它是@Controller和@ResponseBody的合集。
//RequestMapping是一个用来处理请求地址映射的注解；
//提供路由信息，负责URL到Controller中的具体函数的映射，可用于类或方法上。用于类上，表示类中的所有响应请求的方法都是以该地址作为父路径。
@RestController
@RequestMapping("/book")
public class BookHandler implements ControllersInterface {

    @Autowired
    private Bookservice bookservice;

    //通过搜索查询书籍
    @Override
    @PostMapping("searchbook")
    public Vector<Vector<Searchbookmodel>> searchbook(@RequestBody Map<String,String> map) throws Exception {
        return bookservice.searchbookservice(map.get("name"));
    };

    //更新阅读进度
    @Override
    @PostMapping("upbookreadwhat")
    public String upbookreadwhatControllers(@RequestBody Map<String,String> map) throws Exception {
        return  bookservice.upbookreadwhatService(map.get("url"),map.get("username"),map.get("bookname"));
    }

    //获取小说所有章节
//    @Override
//    @PostMapping("allthepages")
//    public List<Allpages> allthepagescontrollers(@RequestBody Map<String,String> map2) throws Exception{
//        return bookservice.allthepagesService(map2.get("url2"),map2.get("bookname"));
//    }

    //获取章节小说
    @Override
    @PostMapping("readbook")
    public String readbookControllers(@RequestBody Map<String,String> map) throws Exception {
        return bookservice.readbookservice(map.get("url"));
    }

    //下一章
    @Override
    @PostMapping("havenextpage")
    public Map<String,String> nextpageControllers(@RequestBody Map<String,String> map) throws Exception {
        return bookservice.nextpageservice(map.get("url"),map.get("username"),map.get("bookname"));
    }

    //上一章
    @Override
    @PostMapping("bakconepage")
    public Map<String,String> bakconepageControllers(@RequestBody Map<String,String> map) throws Exception {
        return bookservice.bakconepageservice(map.get("url"),map.get("username"),map.get("bookname"));
    }

    //是否存在用户
    @Override
    @GetMapping("finCountUser")
    public int finCountUser(){
       return bookservice.finCountUser();
    }

    //获取某个用户书架,参数是token，判断一下是否为真
    @Override
    @PostMapping("findoneuserbooks")
    public List<Bookshelf> findoneuserbooks(@RequestBody Map<String,String> username){
       return bookservice.findoneuserbooks(username);
    }

    //将书本添加到书架中
    @Override
    @PostMapping("postbooktobooks")
    public int postupbookdata(@RequestBody Map<String,String> data) throws Exception {
        return bookservice.postupbookdata(data);
    }

    //在书架中去除书籍
    @Override
    @PostMapping("deleteuserbook")
    public void deleteuserbook(@RequestBody Map<String,String> data){
        bookservice.deleteuserbook(data);
    }

    //注册
    @Override
    @PostMapping("ishaveuser")
    public Object selectishaveuser(@RequestBody Map<String,String> data){
        return bookservice.selectishaveuser(data);
    }

    //登录
    @Override
    @PostMapping("userlogin")
    public Object userlogin(@RequestBody Map<String,String> data){
        return bookservice.userlogin(data);
    }

    //注销账号
    @Override
    @PostMapping("deleteuser")
    public void deleteUserController(@RequestBody Map<String,String> data){
        bookservice.deleteUserService(data);
    }

    @Override
    @GetMapping("allBookSource")
    public  List<BookSource> allBookSource(){
        return bookservice.allBookSource();
    }
}
