package com.example.yishabookstoremybatis.service;

import com.example.yishabookstoremybatis.entity.Book;
import com.example.yishabookstoremybatis.entity.User;
import com.example.yishabookstoremybatis.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.xml.ws.Action;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class Bookservice {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private BookMapper bookMapper;

    //显示所有书籍
    public List<Book> findAllBooks(){
        return bookMapper.getAllBooks();
    }

    //是否存在用户
    public int finCountUser(){
        return bookMapper.getCountuser();
    }

    //获取某个用户书架,参数是token，判断一下是否为真
    public List<Book> findoneuserbooks(Map<String,String> username){
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
    public int postupbookdata(Map<String,String> data){
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
    public void deleteuserbook(Map<String,String> data){
        String token = data.get("token");
        String admin = "user" + token.substring(32,token.length());
        String value = stringRedisTemplate.opsForValue().get(admin);
        if(value.equals(token)){
            bookMapper.updeletebook(token.substring(32,token.length()),data.get("bookname"));
        }

    }

    //注册
    public Object selectishaveuser(Map<String,String> data){
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
    public Object userlogin(Map<String,String> data){
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



}
