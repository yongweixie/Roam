package com.example.xieyo.roam.tools;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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

public class VideoApi {



    public static List<Mv>  getQQMvlist(int page,int count,int order)
    {
        List<Mv> mvlist = new ArrayList<>();

        String url="https://u.y.qq.com/cgi-bin/musicu.fcg?-=mvlib44147684059762704&g_tk=1430096653&loginUin=164383783&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0" +
                "&data=%7B%22comm%22%3A%7B%22ct%22%3A24%7D%2C%22mv_tag%22%3A%7B%22module%22%3A%22MvService.MvInfoProServer%22%2C%22method%22%3A%22GetAllocTag%22%2C%22param%22%3A%7B%7D%7D%2C%22mv_list%22%3A%7B%22module%22%3A%22MvService.MvInfoProServer%22%2C%22method%22%3A%22GetAllocMvInfo%22%2C%22param%22%3A%7B%22" +
                "start%22%3A"+((page-1)*count)+"%2C%22size%22%3A"+count+"%2C%22version_id%22%3A7%2C%22" +
                "area_id%22%3A15%2C%22order%22%3A"+order+"%7D%7D%7D";

//
//    String url2="https://u.y.qq.com/cgi-bin/musicu.fcg?-=mvlib44147684059762704&g_tk=1430096653&loginUin=164383783&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0" +
//            "&data=%7B%22comm%22%3A%7B%22ct%22%3A24%7D%2C%22mv_tag%22%3A%7B%22module%22%3A%22MvService.MvInfoProServer%22%2C%22method%22%3A%22GetAllocTag%22%2C%22param%22%3A%7B%7D%7D%2C%22mv_list%22%3A%7B%22module%22%3A%22MvService.MvInfoProServer%22%2C%22method%22%3A%22GetAllocMvInfo%22%2C%22param%22%3A%7B%22" +
//            "start%22%3A"+((page-1)*count)+"size%2C%22size%22%3A"+count+"%2C%22version_id%22%3A7%2C%22" +
//            "area_id%22%3A15%2C%22order%22%3A+"+order+"%7D%7D%7D";
        //System.out.println(parseQQMusicMvList(url));

        String result=parseQQMusicMvList(url);

        try{
            JSONArray jsonArray=new JSONObject(result).getJSONObject("mv_list").getJSONObject("data").getJSONArray("list");
            for (int i=0;i<4;i++)
            {
                Mv mv=new Mv();
                mv.Uri="https://api.bzqll.com/music/tencent/mvUrl?key=579621905&id="+jsonArray.getJSONObject(i).getString("vid");
                mv.coveruri=jsonArray.getJSONObject(i).getString("picurl");
                mv.artist=jsonArray.getJSONObject(i).getJSONArray("singers").getJSONObject(0).getString("name");
                mv.title=jsonArray.getJSONObject(i).getString("title");
                mv.type=2;
               // System.out.println(jsonArray.getJSONObject(i).getString("title"));
                mvlist.add(mv);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return mvlist;

    }
    public static List<Mv>  getEchoMvlist(int type)
    {
        List<Mv> mvlist = new ArrayList<>();
        String s_type="";
        if(type==0)
        {
            s_type="daily";
        }
        if(type==1)
        {
            s_type="weekly";
        }
        if(type==2)
        {
            s_type="monthly";
        }
        String url="http://www.app-echo.com/api/rank/mv-hot?periods="+s_type+"&limit=10";
        String result=parseQQMusicMvList(url);

        try{
            JSONArray jsonArray=new JSONObject(result).getJSONObject("lists").getJSONArray(s_type);
            for (int i=0;i<10;i++)
            {
                Mv mv=new Mv();
                mv.Uri=jsonArray.getJSONObject(i).getJSONArray("source_list").getJSONObject(0).getString("source").replaceAll("\\\\","");
                mv.coveruri=jsonArray.getJSONObject(i).getString("cover_url").replaceAll("\\\\","");
                mv.artist=jsonArray.getJSONObject(i).getJSONObject("user").getString("name");
                mv.title=jsonArray.getJSONObject(i).getString("name");
                mv.type=2;
                // System.out.println(jsonArray.getJSONObject(i).getString("title"));
                mvlist.add(mv);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

        return mvlist;

    }
    public static String parseechoMv(String Url){
        final StringBuilder sb = new StringBuilder();
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                BufferedReader br = null;
                InputStreamReader isr = null;

                try {
                    URL geturl = new URL(Url);
                    HttpURLConnection conn = (HttpURLConnection )geturl.openConnection();//创建连接
                    conn.setRequestMethod("GET");

                    conn.setRequestProperty("HOST","www.app-echo.com");

                    conn.connect();//get连接

                    InputStreamReader is = new InputStreamReader(conn.getInputStream());//输入流
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
    public static String parseQQMusicMvList(String Url){
        final StringBuilder sb = new StringBuilder();
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                BufferedReader br = null;
                InputStreamReader isr = null;

                try {




                    URL geturl = new URL(Url);
                    HttpURLConnection conn = (HttpURLConnection )geturl.openConnection();//创建连接
                    conn.setRequestMethod("GET");

                    conn.setRequestProperty("Referer","https://y.qq.com/portal/mv_lib.html");

                    conn.setRequestProperty("accept","application/json, text/plain, */*");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36");

                    conn.setRequestProperty("Origin", "https://y.qq.com");



                    conn.connect();//get连接

                    InputStreamReader is = new InputStreamReader(conn.getInputStream());//输入流
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

