package com.example.yishabookstoremybatis.mapper;

import com.example.yishabookstoremybatis.entity.Bookshelf;
import com.example.yishabookstoremybatis.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {

    //是否存在用户
    @Select("SELECT COUNT(username) FROM USER")
    int getCountuser();

    //获取某个用户书架
    @Select("SELECT * FROM bookshelf WHERE username = #{username}")
    List<Bookshelf> getuserbooks(String username);

    //将书本添加到书架中
    @Insert("insert into bookshelf (username,bookname,bookoneurl,bookauthor,bookcover) values (#{username},#{bookname},#{bookoneurl},#{bookauthor},#{bookcover2})")
    void upbook(String username,String bookname,String bookoneurl,String bookauthor,String bookcover2);

    //删除书架书籍
    @Delete("delete  from bookshelf where username = #{username} and bookname = #{bookname} ")
    void updeletebook(String username,String bookname);


    //查询用户是否已经被注册
    @Select("select * from user where username = #{username}  ")
    User ishaveuser(String username);

    //注册用户
    @Insert("insert into user (username,userpassword) values (#{username},#{userpassword})")
    void insertuser(String username,String userpassword);

    //用户登录
    @Select("select * from user where username = #{username} and userpassword = #{userpassword} ")
    User userlogin(String username,String userpassword);

    //更新阅读进度
    @Update("update bookshelf set bookoneurl=#{bookoneurl} where username=#{username} and bookname=#{bookname}")
    void updataurl(String bookoneurl,String username,String bookname);

    //删除用户以及书架里对应书籍
    @Delete("delete  from user where username=#{username}")
    void deleteUsermapper(String username);

    @Delete("delete  from bookshelf where username=#{username2}")
    void deleteBookshelfmapper(String username2);


}
