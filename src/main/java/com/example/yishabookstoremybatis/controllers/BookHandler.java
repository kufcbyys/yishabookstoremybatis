package com.example.yishabookstoremybatis.controllers;

import com.example.yishabookstoremybatis.entity.Book;
import com.example.yishabookstoremybatis.entity.User;
import com.example.yishabookstoremybatis.mapper.BookMapper;

import com.example.yishabookstoremybatis.service.Bookservice;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

//默认返回 json数据 controller 以及 responsebody合体
@RestController
@RequestMapping("/book")
public class BookHandler {


    @Autowired
    private Bookservice bookservice;


    //显示所有书籍
    @GetMapping("findAllBooks")
    public List<Book> findAllBooks(){
        return bookservice.findAllBooks();
    }

    //是否存在用户
    @GetMapping("finCountUser")
    public int finCountUser(){
       return bookservice.finCountUser();
    }

    //获取某个用户书架,参数是token，判断一下是否为真
    @PostMapping("findoneuserbooks")
    public List<Book> findoneuserbooks(@RequestBody Map<String,String> username){
       return bookservice.findoneuserbooks(username);
    }

    //将书本添加到书架中
    @PostMapping("postbooktobooks")
    public int postupbookdata(@RequestBody Map<String,String> data){
        return bookservice.postupbookdata(data);
    }

    //在书架中去除书籍
    @PostMapping("deleteuserbook")
    public void deleteuserbook(@RequestBody Map<String,String> data){
        bookservice.deleteuserbook(data);
    }

    //注册
    @PostMapping("ishaveuser")
    public Object selectishaveuser(@RequestBody Map<String,String> data){
        return bookservice.selectishaveuser(data);
    }

    //登录
    @PostMapping("userlogin")
    public Object userlogin(@RequestBody Map<String,String> data){
        return bookservice.userlogin(data);
    }




//    public void postupbookdata(HttpServletRequest req){
//        ServletInputStream is;
//        try {
//            is = req.getInputStream();
//            int nRead = 1;
//            int nTotalRead = 0;
//            byte[] bytes = new byte[10240];
//            while (nRead > 0) {
//                nRead = is.read(bytes, nTotalRead, bytes.length - nTotalRead);
//                if (nRead > 0)
//                    nTotalRead = nTotalRead + nRead;
//            }
//            String str = new String(bytes, 0, nTotalRead, "utf-8");
//            System.out.println("str"+str);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//
//    }


}
