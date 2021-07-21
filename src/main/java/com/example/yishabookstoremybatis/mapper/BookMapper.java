package com.example.yishabookstoremybatis.mapper;

import com.example.yishabookstoremybatis.entity.Bookshelf;
import com.example.yishabookstoremybatis.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 如上图代码：使用@Mapper注解要定义成一个接口interface
 * 作用：
 * 1.使用@Mapper将NewsDAO接口交给Spring进行管理
 * 2.不用写Mapper映射文件（XML）
 * 3.为这个NewsDAO接口生成一个实现类，让别的类进行引用
 */
@Mapper
public interface BookMapper {
    /**
     *
     @Select、@Insert、@Update 以及 @Delete 四个注解分别对应 XML 中的 select、insert、
        update 以及 delete 标签，@Results 注解类似于 XML 中的 ResultMap 映射文件（getUserById
        方法给查询结果的字段取别名主要是向小伙伴们演示下 @Results 注解的用法）。
     另外使用 @SelectKey 注解可以实现主键回填的功能，即当数据插入成功后，插入成功的数据 id会赋值到 user 对象的id 属性上。
     UserMapper2 创建好之后，还要配置 mapper 扫描，有两种方式，一种是直接在 UserMapper2 上面添加 @Mapper 注解，
     这种方式有一个弊端就是所有的 Mapper 都要手动添加，要是落下一个就会报错，
        还有一个一劳永逸的办法就是直接在启动类上添加 Mapper 扫描@MapperScan(basePackages = "xxx.mapper")
      * @return
     */
//    @Select("select * from book")
//    List<Book> getAllBooks();

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




//    @Select("select * from user")
//    List<User> getAllUsers();
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "username", column = "u"),
//            @Result(property = "address", column = "a")
//    })
//    @Select("select username as u,address as a,id as id from user where id=#{id}")
//    User getUserById(Long id);
//
//    @Select("select * from user where username like concat('%',#{name},'%')")
//    List<User> getUsersByName(String name);
//
//    @Insert({"insert into user(username,address) values(#{username},#{address})"})
//    @SelectKey(statement = "select last_insert_id()", keyProperty = "id", before = false, resultType = Integer.class)
//    Integer addUser(User user);
//
//    @Update("update user set username=#{username},address=#{address} where id=#{id}")
//    Integer updateUserById(User user);
//
//    @Delete("delete from user where id=#{id}")
//    Integer deleteUserById(Integer id);
}
