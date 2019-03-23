package com.example.xieyo.roam.tools;

import android.net.Uri;
import android.util.Log;

import com.example.xieyo.roam.bookbean.BookData;
import com.example.xieyo.roam.bookbean.BookDigestData;
import com.example.xieyo.roam.bookbean.BookFragList;
import com.example.xieyo.roam.moviebean.MovieData;
import com.example.xieyo.roam.moviebean.MovieDigestData;
import com.example.xieyo.roam.moviebean.MovieFragList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieApi {
    public static List<MovieFragList> getNowPlaying(int count)
    {
        List<MovieFragList>  blist=new ArrayList<>();
        try{
            Document doc= Jsoup.connect("https://movie.douban.com/cinema/nowplaying/zhengzhou/").get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements movielist= doc.select("div[id=\"nowplaying\"]").select("ul[class=\"\"]");

            for (int i=0;i<count;i++)
            {
                MovieFragList mf=new MovieFragList();
                mf.coveruri=movielist.get(i).select("li[class=\"poster\"]").select("img").attr("src");
                mf.name=movielist.get(i).select("li[class=\"poster\"]").select("img").attr("alt");
                mf.movielink=movielist.get(i).select("li[class=\"poster\"]").select("a").attr("href");
                mf.rating=movielist.get(i).select("li[class=\"srating\"]").select("span[class=\"subject-rate\"]").text();
                mf.type=2;
                blist.add(mf);
            }


        }
        catch (Exception e)
        {

        }
        return blist;
    }



    public static List<MovieFragList> getHotMovie(int page,int count)
    {
        List<MovieFragList>  blist=new ArrayList<>();
        try{

            String Url="https://movie.douban.com/j/search_subjects?type=movie&tag=热门&sort=recommend&page_limit="+count+"&page_start="+(page-1)*count;
            JSONArray jsonArray=new JSONObject(getResult(Url)).getJSONArray("subjects");

            for (int i=0;i<count;i++)
            {
                MovieFragList mf=new MovieFragList();
                mf.coveruri=jsonArray.getJSONObject(i).getString("cover").replaceAll("\\\\","");
                mf.name=jsonArray.getJSONObject(i).getString("title");
                mf.movielink=jsonArray.getJSONObject(i).getString("url").replaceAll("\\\\","");
                mf.rating=jsonArray.getJSONObject(i).getString("rate");
                mf.type=2;
                 blist.add(mf);
            }


        }
        catch (Exception e)
        {

        }
        return blist;
    }

    public static List<MovieFragList> getotherlike(String link) {
        List<MovieFragList> bflist = new ArrayList<>();

        try {

            Document doc = Jsoup.connect(link).get();

            Elements recommened = doc.select("div[class=\"recommendations-bd\"]").select("dl[class=\"\"]");

            for (int i = 0; i < 9; i++) {
                MovieFragList bf = new MovieFragList();
                bf.type = 3;
                bf.movielink = recommened.get(i).select("dt").select("a").attr("href");
                bf.coveruri = recommened.get(i).select("dt").select("img").attr("src");
                bf.name = recommened.get(i).select("dd").select("a").text();
                bflist.add(bf);
            }

        } catch (Exception e) {

        }
        return bflist;
    }
    public static List<MovieFragList> getListbySearch(String text, int page) {
        List<MovieFragList> blist = new ArrayList<>();

        try {
            String url = "https://api.douban.com/v2/movie/search?q=" + Uri.encode(text) + "&start=" + page * 20;

            JSONArray jsonArray=new JSONObject(getResult(url)).getJSONArray("subjects");

            for (int i = 0; i < 20; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url
                MovieFragList bf = new MovieFragList();
                bf.movielink = jsonArray.getJSONObject(i).getString("alt").replaceAll("\\\\","");
                bf.coveruri = jsonArray.getJSONObject(i).getJSONObject("images").getString("medium");
                bf.name = jsonArray.getJSONObject(i).getString("title");
                //bf.artist = "导演："+jsonArray.getJSONObject(i).getJSONArray("directors").getJSONObject(0).getString("name");
                bf.rating = jsonArray.getJSONObject(i).getJSONObject("rating").getString("average");
                JSONArray array=jsonArray.getJSONObject(i).getJSONArray("casts");
                bf.artist="主演："+array.getJSONObject(0).getString("name")+"/"+array.getJSONObject(0).getString("name")+"/"+array.getJSONObject(0).getString("name");
                bf.type = 4;
                blist.add(bf);
            }
            // System.out.println(booklist.get(0).attr("artist"));
        } catch (Exception e) {
             Log.i("123456", "getListbySearch: "+e.toString());
        }
        return blist;
    }

    public static List<MovieFragList> getListbyIMDB(int page) {
        List<MovieFragList> blist = new ArrayList<>();

        try {
            String url = "http://www.imdb.cn/imdb250/" +page;

            Document doc=Jsoup.connect(url).get();


            Elements elements=doc.select("div[class=\"ss-3 clear\"]").select("a");

            for (int i = 0; i < 20; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url

               // System.out.println(elements.get(i).attr("href"));
                //System.out.println(elements.get(i).select("p[class=\"bb\"]").text());
               // System.out.println(elements.get(i).select("div[class=\"honghe-4 clear\"]").select("p").get(2).text());
              //  System.out.println(elements.get(i).select("div[class=\"honghe-2\"]").select("span").select("i").text());
               // System.out.println(elements.get(i).select("div[class=\"hong\"]").select("img").attr("src"));


                MovieFragList bf = new MovieFragList();
                bf.movielink ="http://www.imdb.cn"+elements.get(i).attr("href");
                bf.coveruri = elements.get(i).select("div[class=\"hong\"]").select("img").attr("src");
                bf.name =elements.get(i).select("p[class=\"bb\"]").text();
                //bf.artist = "导演："+jsonArray.getJSONObject(i).getJSONArray("directors").getJSONObject(0).getString("name");
                bf.rating = elements.get(i).select("div[class=\"honghe-2\"]").select("span").select("i").text();
                bf.artist="导演："+elements.get(i).select("div[class=\"honghe-4 clear\"]").select("p").get(2).text();
                bf.type = 4;
                blist.add(bf);
            }
            // System.out.println(booklist.get(0).attr("artist"));
        } catch (Exception e) {
            Log.i("123456", "getListbySearch: "+e.toString());
        }
        return blist;
    }

    public static List<MovieFragList> getListbyTag(String tag, int page) {
        List<MovieFragList> blist = new ArrayList<>();

        String url = "https://movie.douban.com/tag/" + tag + "?start=" + page * 20;
        try {
            Document doc = Jsoup.connect(url).get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements elements = doc.select("tr[class=\"item\"]");
            for (int i = 0; i < 20; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url
                MovieFragList bf = new MovieFragList();

                bf.movielink = elements.get(i).select("a[class=\"nbg\"]").attr("href");
                bf.coveruri = elements.get(i).select("a[class=\"nbg\"]").select("img").attr("src");
                bf.name = elements.get(i).select("a[class=\"nbg\"]").attr("title");
                bf.artist = elements.get(i).select("p[class=\"pl\"]").text();
                bf.rating = elements.get(i).select("div[class=\"star clearfix\"]").select("span[class=\"rating_nums\"]").text();
                bf.type = 4;
                blist.add(bf);
            }

            // System.out.println(booklist.get(0).attr("artist"));
        } catch (Exception e) {

        }

        return blist;
    }
    public static MovieData getIMDBData(String link)
    {
        MovieData md = new MovieData();
        List<String> starlist = new ArrayList<>();
        String id = new String();

        try {

            Document doc = Jsoup.connect(link).get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements estar = doc.select("span[class=\"rating_per\"]");
            if (estar.size() == 5) {
                for (int i = 0; i < 5; i++) {
                    starlist.add(estar.get(i).text());
                    //Log.i("123456", "getBookData: "+estar.get(i).text());
                }
                md.star.addAll(starlist);
            }


            Matcher matcher = Pattern.compile("subject/(.*)/").matcher(link);
            while (matcher.find()) {
                id = matcher.group(1);
            }
            JSONObject jsonObject = new JSONObject(getResult("https://api.douban.com/v2/movie/" + id));
            md.numraters = jsonObject.getJSONObject("rating").getString("numRaters");
            if(jsonObject.getJSONObject("rating").getString("average").length()>1)
            {
                md.averagerating = jsonObject.getJSONObject("rating").getString("average");
            }
            else
            {
                md.averagerating="0.0";
            }


            if (jsonObject.getString("summary")!=null) {
                md.introduce = jsonObject.getString("summary");
            } else {
                md.introduce = "暂无简介";
            }
            md.coveruri = jsonObject.getString("image");
            //  md.moviename =StringUtils.unicodeToCn(jsonObject.getString("alt_title")) ;
            md.moviename=doc.select("span[property=\"v:itemreviewed\"]").text();
            md.auther = doc.select("a[rel=\"v:directedBy\"]").text();
            md.longtime =StringUtils.unicodeToCn( jsonObject.getJSONObject("attrs").getJSONArray("movie_duration").get(0).toString());
            md.pubdate = StringUtils.unicodeToCn( jsonObject.getJSONObject("attrs").getJSONArray("year").get(0).toString());
            md.cast=doc.select("span[class=\"actor\"]").text();

        } catch (Exception e) {
        }
        return md;
    }

    public static MovieData getMovieData(String link)
    {
        MovieData md = new MovieData();
        List<String> starlist = new ArrayList<>();
        String id = new String();

        try {

            Document doc = Jsoup.connect(link).get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements estar = doc.select("span[class=\"rating_per\"]");
            if (estar.size() == 5) {
                for (int i = 0; i < 5; i++) {
                    starlist.add(estar.get(i).text());
                    //Log.i("123456", "getBookData: "+estar.get(i).text());
                }
                md.star.addAll(starlist);
            }


            Matcher matcher = Pattern.compile("subject/(.*)/").matcher(link);
            while (matcher.find()) {
                id = matcher.group(1);
            }
            JSONObject jsonObject = new JSONObject(getResult("https://api.douban.com/v2/movie/" + id));
            md.numraters = jsonObject.getJSONObject("rating").getString("numRaters");
            if(jsonObject.getJSONObject("rating").getString("average").length()>1)
            {
                md.averagerating = jsonObject.getJSONObject("rating").getString("average");
            }
            else
            {
                md.averagerating="0.0";
            }


            if (jsonObject.getString("summary")!=null) {
                md.introduce = jsonObject.getString("summary");
            } else {
                md.introduce = "暂无简介";
            }
            md.coveruri = jsonObject.getString("image");
          //  md.moviename =StringUtils.unicodeToCn(jsonObject.getString("alt_title")) ;
            md.moviename=doc.select("span[property=\"v:itemreviewed\"]").text();
            md.auther = doc.select("a[rel=\"v:directedBy\"]").text();
            md.longtime =StringUtils.unicodeToCn( jsonObject.getJSONObject("attrs").getJSONArray("movie_duration").get(0).toString());
            md.pubdate = StringUtils.unicodeToCn( jsonObject.getJSONObject("attrs").getJSONArray("year").get(0).toString());
            md.cast=doc.select("span[class=\"actor\"]").text();

        } catch (Exception e) {
        }
        return md;
    }

    public static List<MovieFragList> getDigest( int page) {
        List<MovieFragList> blist = new ArrayList<>();

        try {
            String url = "https://www.juzimi.com/allarticle/jingdiantaici"+"?page="+page;
            Document doc=Jsoup.parse(getDigestResult(url));


            Elements elements=doc.select("div[class=\"view-content\"]").select("div[class=\"views-field-tid\"]");

            Elements elements2=doc.select("div[class=\"view-content\"]").select("div[class=\"views-field-phpcode\"]");

            for (int i = 0; i < 20; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url
                MovieFragList bf = new MovieFragList();
                bf.movielink = "https://www.juzimi.com"+elements.get(i).select("a").attr("href");
                bf.coveruri ="https:"+elements.get(i).select("img").attr("src");
                bf.name = elements2.get(i).select("a[class=\"xqallarticletilelink\"]").text();
                //  bf.artist = elements.get(i).select("div[class=\"views-field-phpcode\"]").select("a").get(1).text();
                bf.part_digest= elements2.get(i).select("div[class=\"xqagepawirdesc\"]").text();
                bf.num_digest=elements2.get(i).select("div[class=\"xqagepawirdesclink\"]").text().split(" ")[1];
                bf.type = 5;
                blist.add(bf);
            }
            // System.out.println(booklist.get(0).attr("artist"));
        } catch (Exception e) {
            Log.i("123456", "getListbySearch: "+e.toString());
        }
        return blist;
    }
    public static List<MovieDigestData> getDigestList(String link, int page) {
        List<MovieDigestData> blist = new ArrayList<>();

        try {

            Document doc=Jsoup.parse(getDigestResult(link+"?page="+page));
            // Elements elements=doc.select("div[class=\"view-content\"]").select("div[class=\"views-field-tid\"]");

            Elements elements2=doc.select("div[class=\"view-content\"]").select("div[class=\"views-field-phpcode\"]");

            for (int i = 0; i < 10; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url
                MovieDigestData bf = new MovieDigestData();
                bf.content=elements2.get(i).select("a[title=\"查看本句\"]").text();
                bf.from=elements2.get(i).select("div[class=\"xqjulistwafo\"]").text();
                Log.i("123456", "getListbySearch: "+ bf.content);
                blist.add(bf);
            }
            // System.out.println(booklist.get(0).attr("artist"));
        } catch (Exception e) {
        }
        return blist;
    }

    private static String getDigestResult(final String url) {
        final StringBuilder sb = new StringBuilder();
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                BufferedReader br = null;
                InputStreamReader isr = null;
                HttpURLConnection conn;
                try {
                    URL geturl = new URL(url);
                    conn = (HttpURLConnection) geturl.openConnection();//创建连接
                    conn.setRequestMethod("GET");

                    conn.setRequestProperty("authority","www.juzimi.com");
                    conn.setRequestProperty("method","GET");
                    conn.setRequestProperty("cookie","SESSc60faee9ca2381b86f19bef9617d499b=up6bdht0c39p0pklhi16066mf2; xqrclbr=78584; __cfduid=dc187e3b265a1f135dca899a9f1095f891552313841; visited=1; UM_distinctid=16996641f78178-078340551ed3c2-9333061-144000-16996641f79177; CNZZDATA1256504232=1463944681-1553006338-%7C1553006338; has_js=1; Hm_lvt_0684e5255bde597704c827d5819167ba=1552997056,1552997156,1553008237,1553040401; xqrclm=; homere=2; xqrcli=MTU1MzA0MjczNywxNTM2Kjg2NCxXaW4zMixOZXRzY2FwZSw3ODU4NCw%3D; Hm_lpvt_0684e5255bde597704c827d5819167ba=1553042738; Hm_cv_0684e5255bde597704c827d5819167ba=1*login*PC-0!1*version*PC");
                    conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");



                    conn.connect();//get连接
                    InputStreamReader is = new InputStreamReader(conn.getInputStream(), "UTF-8");//输入流
                    br = new BufferedReader(is);
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");//获取输入流数据
                    }
                    //System.out.println(sb.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {//执行流的关闭
                    if (br != null) {
                        try {
                            if (br != null) {
                                br.close();
                            }
                            if (isr != null) {
                                isr.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return sb.toString();
            }
        });
        new Thread(task).start();
        String s = null;
        try {
            s = task.get();//异步获取返回值
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (s.startsWith("\ufeff")) {
            s = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
        }

        return s;
    }

    private static String getResult(final String url) {
        final StringBuilder sb = new StringBuilder();
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                BufferedReader br = null;
                InputStreamReader isr = null;
                HttpURLConnection conn;
                try {
                    URL geturl = new URL(url);
                    conn = (HttpURLConnection)geturl.openConnection();//创建连接
                    conn.connect();//get连接
                    InputStreamReader is = new InputStreamReader(conn.getInputStream(),"UTF-8");//输入流
                    br = new BufferedReader(is);
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");//获取输入流数据
                    }
                    //System.out.println(sb.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {//执行流的关闭
                    if (br != null) {
                        try {
                            if (br != null) {
                                br.close();
                            }
                            if (isr != null) {
                                isr.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } }}
                return sb.toString();
            }
        });
        new Thread(task).start();
        String s = null;
        try {
            s = task.get();//异步获取返回值
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (s.startsWith("\ufeff"))
        {
            //s= s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
        }
        return s;
    }
}
