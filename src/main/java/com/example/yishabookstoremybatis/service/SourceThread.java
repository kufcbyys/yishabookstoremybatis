package com.example.yishabookstoremybatis.service;

import com.example.yishabookstoremybatis.entity.Searchbookmodel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class SourceThread extends Thread {
    private Vector<Vector<Searchbookmodel>>  tvectors ;
    private CountDownLatch tcountDownLatch;

    //搜索界面网址
    String turl;
    //头网址
    String theadurl;
    //请求参数
    String thowtoparameter;
    //get为true
    Boolean thow;
    //请求要搜索的书名
    String tbookname;
    SourceThread(CountDownLatch countDownLatch,String url,String howtoparameter,String bookname,Boolean how,String headurl){
        tcountDownLatch = countDownLatch;
        turl = url;
        thowtoparameter = howtoparameter;
        thow = how;
        tbookname = bookname;
        theadurl = headurl;
    }

    public void init(Vector<Vector<Searchbookmodel>> vectors ) {
        tvectors = vectors;
    }

    @Override
    public void run() {
        Element elementall = null;
        Map<String,String> map = new HashMap<>();
        map.put(thowtoparameter,tbookname);
        try {
            if(thow){
                elementall =  Jsoup.connect(turl).data(map).timeout(8000).get().body();
                tvectors.add(to81zwsearch(elementall));
            }else{
                elementall = Jsoup.connect(turl).data(map).timeout(8000).post().body();
                //如果是笔趣阁1书源 执行对应方法,现在只有两个书源，默认是笔趣阁1
                tvectors.add(tobiqugesearch(elementall));
            }
            tcountDownLatch.countDown();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
    //笔趣阁1搜索
    private Vector<Searchbookmodel> tobiqugesearch(Element element){
        Vector<Searchbookmodel> vector1 = new Vector<>();
        //书名
        String[] booknames =  element.select("td:eq(0)").text().split("\\s+");
        //最新章节
        String[] booknewpages = element.select("td:eq(1)").text().split("\\s+");
        //作者
        String[] bookauthors = element.select("td:eq(2)").text().split("\\s+");
        //获取搜索界面后每个书籍的网址
        List<String> list = new ArrayList<>();
        org.jsoup.select.Elements a = element.select("td.even");
        for(Element i : a){
            org.jsoup.select.Elements b = i.getElementsByTag("a");
            for(Element k : b){
                list.add(k.attr("href"));
            }
        }
        //获取每个书籍网址的第一章
        List<String> bookonpageurl = new ArrayList<>();
        for (int g =0;g<list.size();g++){
            Element element1 = null;
            try {
                element1 = nextPage(list.get(g));
            } catch (Exception e) {
                e.printStackTrace();
            }
            org.jsoup.select.Elements elements = element1.select("div #list");
            for (Element q : elements){
                Elements w = q.getElementsByTag("dd");
                bookonpageurl.add(turl+w.get(0).getElementsByTag("a").attr("href"));
            }
        }
        //返回前端
        for(int i=0;i<booknames.length;i++){
            Searchbookmodel searchbookmodel = new Searchbookmodel();
            searchbookmodel.Searchbookname = booknames[i];
            searchbookmodel.Searchbooknewpage = booknewpages[i];
            searchbookmodel.Searchbookauthor = bookauthors[i];
            searchbookmodel.Searchbookoneurl = bookonpageurl.get(i);
            vector1.add(searchbookmodel);
        }
        return vector1;

    }

    //八一中文网
    private Vector<Searchbookmodel> to81zwsearch(Element element){
        Vector<Searchbookmodel> vector2 = new Vector<>();

        List<String> bookname = new ArrayList<>();
        List<String> bookauthor = new ArrayList<>();
        List<String> booknewpagetext = new ArrayList<>();

        //获取书名、作者、最新章节
        Elements elements1 = element.select("div .result-item").select("div .result-game-item");
        for(Element e : elements1){
            bookname.add(e.select("div .result-game-item-detail").
                    get(0).select("h3 .result-item-title").
                    select("h3 .result-game-item-title").
                    get(0).
                    getElementsByTag("span").text());
            bookauthor.add(e.select("div .result-game-item-info").
                    get(0).select("p .result-game-item-info-tag").
                    get(0).getElementsByTag("span").get(1).text());
            booknewpagetext.add(e.select("div .result-game-item-info").
                    get(0).select("p .result-game-item-info-tag").
                    get(3).getElementsByTag("a").text());
        }
        //获取搜索界面后每个书籍的网址
        List<String> list81 = new ArrayList<>();
        Elements a = element.select("div .result-item").
                select("div .result-game-item").select("div .result-game-item-pic").get(0).
                getElementsByTag("a");
        for(Element i : a){
            list81.add("https://www.81zw.com"+i.attr("href"));
        }
        //获取每个书籍第一章
        for(int q=0;q<list81.size();q++){

        }

        return vector2;
    }
    //笔趣阁获取第一章需要的请求头
    private static Element nextPage(String url) throws Exception{
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

}
