package com.example.yishabookstoremybatis.controllers;

import com.example.yishabookstoremybatis.entity.Book;
import com.example.yishabookstoremybatis.entity.User;
import com.example.yishabookstoremybatis.mapper.BookMapper;

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
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BookMapper bookMapper;


    //显示所有书籍
    @GetMapping("findAllBooks")
    public List<Book> findAllBooks(){
        return bookMapper.getAllBooks();
    }

    //是否存在用户
    @GetMapping("finCountUser")
    public int finCountUser(){
       return bookMapper.getCountuser();
    }

    //获取某个用户书架,参数是token，判断一下是否为真
    @PostMapping("findoneuserbooks")
    public List<Book> findoneuserbooks(@RequestBody Map<String,String> username){
        String token = username.get("username");
        String admin = "user" + token.substring(32,token.length());
        String value = stringRedisTemplate.opsForValue().get(admin);
        if(value.equals(token)){
            return bookMapper.getuserbooks(token.substring(32,token.length()));
        }else{
            List<Book> listbook = new ArrayList<>();
            Book book = new Book();
            book.setBookid(-1);
            book.setBookname("-1");
            book.setBooksummary("-1");
            listbook.add(book);
            return listbook;
        }
    }

    //将书本添加到书架中
    @PostMapping("postbooktobooks")
    public int postupbookdata(@RequestBody Map<String,String> data){
        String token = data.get("token");
        String admin = "user" + token.substring(32,token.length());
        String value = stringRedisTemplate.opsForValue().get(admin);
        if(value.equals(token)){
            bookMapper.upbook(token.substring(32,token.length()),data.get("bookname"));
            return 1;
        }
        return 0;
    }

    //在书架中去除书籍
    @PostMapping("deleteuserbook")
    public void deleteuserbook(@RequestBody Map<String,String> data){
        String token = data.get("token");
        String admin = "user" + token.substring(32,token.length());
        String value = stringRedisTemplate.opsForValue().get(admin);
        if(value.equals(token)){
            bookMapper.updeletebook(token.substring(32,token.length()),data.get("bookname"));
        }

    }

    //注册
    @PostMapping("ishaveuser")
    public Object selectishaveuser(@RequestBody Map<String,String> data){
        Map<String,Object> jsonObject = new HashMap<>();
        User user  = bookMapper.ishaveuser(data.get("username"));
        //已经有用户注册了
        if (user != null) {
            jsonObject.put("respcode", 0);
        }else {
            bookMapper.insertuser(data.get("username"),data.get("userpassword"));
            jsonObject.put("respcode", 1);
        }
        // 向前端返回相应的json数据
        return jsonObject;
    }

    //登录
    @PostMapping("userlogin")
    public Object userlogin(@RequestBody Map<String,String> data){

        Map<String,Object> jsonObject = new HashMap<>();
        User user  = bookMapper.userlogin(data.get("username"),data.get("userpassword"));
        //有此账号
        if (user != null) {
            // 使用UUID作为token值
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // 在uuid后拼接管理员id组成最后的token值（添加id是为了方便后续验证）
            String token = uuid + user.getUsername();
            // 将用户的ID信息存入redis缓存，并设置两小时的过期时间
            stringRedisTemplate.opsForValue().set("user"+user.getUsername(), token, 7200, TimeUnit.SECONDS);
            jsonObject.put("respcode", 1);
            jsonObject.put("token", token);
        }else {
            jsonObject.put("respcode", 0);
        }
        // 向前端返回相应的json数据
        return jsonObject;
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
