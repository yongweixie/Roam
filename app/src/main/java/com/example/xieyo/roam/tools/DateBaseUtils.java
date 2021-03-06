package com.example.xieyo.roam.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.xieyo.roam.bookbean.BookFragList;
import com.example.xieyo.roam.moviebean.MovieFragList;
import com.example.xieyo.roam.musicbean.Music;

import java.util.ArrayList;
import java.util.List;
public class DateBaseUtils {
    private static Context con;
    public  DateBaseUtils(Context context)
    {
        con=context;
    }
    public static void setLoginState(boolean state)
    {
        SharedPreferences sharedPreferences=con.getSharedPreferences("LoginState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();//获取编辑器
        if(state==true)
        {
            editor2.putString("state","yes");
        }
        if(state==false)
        {
            editor2.putString("state","no");
        }
        editor2.commit();//提交修改

    }

    public static void setReBook(String Link)//推荐的书
    {

        SharedPreferences sharedPreferences=con.getSharedPreferences("ReBook", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();//获取编辑器
        StringBuffer linkbuffer=new StringBuffer();
        StringBuffer coverbuffer=new StringBuffer();
        StringBuffer namebuffer=new StringBuffer();

        List<BookFragList> recommened=new ArrayList<>();
        recommened.addAll(BookApi.getotherlike(Link));

        for (int i=0;i<recommened.size();i++)
        {
            linkbuffer.append(recommened.get(i).booklink).append("@0@");
            coverbuffer.append(recommened.get(i).coveruri).append("@0@");
            namebuffer.append(recommened.get(i).name).append("@0@");

        }

        editor2.putString("linklist",sharedPreferences.getString("linklist","") +linkbuffer.toString());
        editor2.putString("coverlist",sharedPreferences.getString("coverlist","") +coverbuffer.toString());
        editor2.putString("namelist",sharedPreferences.getString("namelist","") +namebuffer.toString());
        editor2.commit();

    }
    public static void setReMovie(String Link)//推荐的书
    {
        SharedPreferences sharedPreferences=con.getSharedPreferences("ReMovie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();//获取编辑器
        StringBuffer linkbuffer=new StringBuffer();
        StringBuffer coverbuffer=new StringBuffer();
        StringBuffer namebuffer=new StringBuffer();
        List<MovieFragList> recommened=new ArrayList<>();//为了适配书的布局
        recommened.addAll(MovieApi.getotherlike(Link));

        for (int i=0;i<recommened.size();i++)
        {
            linkbuffer.append(recommened.get(i).movielink).append("@0@");
            coverbuffer.append(recommened.get(i).coveruri).append("@0@");
            namebuffer.append(recommened.get(i).name).append("@0@");

        }

        editor2.putString("linklist",sharedPreferences.getString("linklist","") +linkbuffer.toString());
        editor2.putString("coverlist",sharedPreferences.getString("coverlist","") +coverbuffer.toString());
        editor2.putString("namelist",sharedPreferences.getString("namelist","") +namebuffer.toString());
        editor2.commit();

    }
    public static List<BookFragList> getMovieRecommened()
    {
        List<BookFragList> lb=new ArrayList<>();

        SharedPreferences sharedPreferences=con.getSharedPreferences("ReMovie", Context.MODE_PRIVATE);
        String linklist=sharedPreferences.getString("linklist","");
        String coverlist=sharedPreferences.getString("coverlist","");
        String namelist=sharedPreferences.getString("namelist","");


        if (linklist.length()!=0)
        {
            for(int i=0;i<linklist.split("@0@").length;i++)
            {
                BookFragList bf =new BookFragList();
                bf.from=1;
                bf.type=2;
                bf.booklink=linklist.split("@0@")[i];
                bf.coveruri=coverlist.split("@0@")[i];
                bf.name=namelist.split("@0@")[i];
                lb.add(bf);
            }
        }

        return  lb;
    }

    public static List<BookFragList> getBookRecommened()
    {
        List<BookFragList> lb=new ArrayList<>();

        SharedPreferences sharedPreferences=con.getSharedPreferences("ReBook", Context.MODE_PRIVATE);
        String linklist=sharedPreferences.getString("linklist","");
        String coverlist=sharedPreferences.getString("coverlist","");
        String namelist=sharedPreferences.getString("namelist","");


        if (linklist.length()!=0)
        {
            for(int i=0;i<linklist.split("@0@").length;i++)
            {
                BookFragList bf =new BookFragList();
                bf.from=1;
                bf.type=2;
                bf.booklink=linklist.split("@0@")[i];
                bf.coveruri=coverlist.split("@0@")[i];
                bf.name=namelist.split("@0@")[i];

                lb.add(bf);
            }
        }

        return  lb;
    }


    public static void setUserInfo(String phone)
    {

        SharedPreferences sharedPreferences=con.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();//获取编辑器
        editor2.putString("phone",phone);
        editor2.commit();//提交修改


    }
    public static String getUserInfo()
    {

        SharedPreferences sharedPreferences=con.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String phone=sharedPreferences.getString("phone","");
        return phone;

    }
    public static boolean getLoginState()
    {
        boolean flag=false;
        SharedPreferences sharedPreferences=con.getSharedPreferences("LoginState", Context.MODE_PRIVATE);
        String state=sharedPreferences.getString("state","");
        if(state.equals("yes"))
        {
            flag=true;
        }
        if(state.equals("no"))
        {
            flag=false;
        }
        return  flag;
    }
    public static void SetMusicList(List<Music> list)
    {
        SharedPreferences sharedPreferences=con.getSharedPreferences("CurrentList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences.edit();//获取编辑器
        StringBuffer pathbuffer=new StringBuffer();
        StringBuffer musicbmpUribuffer=new StringBuffer();
        StringBuffer titlebuffer=new StringBuffer();
        StringBuffer artistbuffer=new StringBuffer();
        StringBuffer idbuffer=new StringBuffer();
        StringBuffer frombuffer=new StringBuffer();

        for (int i=0;i<list.size();i++)
        {
            pathbuffer.append(list.get(i).path).append("@0@");
            musicbmpUribuffer.append(list.get(i).musicbmpUri).append("@0@");
            titlebuffer.append(list.get(i).title).append("@0@");
            artistbuffer.append(list.get(i).artist).append("@0@");
            idbuffer.append(list.get(i).musicid).append("@0@");
            frombuffer.append(list.get(i).from).append("@0@");
        }
        //存储键值对

        editor2.putString("titlelist",titlebuffer.toString());
        editor2.putString("artistlist",artistbuffer.toString());
        editor2.putString("bmpurilist",musicbmpUribuffer.toString());
        editor2.putString("pathlist",pathbuffer.toString());
        editor2.putString("idlist",idbuffer.toString());
        editor2.putString("fromlist",frombuffer.toString());


        //提交
        editor2.commit();//提交修改
    }
    public static void setIndex(int index)
    {

        SharedPreferences sharedPreferences=con.getSharedPreferences("CurrentIndex", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putInt("index",index);
        editor.commit();

    }
    public static int getIndex()
    {
        SharedPreferences sharedPreferences=con.getSharedPreferences("CurrentIndex", Context.MODE_PRIVATE);
        int index=sharedPreferences.getInt("index",0);
        return index;
    }
    public static List<Music> getMusicList()

    {
        List<Music> mlist=new ArrayList<>();;
        SharedPreferences sharedPreferences = con.getSharedPreferences("CurrentList", Context.MODE_PRIVATE);
        String titlelist=sharedPreferences.getString("titlelist","");
        String bmpurllist=sharedPreferences.getString("bmpurilist","");
        String artistlist=sharedPreferences.getString("artistlist","");
        String pathlist=sharedPreferences.getString("pathlist","");
        String idlist=sharedPreferences.getString("idlist","");
        String fromlist=sharedPreferences.getString("fromlist","");

        if (titlelist.length()!=0)
        {
            for(int i=0;i<titlelist.split("@0@").length;i++)
            {
                Music mc =new Music();
                mc.title=titlelist.split("@0@")[i];
                mc.artist=artistlist.split("@0@")[i];
                mc.path=pathlist.split("@0@")[i];
                mc.musicbmpUri=bmpurllist.split("@0@")[i];
                mc.musicid=idlist.split("@0@")[i];
                if(fromlist.split("@0@")[i].length()==0)
                {
                    mc.from=1;
                }
                else

                mc.from=Integer.valueOf(fromlist.split("@0@")[i]) ;
                mlist.add(mc);
            }
        }
        return  mlist;
    }
}
