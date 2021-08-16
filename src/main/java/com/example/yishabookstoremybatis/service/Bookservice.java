package com.example.yishabookstoremybatis.service;

import com.example.yishabookstoremybatis.entity.*;
import com.example.yishabookstoremybatis.mapper.BookMapper;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class Bookservice{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BookMapper bookMapper;

//    private static String headerurl = "https://www.xbiquge.la";
//    private static String url = "https://www.xbiquge.la/modules/article/waps.php";

    private  List<BookSource> tobooksource;

    //查找书源
    public List<BookSource> allBookSource(){
        return bookMapper.selectBookSource();
    }
    //初始化书源
    void initbooksource(){
        if(tobooksource == null){
            tobooksource = bookMapper.selectBookSource();
        }
    }
    //通过搜索查询书籍
    public Vector<Vector<Searchbookmodel>> searchbookservice(String name) {
        initbooksource();
        Vector<Vector<Searchbookmodel>> vectors = new Vector<>();//定义一个Vector做为存储返回结果的容器；
        final CountDownLatch countDownLatch = new CountDownLatch(tobooksource.size());
        // 启动多个工作线程
        for (int i = 0; i < tobooksource.size(); i++) {
            SourceThread sourceThread = new SourceThread(countDownLatch,tobooksource.get(i).getSourceurl(),
                    tobooksource.get(i).getHowtoparameter(),
                    name
                    ,tobooksource.get(i).getHowto().equals("get"),tobooksource.get(i).getSourceheaderurl());
            sourceThread.init(vectors);
            sourceThread.start();
        }
        try {
            countDownLatch.await();
        }catch (InterruptedException interruptedException){
            //等待修改为日志形式
            interruptedException.printStackTrace();
        }
        return vectors;
//        Map<String,String> map = new HashMap<>();
//        map.put("searchkey",name);
//        List<Searchbookmodel> searchbookmodels = new ArrayList<>();
//        Element element = null;
//        try {
//            Connection conn = Jsoup.connect(url)
//                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                    .header("Accept-Encoding", "gzip, deflate, br")
//                    .header("Accept-Language", "zh-CN,zh;q=0.9")
//                    .header("Cache-Control", "max-age=0")
//                    .header("Connection", "keep-alive")
//                    .header("Host", "www.xbiquge.la")
//                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
//                    .data(map).timeout(8000);
//            element = conn.execute().parse().body();
//        } catch (IOException e) {
//            System.out.println("爬取超时或其他异常");
//            e.printStackTrace();
//        }
//
//        String[] booknames =  element.select("td:eq(0)").text().split("\\s+");
//        String[] booknewpages = element.select("td:eq(1)").text().split("\\s+");
//        String[] bookauthors = element.select("td:eq(2)").text().split("\\s+");
//        //获取搜索界面后每个书籍的网址
//        List<String> list = new ArrayList<>();
//        Elements a = element.select("td.even");
//        for(Element i : a){
//            Elements b = i.getElementsByTag("a");
//            for(Element k : b){
//                list.add(k.attr("href"));
//            }
//        }
//        //获取每个书籍网址的第一章
//        List<String> bookonpageurl = new ArrayList<>();
//        for (int g =0;g<list.size();g++){
//            Element element1 = null;
//            try {
//                element1 = nextPage2(list.get(g));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Elements elements = element1.select("div #list");
//            for (Element q : elements){
//                Elements w = q.getElementsByTag("dd");
//                bookonpageurl.add(headerurl+w.get(0).getElementsByTag("a").attr("href"));
//            }
//        }
//        //返回前端
//        for(int i=0;i<booknames.length;i++){
//            Searchbookmodel searchbookmodel = new Searchbookmodel();
//            searchbookmodel.Searchbookname = booknames[i];
//            searchbookmodel.Searchbooknewpage = booknewpages[i];
//            searchbookmodel.Searchbookauthor = bookauthors[i];
//            searchbookmodel.Searchbookoneurl = bookonpageurl.get(i);
//            searchbookmodels.add(searchbookmodel);
//        }
//        return searchbookmodels;
    }
    //jsoup脚本
    private static Element nextPage2(String url) throws Exception{
        // 获取连接实例，伪造浏览器身份
        Connection conn = Jsoup.connect(url)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .header("Cache-Control", "max-age=0")
                .header("Connection", "keep-alive")
                .header("Host", "www.xbiquge.la")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        return conn.execute().parse().body();
    }

    //获取章节小说数据
    public String readbookservice(String url) throws Exception {
        Element element = nextPage2(url);
        return outputToFile(element);
    }
    private static String outputToFile(Element element) throws IOException {
        String title = getTitle(element);
        String content = getContent(element);
        String text = "<h1>" + title +"</h1>" + "<br/>" + content;
        return text;
    }
    // 获取当前章节标题
    private static String getTitle(Element element) {
        return element.select(".bookname h1").text();
    }
    // 获取章节具体内容
    private static String getContent(Element element) {
        // 删除底部P标签的广告内容
        element.getElementsByTag("p").remove();
        // 获取到ID为content的所有HTML内容
        String body = element.select("#content").html();
        return body;
        // 对body进行处理，返回正常格式的内容
//        body = body.replace("&nbsp;&nbsp;&nbsp;&nbsp;", "    ");
//        body = body.replace("<br>", "");
//        return body.replace(" \n \n", "\r\n");
    }

    //单独更新阅读进去
    public String upbookreadwhatService(String url,String username,String bookname) throws Exception{
        bookMapper.updataurl(url,username,bookname);
        return this.readbookservice(url);
    }

    //获取下一章
    public Map<String,String> nextpageservice(String url,String username,String bookname) throws Exception {
        Element element  = nextPage2(url);
        String nexturl =  hasNext(element);
        Map<String,String> map = new HashMap<>();
        if(nexturl != null){
            bookMapper.updataurl(nexturl,username,bookname);
            map.put("nowurl",nexturl);
            map.put("bookshow",readbookservice(nexturl));
            return map;
        }
        map.put("nowurl",url);
        map.put("bookshow","<p>未找到下一章</p>");
        return map;

    }
    //获取上一章
    public Map<String,String> bakconepageservice(String url,String username,String bookname) throws Exception {
        Element element  = nextPage2(url);
        String backoneurl =  hasbackone(element);
        Map<String,String> map = new HashMap<>();
        if(backoneurl != null){
            bookMapper.updataurl(backoneurl,username,bookname);
            map.put("nowurl",backoneurl);
            map.put("bookshow",readbookservice(backoneurl));
            return map;
        }
        map.put("nowurl",url);
        map.put("bookshow","<p>未找到上一章</p>");
        return map;

    }

    // 是否有下一页？有返回下一页URL地址，没有就返回NULL
    private static String hasNext(Element element) {
        // 找到"下一章"的按钮，获取跳转的目标地址
        Elements div = element.getElementsByClass("bottem2");
            Element a = div.get(0).getElementsByTag("a").get(3);
        String href = a.attr("href");
        // 通过观察存在下一章的时候URL会以.html结尾，不存在时会跳转到首页，通过这个特点判断是否存在下一章
        return href.endsWith(".html") ? "https://www.xbiquge.la" + href : null;
    }

    // 是否有上一页？有返回上一页URL地址，没有就返回NULL
    private static String hasbackone(Element element) {
        // 找到"下一章"的按钮，获取跳转的目标地址
        Elements div = element.getElementsByClass("bottem2");
        Element a = div.get(0).getElementsByTag("a").get(1);
        String href = a.attr("href");
        // 通过观察存在下一章的时候URL会以.html结尾，不存在时会跳转到首页，通过这个特点判断是否存在下一章
        return href.endsWith(".html") ? "https://www.xbiquge.la" + href : null;
    }



    //是否存在用户
    public int finCountUser(){
        return bookMapper.getCountuser();
    }

    //获取某个用户书架,参数是token，判断一下是否为真
    public List<Bookshelf> findoneuserbooks(Map<String,String> username){
        String token = username.get("username");
        String admin = "user" + token.substring(32,token.length());
        String value = stringRedisTemplate.opsForValue().get(admin);
        List<Bookshelf> listbook = new ArrayList<>();
        Bookshelf bookshelf = new Bookshelf();
        if(value == null){
            bookshelf.setShelfid(-2);
            bookshelf.setBookname("-2");
            bookshelf.setBookauthor("-2");
            bookshelf.setBookoneurl("-2");
            bookshelf.setUsername("-2");
            listbook.add(bookshelf);
            return listbook;
        }
        if(value.equals(token) && value!=null){
            return bookMapper.getuserbooks(token.substring(32,token.length()));
        }
        bookshelf.setShelfid(-1);
        bookshelf.setBookname("-1");
        bookshelf.setBookauthor("-1");
        bookshelf.setBookoneurl("-1");
        return listbook;
    }

    //将书本添加到书架中
    public int postupbookdata(Map<String,String> data) throws Exception {
        String token = data.get("token");
        String admin = "user" + token.substring(32,token.length());
        String value = stringRedisTemplate.opsForValue().get(admin);
        //获取小说第一章后，读取小说图像
        Element element = nextPage2(data.get("bookoneurl"));
        Elements div = element.getElementsByClass("bottem1");
        //获取章节目录地址
        Element a = div.get(0).getElementsByTag("a").get(2);
        String bookcover = a.attr("href");
        Element element1 = nextPage2(bookcover);
        Elements element2 = element1.select("div #fmimg");
        Element b = element2.get(0).getElementsByTag("img").get(0);
        String bookcover2 = b.attr("src");
        if(value.equals(token)){
            bookMapper.upbook(token.substring(32,token.length()),data.get("bookname"),
                    data.get("bookoneurl"),data.get("bookauthor"),bookcover2);
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

    //注销账号
    public void deleteUserService(Map<String,String> map){
        String token = map.get("token");
        String admin = "user" + token.substring(32,token.length());
        String value = stringRedisTemplate.opsForValue().get(admin);
        //如果用户没过期以及匹配成功
        if(value!=null && value.equals(token)){
            bookMapper.deleteUsermapper(token.substring(32,token.length()));
            bookMapper.deleteBookshelfmapper(token.substring(32,token.length()));
        }
        stringRedisTemplate.delete("admin");

    }

    //获取小说所有章节
//    public List<Allpages> allthepagesService(String url2,String bookname) throws Exception {
//        List<Allpages> list = new ArrayList<>();
//        Long num = stringRedisTemplate.opsForList().size(bookname);
//        //已经存在redis里了,left存就向后拿
//        if( num != 0 || num == null){
//            for(long i=num-1;i>=0;i--){
//                Allpages allpages = new Allpages();
//                allpages.titlename = stringRedisTemplate.opsForList().index(bookname,i);
//                allpages.pagesurl = stringRedisTemplate.opsForList().index(bookname+"url",i);
//                list.add(allpages);
//            }
//            return list;
//        }
//        Connection conn = Jsoup.connect(url2)
//                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .header("Accept-Encoding", "gzip, deflate, br")
//                .header("Accept-Language", "zh-CN,zh;q=0.9")
//                .header("Cache-Control", "max-age=0")
//                .header("Connection", "keep-alive")
//                .header("Host", "www.xbiquge.la")
//                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
//        Element elementx1 =  conn.execute().parse().body();
//        String allurl =  elementx1.select("div .bottem1").get(0).getElementsByTag("a").get(2).attr("href");
//        Element elementx2 = nextPage2(allurl);
//        Elements elementsx3 =  elementx2.select("div #list").get(0).getElementsByTag("dl").get(0).getElementsByTag("dd");
//        for(Element a : elementsx3){
//            stringRedisTemplate.opsForList().leftPush(bookname,a.getElementsByTag("a").text());
//            stringRedisTemplate.opsForList().leftPush(bookname+"url",headerurl+a.getElementsByTag("a").attr("href"));
//            Allpages allpages = new Allpages();
//            allpages.titlename = a.getElementsByTag("a").text();
//            allpages.pagesurl = headerurl+a.getElementsByTag("a").attr("href");
//           list.add(allpages);
//        }
//        return list;
//    }

}
