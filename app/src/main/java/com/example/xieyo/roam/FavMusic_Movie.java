package com.example.xieyo.roam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.MyAdapter.BookFragAdapter;
import com.example.xieyo.roam.baseinfo.BaseInfo;
import com.example.xieyo.roam.baseinfo.BookBaseInfo;
import com.example.xieyo.roam.bookactivity.BookDataActivity;
import com.example.xieyo.roam.bookactivity.NewBookAll;
import com.example.xieyo.roam.bookbean.BookDigestData;
import com.example.xieyo.roam.bookbean.BookFragList;
import com.example.xieyo.roam.tools.BookApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class FavMusic_Movie extends BaseActivity implements BaseQuickAdapter.OnItemClickListener{


    private RecyclerView ry;
    private static BookFragAdapter mAdapter;
    private static List<BookFragList> bList=new ArrayList<>();
    private static AVLoadingIndicatorView avi;
    private String extar_data;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_new_all);
        Intent intent = getIntent();
        extar_data = intent.getStringExtra("extra_data");

        avi=findViewById(R.id.loadingview);
        avi.show();
        LinearLayout backbutton = findViewById(R.id.back);
        TextView headertext=findViewById(R.id.header_text);

        headertext.setText("我的收藏");
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ry = findViewById(R.id.ry_book_new_all);
        mAdapter=new BookFragAdapter(bList);
        mAdapter.setOnItemClickListener(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,6);

        ry.setLayoutManager(gridLayoutManager);
        ry.addItemDecoration(new SpacesItemDecoration(20));
        ry.setAdapter(mAdapter);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {//表示需要占据几个位置的span
                //因为在声明gridlayoutManager的时候进行了设置，so每一行2个span
                switch (mAdapter.getItemViewType(position)) {
                    case 1:
                        return 6;
                    case 2:
                        return 2;
                    case 3:
                        return 6;
                    default:
                        return 6;//占据一个位置
                }
            }
        });



        BmobQuery query = new BmobQuery("u" + BaseInfo.account);
        query.addWhereEqualTo("type", extar_data);
        query.setLimit(500);
        query.order("createdAt");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {

                    //Log.i("123456", "done: "+ary.toString());
                    try {
                        for (int i = 0; i < ary.length(); i++) {
                            BookFragList bl = new BookFragList();
                            String get = ary.getJSONObject(i).getString("data");
                            //  BmobApi.UpLoadData(bmc.artist+"@_@"+bmc.title+"@_@"+bmc.musicbmpUri+"@_@"+bmc.path+"@_@"+bmc.musicid+"@_@"+bmc.from,"music");
                            bl.type=2;
                            bl.name=get.split("@_@")[0];
                            bl.coveruri=get.split("@_@")[1];
                            bl.from=1;
                            bl.booklink=get.split("@_@")[2];
                            bList.add(bl);
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception ex) {

                        Log.i("123456", "done: "+ex.toString());

                    }

                } else {

                }
            }
        });
        avi.hide();

    }



    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        BookBaseInfo.booklink=bList.get(position).booklink;
        Intent intent = new Intent(this, BookDataActivity.class);
        startActivity(intent);
    }


    protected void onDestroy() {
        super.onDestroy();
        bList.clear();

    }

}
