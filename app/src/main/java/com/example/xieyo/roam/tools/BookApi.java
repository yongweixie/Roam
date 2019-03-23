package com.example.xieyo.roam.tools;

import android.net.Uri;
import android.util.Log;

import com.example.xieyo.roam.bookbean.BookData;
import com.example.xieyo.roam.bookbean.BookDigestData;
import com.example.xieyo.roam.bookbean.BookFragList;

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

public class BookApi {

    public static List<BookFragList> getnewBook(int count) {
        List<BookFragList> blist = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://book.douban.com").get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements booklist = doc.select("div[class=\"section books-express \"]").select("li[class=\"\"]");

            for (int i = 0; i < count; i++) {
                BookFragList bf = new BookFragList();
                bf.type = 2;
                bf.name = booklist.select("div[class=\"cover\"]").get(i).select("a").attr("title");
                bf.coveruri = booklist.select("div[class=\"cover\"]").get(i).select("img").attr("src");
                bf.from = 1;
                bf.artist = booklist.select("div[class=\"info\"]").get(i).select("div[class=\"author\"]").text();
                bf.booklink = booklist.select("div[class=\"cover\"]").get(i).select("a").attr("href");
                //System.out.println(booklist.get(i).select("a").attr("href"));
                blist.add(bf);
            }
        } catch (Exception e) {

        }
        return blist;
    }

    public static List<BookFragList> getMostconcerned(int count) {
        List<BookFragList> blist = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://book.douban.com").get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements booklist = doc.select("div[class=\"section popular-books\"]").select("li[class=\"\"]");

            for (int i = 0; i < count; i++) {
                BookFragList bf = new BookFragList();
                bf.type = 3;
                bf.name = booklist.select("div[class=\"cover\"]").get(i).select("img").attr("alt");
                bf.coveruri = booklist.select("div[class=\"cover\"]").get(i).select("img").attr("src");
                bf.from = 2;
                bf.artist = booklist.select("div[class=\"info\"]").get(i).select("p[class=\"author\"]").text();
                bf.booklink = booklist.select("div[class=\"info\"]").get(i).select("h4[class=\"title\"]").select("a").attr("href");
                //System.out.println(booklist.get(i).select("a").attr("href"));
                bf.rating = booklist.select("div[class=\"info\"]").get(i).select("span[class=\"average-rating\"]").text();
                bf.classification = booklist.select("div[class=\"info\"]").get(i).select("p[class=\"book-list-classification\"]").text();
                bf.reviews = booklist.select("div[class=\"info\"]").get(i).select("p[class=\"reviews\"]").text();
                blist.add(bf);
            }


        } catch (Exception e) {

        }
        return blist;
    }

    public static List<BookFragList> getTop250(int count) {

        List<BookFragList> blist = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://book.douban.com").get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements booklist = doc.select("div[class=\"block5\"]").select("dl");

            for (int i = 0; i < count; i++) {
                BookFragList bf = new BookFragList();
                bf.booklink = booklist.get(i).select("dt").select("a").attr("href");
                bf.coveruri = booklist.get(i).select("dt").select("img").attr("src");
                bf.name = booklist.get(i).select("dd").select("a").text();
                bf.type = 2;
                blist.add(bf);
            }


        } catch (Exception e) {

        }
        return blist;
    }

    public static List<BookFragList> getBestSelling(int type) {
        List<BookFragList> blist = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://book.douban.com").get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements booklist = doc.select("div[class=\"section weekly-top\"]").select("ul[class=\"list list-ranking\"]").select("li[class=\"item\"]");

            if (type == 1) {

                for (int i = 0; i < 10; i++) {
                    BookFragList bf = new BookFragList();
                    bf.type = 4;
                    bf.sellindex = String.valueOf(i + 1);
                    bf.name = booklist.get(i).select("div[class=\"book-info\"]").select("a").text();
                    bf.artist = booklist.get(i).select("div[class=\"book-info\"]").select("div[class=\"author\"]").text();
                    bf.booklink = booklist.get(i).select("div[class=\"book-info\"]").select("a").attr("href");
                    blist.add(bf);
                }
            }
            if (type == 2) {

                for (int i = 10; i < 20; i++) {
                    BookFragList bf = new BookFragList();
                    bf.type = 4;
                    bf.sellindex = String.valueOf(i + 1 - 10);
                    bf.name = booklist.get(i).select("div[class=\"book-info\"]").select("a").text();
                    bf.artist = booklist.get(i).select("div[class=\"book-info\"]").select("div[class=\"author\"]").text();
                    bf.booklink = booklist.get(i).select("div[class=\"book-info\"]").select("a").attr("href");
                    blist.add(bf);

                }
            }


        } catch (Exception e) {

        }
        return blist;
    }

    public static List<BookFragList> getotherlike(String link) {
        List<BookFragList> bflist = new ArrayList<>();

        try {

            Document doc = Jsoup.connect(link).get();

            Elements recommened = doc.select("div[class=\"content clearfix\"]").select("dl[class=\"\"]");

            for (int i = 0; i < 9; i++) {
                BookFragList bf = new BookFragList();
                bf.type = 2;
                bf.booklink = recommened.get(i).select("dt").select("a").attr("href");
                bf.coveruri = recommened.get(i).select("dt").select("img").attr("src");
                bf.name = recommened.get(i).select("dd").select("a").text();
                bflist.add(bf);
            }

        } catch (Exception e) {

        }
        return bflist;
    }

    public static BookData getBookData(String link) {
        BookData bd = new BookData();
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
                bd.star.addAll(starlist);
            }

            Matcher matcher = Pattern.compile("subject/(.*)/").matcher(link);
            while (matcher.find()) {
                id = matcher.group(1);
            }
            JSONObject jsonObject = new JSONObject(getResult("https://douban.uieee.com/v2/book/" + id));
            bd.numraters = jsonObject.getJSONObject("rating").getString("numRaters");
            if (jsonObject.getJSONObject("rating").getString("average").length()>1)
            {
                bd.averagerating = jsonObject.getJSONObject("rating").getString("average");
            }
            else
            {
                bd.averagerating="0.0";
            }

            if (jsonObject.getString("author_intro").length() > 1) {
                bd.artistintroduce = jsonObject.getString("author_intro");
            } else {
                bd.artistintroduce = "暂无简介";
            }
            if (jsonObject.getString("catalog").length() > 1) {
                bd.catalog = jsonObject.getString("catalog");
            } else {
                bd.catalog = "暂无内容";
            }
            if (jsonObject.getString("summary").length() > 1) {
                bd.introduce = jsonObject.getString("summary");
            } else {
                bd.introduce = "暂无简介";
            }
            bd.coveruri = jsonObject.getString("image");
            bd.bookname = jsonObject.getString("title");
            bd.bookauther = jsonObject.getJSONArray("author").get(0).toString();
            bd.page = jsonObject.getString("pages");
            bd.pubdate = jsonObject.getString("pubdate");
            if(jsonObject.getJSONArray("translator").get(0).toString()!=null)
            {
                bd.translator = jsonObject.getJSONArray("translator").get(0).toString();
            }
            else
            {
                bd.translator="无";
            }

        } catch (Exception e) {
            Log.i("1234567", "getBookData: "+e.toString());
        }
        return bd;

    }
    public static List<BookFragList> getListbySearch(String text, int page) {
        List<BookFragList> blist = new ArrayList<>();

        try {
            String url = "https://douban.uieee.com/v2/book/search?q=" + Uri.encode(text) + "&start=" + page * 20;
          //  Log.i("123456789", "onQueryTextSubmit: "+url);

            JSONArray jsonArray=new JSONObject(getResult(url)).getJSONArray("books");

            for (int i = 0; i < 20; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url
                BookFragList bf = new BookFragList();
                bf.booklink = jsonArray.getJSONObject(i).getString("alt").replaceAll("\\\\","");
                bf.coveruri = jsonArray.getJSONObject(i).getString("image");
                bf.name = jsonArray.getJSONObject(i).getString("title");
                bf.artist = jsonArray.getJSONObject(i).getJSONArray("author").get(0).toString();
                bf.rating = jsonArray.getJSONObject(i).getJSONObject("rating").getString("average");
                bf.numraters=jsonArray.getJSONObject(i).getJSONObject("rating").getString("numRaters");
                bf.type = 5;
                blist.add(bf);
            }
            // System.out.println(booklist.get(0).attr("artist"));
        } catch (Exception e) {
          //  Log.i("123456", "getListbySearch: "+e.toString());
        }
        return blist;
    }
    public static List<BookFragList> getDigest( int page) {
        List<BookFragList> blist = new ArrayList<>();

        try {
            String url = "https://www.juzimi.com/allarticle/zhaichao"+"?page="+page;
            Document doc=Jsoup.parse(getDigestResult(url));


            Elements elements=doc.select("div[class=\"view-content\"]").select("div[class=\"views-field-tid\"]");

            Elements elements2=doc.select("div[class=\"view-content\"]").select("div[class=\"views-field-phpcode\"]");

            for (int i = 0; i < 20; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url
                BookFragList bf = new BookFragList();
                bf.booklink = "https://www.juzimi.com"+elements.get(i).select("a").attr("href");
                bf.coveruri ="https:"+elements.get(i).select("img").attr("src");
                bf.name = elements2.get(i).select("a[class=\"xqallarticletilelink\"]").text();
              //  bf.artist = elements.get(i).select("div[class=\"views-field-phpcode\"]").select("a").get(1).text();
                bf.part_digest= elements2.get(i).select("div[class=\"xqagepawirdesc\"]").text();
                bf.num_digest=elements2.get(i).select("div[class=\"xqagepawirdesclink\"]").text().split(" ")[1];
                bf.type = 6;
                blist.add(bf);
            }
            // System.out.println(booklist.get(0).attr("artist"));
        } catch (Exception e) {
             Log.i("123456", "getListbySearch: "+e.toString());
        }
        return blist;
    }
    public static List<BookDigestData> getDigestList(String link,int page) {
        List<BookDigestData> blist = new ArrayList<>();

        try {

            Document doc=Jsoup.parse(getDigestResult(link+"?page="+page));


           // Elements elements=doc.select("div[class=\"view-content\"]").select("div[class=\"views-field-tid\"]");

            Elements elements2=doc.select("div[class=\"view-content\"]").select("div[class=\"views-field-phpcode\"]");

            for (int i = 0; i < 10; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url
                BookDigestData bf = new BookDigestData();
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

    public static List<BookFragList> getListbyTag(String tag, int page) {
        List<BookFragList> blist = new ArrayList<>();

        String url = "https://book.douban.com/tag/" + tag + "?start=" + page * 20;
        try {
            Document doc = Jsoup.connect(url).get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements elements = doc.select("ul[class=\"subject-list\"]").select("li[class=\"subject-item\"]");
            for (int i = 0; i < 20; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url
                BookFragList bf = new BookFragList();

                bf.booklink = elements.get(i).select("a[class=\"nbg\"]").attr("href");
                bf.coveruri = elements.get(i).select("a[class=\"nbg\"]").select("img").attr("src");
                bf.name = elements.get(i).select("a").get(1).text();
                bf.artist = elements.get(i).select("div[class=\"pub\"]").text();
                bf.rating = elements.get(i).select("div[class=\"star clearfix\"]").select("span[class=\"rating_nums\"]").text();
                bf.reviews = elements.get(i).select("p").text();
                bf.type = 3;
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

    public static List<BookFragList> getAll250(int page) {
        List<BookFragList> blist = new ArrayList<>();
        String url = new String();


        url = "https://book.douban.com/top250?start=" + page * 25;//非虚构类
        try {
            Document doc = Jsoup.connect(url).get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements elements = doc.select("div[class=\"article\"]").select("tr[class=\"item\"]");

            for (int i = 0; i < 25; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url
                BookFragList bf = new BookFragList();

                bf.booklink = elements.get(i).select("a[class=\"nbg\"]").attr("href");
                bf.coveruri = elements.get(i).select("a[class=\"nbg\"]").select("img").attr("src");
                bf.name = elements.get(i).select("div[class=\"pl2\"]").select("a").text();
                bf.artist = elements.get(i).select("p[class=\"pl\"]").text();
                bf.rating = elements.get(i).select("div[class=\"star clearfix\"]").select("span[class=\"rating_nums\"]").text();
                bf.reviews = elements.get(i).select("p[class=\"quote\"]").select("span").text();
                bf.type = 3;
                blist.add(bf);
            }

            // System.out.println(booklist.get(0).attr("artist"));
        } catch (Exception e) {

        }
        return blist;
    }


    public static List<BookFragList> getAllConcerned(int type) {
        List<BookFragList> blist = new ArrayList<>();
        String url = new String();

        if (type == 2) {
            url = "https://book.douban.com/chart?subcat=I";//非虚构类
        }
        if (type == 1) {
            url = "https://book.douban.com/chart?subcat=F";//虚构类
        }

        try {
            Document doc = Jsoup.connect(url).get();
            //Elements booklist= doc.select("ul[class=\"list-col list-col5 list-express slide-item\"]");
            Elements elements = doc.select("div[class=\"grid-16-8 clearfix\"]").select("li[class=\"media clearfix\"]");

            for (int i = 0; i < 10; i++) {
                // System.out.println(booklist.get(i).select("a").attr("href"));//url
                BookFragList bf = new BookFragList();
                bf.booklink = elements.get(i).select("div[class=\"media__img\"]").select("a").attr("href");
                bf.coveruri = elements.get(i).select("div[class=\"media__img\"]").select("img").attr("src");
                bf.artist = elements.get(i).select("div[class=\"media__body\"]").select("p[class=\"subject-abstract color-gray\"]").text().split("/")[0];
                bf.name = elements.get(i).select("div[class=\"media__body\"]").select("h2[class=\"clearfix\"]").select("a").text();
                bf.type = 2;
                blist.add(bf);
            }

            // System.out.println(booklist.get(0).attr("artist"));
        } catch (Exception e) {

        }
        return blist;
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
                    conn = (HttpURLConnection) geturl.openConnection();//创建连接
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


}
