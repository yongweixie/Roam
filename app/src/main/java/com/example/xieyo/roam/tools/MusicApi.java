package com.example.xieyo.roam.tools;

import android.util.Log;

import com.example.xieyo.roam.baseinfo.MusicBaseInfo;
import com.example.xieyo.roam.musicbean.Music;
import com.example.xieyo.roam.musicbean.MusicHallList;
import com.example.xieyo.roam.musicbean.MusicList;

import org.json.JSONException;
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

public class MusicApi {

    public static final int TYPE_QQ = 1;
    public static final int TYPE_CLOUD_MUSIC=2;


    private static String getLrctext=new String();
    private static List<MusicHallList>  musicListArrayList=new ArrayList<>();
    private static List<Music> musiclist=new ArrayList<>();
    public static List<Music> searchlist=new ArrayList<>();

    public static List<Music> getMusicfromList(String ID, int from)
    {
        musiclist=new ArrayList<>();
        String QQMusicListpath=new String();
        if(from==1)
        {
             QQMusicListpath="https://api.bzqll.com/music/tencent/songList?key=579621905&id="+ID;
            pasingMusicFromList(getResult(QQMusicListpath),from);
        }
        if (from==2)
        {
            QQMusicListpath=" https://api.bzqll.com/music/netease/songList?key=579621905&id="+ID;
            pasingMusicFromList(getResult(QQMusicListpath),from);
        }
        if (from==3)
        {
            QQMusicListpath="https://api.bzqll.com/music/kugou/songList?key=579621905&id="+ID;
            pasingMusicFromList(getResult(QQMusicListpath),from);
        }

        return musiclist;
    }

    public static  List<Music> getSearchList(String msg, int count, int page, int from)
    {
        searchlist=new ArrayList<>();
        if(from==1)
        {
            String path="https://api.bzqll.com/music/tencent/search?key=579621905&s="+msg+"&limit="+count+"&offset="+(page-1)*count+"&type=song";
            pasingMusicFromSearch(getResult(path),count,from);
        }
        if(from==2)
        {
            String path="https://api.bzqll.com/music/netease/search?key=579621905&s="+msg+"&type=song&limit="+count+"&offset="+(page-1)*count;
            pasingMusicFromSearch(getResult(path),count,from);
        }
        if(from==3)
        {
            String path="https://api.bzqll.com/music/kugou/search?key=579621905&s="+msg+"&limit="+count+"&offset="+(page-1)*count+"&type=song";
            pasingMusicFromSearch(getResult(path),count,from);
        }
        return searchlist;
    }
    public  static  List<String> getHotSearch(int from)
    {
        List<String> list=new ArrayList<String>();
        if (from==2)
        {
            String path="https://music.163.com/discover/toplist?id=19723756";
            //String getData=getResult(path);
            try {
                Document doc=Jsoup.connect(path).get();
               // JSONObject jsonObject = new JSONObject(getData);
                Elements elements=doc.select("ul[class=\"f-hide\"]").select("li");
                for (int i=0;i<10;i++)
                {
                   // JSONObject object= jsonObject.getJSONObject("data").getJSONArray("songs").getJSONObject(i);
                    list.add(elements.get(i).select("a").text());

                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }


        return  list;
    }
    public static MusicList getMusitListdata(String listID, int from)
    {
        MusicList ml=new MusicList();
        if (from==2)
        {
            final String url="https://music.163.com/playlist?id="+listID ;

            try {

                Document doc = Jsoup.connect(url).get();

                Elements data= doc.select("script[type=\"application/ld+json\"]");
                // Elements userdata= doc.getElementsByClass("user f-cb").select("a[class=\"face\"]");
                Elements useruri= doc.getElementsByClass("user f-cb").select("a[class=\"face\"]").select("img");
                Elements  username=doc.getElementsByClass("user f-cb").select("a[href]");
                //
                Elements  playcount=doc.getElementsByClass("more s-fc3").select("strong[id]");
                Elements  favcount=doc.select("a[data-res-id]");
                Elements desc= doc.select("p[id=\"album-desc-more\"]");
                ml.creator=username.text();
                ml.userface=useruri.attr("src");
                Matcher matcher = Pattern.compile("</b>([\\s\\S]*)</p>").matcher(desc.toString());
                while (matcher.find())
                {
                    ml.desc=matcher.group(1).replaceAll("<br>","\n").replaceAll("&gt|&lt","");
                }
                ml.from=from;
                ml.playCount=NumberUtils.amountConversion(Integer.valueOf(playcount.text()));
                ml.favCount=favcount.get(2).attr("data-count");

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        if(from==1)
        {
            final String url="https://y.qq.com/n/m/detail/taoge/index.html?ADTAG=newyqq.taoge&id="+listID ;

            try {

                Document doc = Jsoup.connect(url).get();

                Elements desc= doc.select("meta[name=\"description\"]");
                Elements user= doc.select("img[class=\"author__avatar\"]");
                Elements playcount= doc.select("p[class=\"album__desc\"]");


                // Elements userdata= doc.getElementsByClass("user f-cb").select("a[class=\"face\"]");
                //Matcher matcher = Pattern.compile("</b>([\\s\\S]*)</p>").matcher(data.toString());
//            while (matcher.find())
//            {
//                System.out.println(matcher.group(1).replaceAll("<br>","\n"));
//            }
                ml.desc=desc.attr("content");
                ml.playCount=playcount.text().substring(4);
                ml.userface=user.attr("src");
                ml.creator=user.attr("alt");
//                System.out.println(desc.attr("content"));
//                System.out.println(user.attr("src"));
//                System.out.println(user.attr("alt"));
//                System.out.println(playcount.text().substring(4));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(from==3)
        {
            final String url="https://www.kugou.com/yy/special/single/"+listID+".html" ;

            try {

                Document doc = Jsoup.connect(url).get();

                Elements desc= doc.select("p[class=\"more_intro\"]");
                Elements user= doc.select("p[class=\"detail\"]");


                //Elements userdata= doc.getElementsByClass("user f-cb").select("a[class=\"face\"]");
                Matcher matcher = Pattern.compile("创建人：(.*)心情").matcher(user.text().toString());
                while (matcher.find())
                {
                    //System.out.println(matcher.group(1));
                    ml.creator=matcher.group(1);
                }
                //System.out.println(desc.text());
                ml.desc=desc.text();
                ml.userface="http://imge.kugou.com/kugouicon/165/20120724/20120724145917274529.jpg";
                // System.out.println(user.text());
                ml.playCount= MusicBaseInfo.MusicListPlayCount;


                // System.out.println(data.get(i).child(0).child(0).attr("alt"));

                //  System.out.println(data.get(i).attr("title"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ml;
    }
    public  static List<MusicHallList> getMusicList( int page,int count,int from)
    {
        musicListArrayList=new ArrayList<>();
        if (from==2)
        {


            try {

                String url="https://music.163.com/discover/playlist/?order=hot&cat=%E5%85%A8%E9%83%A8&limit="+count+"&offset="+(page-1)*count;
                Document doc = Jsoup.connect(url).get();
                Elements elements= doc.select("div[class=\"u-cover u-cover-1\"]");
                //Log.i("123456", "getMusicList: "+doc);

                for(int i=0;i<count;i++)
                {
                    MusicHallList ml=new MusicHallList();
                    //System.out.println(object.getString("dissname"));
                    ml.imageUri=elements.get(i).select("img").attr("src").replaceAll("140y140","800y800");
                    ml.playCount =elements.get(i).select("span[class=\"nb\"]").text();
                    ml.id=elements.get(i).select("a[class=\"icon-play f-fr\"]").attr("data-res-id");
                    ml.title=elements.get(i).select("a").get(0).attr("title");
                    ml.from = 2;
                    ml.type=2;
                    musicListArrayList.add(ml);
                }
            }
            catch (Exception e)
            {
                Log.i("123456", "getMusicList: "+e.toString());
            }


        }
        if(from==1)
        {
            String url="https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_diss_by_tag.fcg?picmid=" +
                    "1&inCharset=utf8&outCharset=utf-8&categoryId=10000000&sortId=5&sin="+(page-1)*count+"&ein="+(page*count-1);
            Matcher matcher= Pattern.compile("\\((.*)\\)").matcher(getQQResult(url));
            while (matcher.find())
            {
                try
                {
                    JSONObject jsonobject=  new JSONObject(matcher.group(1));
                    for (int i=0;i<count;i++)
                    {
                        MusicHallList ml=new MusicHallList();
                        JSONObject object=jsonobject.getJSONObject("data").getJSONArray("list").getJSONObject(i);
                        //System.out.println(object.getString("dissname"));
                        ml.imageUri=object.getString("imgurl");
                        ml.playCount = NumberUtils.amountConversion(Integer.valueOf(object.getString("listennum")));
                        ml.id=object.getString("dissid");
                        ml.title=object.getString("dissname");
                        ml.from = 1;
                        ml.type=2;
                        musicListArrayList.add(ml);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();

                }
                //System.out.println(matcher.group(1));

            }
        }
        if(from==3)
        {
            final String url="http://m.kugou.com/plist/index/"+page;

            try {

                Document doc = Jsoup.parse(getResult(url));
                Elements title= doc.select("p[class=\"panel-img-content-first\"]");
                Elements pic= doc.select("div[class=\"panel-img-left\"]");
                Elements playcount=doc.select("p[class=\"panel-img-content-sub\"]");
                Elements ID=doc.getElementsByClass("panel-img-list").select("li");
                //Elements pic= doc.select("img[_src$=.jpg]");
                for(int i=0;i<count;i++)
                {
                    //System.out.println(title.get(i).text());
                   // System.out.println(pic.get(i).child(0).attr("_src"));
                   // System.out.println(playcount.get(i).text());
                    MusicHallList mc=new MusicHallList();
                    mc.type=2;
                    mc.from=from;
                    mc.title=title.get(i).text();
                    mc.id=ID.get(i).child(0).attr("href").split("/")[5];
                    mc.playCount= NumberUtils.amountConversion(Integer.valueOf(playcount.get(i).text()));
                    mc.imageUri=pic.get(i).child(0).attr("_src");
                    musicListArrayList.add(mc);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return musicListArrayList;
    }

    public static String getLrc(String key)//作者，歌名
    {
        getLrctext=new String();
        String QQLRCpath="https://api.bzqll.com/music/tencent/search?key=579621905&s="+key+"&limit=100&offset=0&type=song";
        pasingLrc(getResult(QQLRCpath));
        return getLrctext;
    }
    public static String getLrcbyID(String ID,int from)//作者，歌名
    {
        getLrctext=new String();
        String NeteaseLRCpath="https://api.bzqll.com/music/netease/lrc?key=579621905&id="+ID;
        String QQLRCpath="https://api.bzqll.com/music/tencent/lrc?key=579621905&id="+ID;
        String Kugoupath="https://api.bzqll.com/music/kugou/lrc?key=579621905&id="+ID;
        if(from==1)
        {
            getLrctext= getResult(QQLRCpath);
        }
        if(from==2)
        {
            getLrctext= getResult(NeteaseLRCpath);
        }
        if (from == 3) {

            getLrctext= getResult(Kugoupath);

        }
        return  getLrctext;
    }

    public static String getQQResult(final String url){
        final StringBuilder sb = new StringBuilder();
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                BufferedReader br = null;
                InputStreamReader isr = null;

                try {
                    URL geturl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection )geturl.openConnection();//创建连接
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Origin", "https://y.qq.com");
                    conn.setRequestProperty("Referer", "https://y.qq.com/portal/playlist.html");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36");


//                conn.setRequestProperty("Origin", "https://y.qq.com");
//                conn.setRequestProperty("Origin", "https://y.qq.com");
//                conn.setRequestProperty("Origin", "https://y.qq.com");



                    //conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

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
    public static void pasingMusicFromSearch(String message,int count,int from)
    {

        JSONObject jsonObject =null;
        try {
            jsonObject = new JSONObject(message);
            for (int i=0;i<count;i++)
            {
                JSONObject object= jsonObject.getJSONArray("data").getJSONObject(i);
                Music mc=new Music();
                mc.type=2;
                mc.musicbmpUri=object.getString("pic");

                mc.artist=object.getString("singer");

                mc.title=object.getString("name");

                mc.musicid=object.getString("id");
                if(from==1)
                {
                    mc.path="https://api.bzqll.com/music/tencent/url?key=579621905&id="+mc.musicid;

                }
                if(from==2)
                {
                    mc.path="https://api.bzqll.com/music/netease/url?key=579621905&id="+mc.musicid;
                }
                mc.from=from;
                mc.Musicindex=i;
                searchlist.add(mc);
            }


        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public static void pasingMusicFromList(String message,int from)
    {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
                    int songnum=0;
                    if (from==1)
                    {
                        songnum =Integer.parseInt(jsonObject.getJSONObject("data").getString("songnum"));
                    }
                    else if(from==2||from==3)
                    {
                        songnum =Integer.parseInt(jsonObject.getJSONObject("data").getString("songListCount"));
                    }
                    for (int i=0;i<songnum;i++)
                    {
                        JSONObject object= jsonObject.getJSONObject("data").getJSONArray("songs").getJSONObject(i);
                        Music mc=new Music();
                        mc.type=2;
                        mc.path=object.getString("url");
                        mc.musicbmpUri=object.getString("pic");
                        mc.artist=object.getString("singer");
                        mc.title=object.getString("name");
                        mc.musicid=object.getString("id");
                        mc.from=from;
                        mc.Musicindex=i;
                        musiclist.add(mc);
                    }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void pasingLrc(String message){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
            //通过key（text）获取value
            String Lrc = jsonObject.getJSONArray("data").getJSONObject(0).getString("lrc");
             getLrctext=getResult(Lrc);
           // System.out.println("text:"+lrctext);

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
