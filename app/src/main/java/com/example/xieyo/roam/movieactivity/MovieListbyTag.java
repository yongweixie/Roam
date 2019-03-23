package com.example.xieyo.roam.movieactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.baseinfo.MovieBaseInfo;
import com.example.xieyo.roam.MyAdapter.MovieFragAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.moviebean.MovieFragList;
import com.example.xieyo.roam.tools.MovieApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MovieListbyTag extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.RequestLoadMoreListener{


    private RecyclerView ry;
    private static MovieFragAdapter mAdapter;
    private static List<MovieFragList> bList=new ArrayList<>();
    private static  int page =0;
    private static TextView headertext;
    private static AVLoadingIndicatorView avi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_bytag);
        avi=findViewById(R.id.loadingview);
        avi.show();
        LinearLayout backbutton = findViewById(R.id.back);
        bList.clear();
        headertext=findViewById(R.id.header_text);
        headertext.setText(MovieBaseInfo.findtag);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ry = findViewById(R.id.ry_list_tag);
        mAdapter=new MovieFragAdapter(bList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(new MovieListbyTag(),ry);
        mAdapter.openLoadAnimation();

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,6);

        ry.setLayoutManager(gridLayoutManager);
        ry.addItemDecoration(new SpacesItemDecoration(10));
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

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                Message msg = new Message();
                if(MovieBaseInfo.findtag.equals("豆瓣 Top250"))
                {
                    bList.addAll(MovieApi.getListbyTag("top250?type=S",0));
                }
                else
                {
                    bList.addAll(MovieApi.getListbyTag(MovieBaseInfo.findtag,0));

                }
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();


    }

    private static final class Listndler extends Handler {
        WeakReference<MovieListbyTag> mMainActivityWeakReference;

        public Listndler(MovieListbyTag mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            avi.hide();
            mAdapter.notifyDataSetChanged();
        }
    }
    final static MovieListbyTag.Listndler handler=new MovieListbyTag.Listndler(new MovieListbyTag());

    private static final class Listndler2 extends Handler {
        WeakReference<MovieListbyTag> mMainActivityWeakReference;

        public Listndler2(MovieListbyTag mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
        }
    }
    final static MovieListbyTag.Listndler2 handler2=new MovieListbyTag.Listndler2(new MovieListbyTag());

    @Override
    public void onLoadMoreRequested() {

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();
                page++;
                Message msg = new Message();
                bList.addAll(MovieApi.getListbyTag(MovieBaseInfo.findtag,page));
                handler2.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //Toast.makeText(getActivity(), bList.get(position).booklink, Toast.LENGTH_LONG).show();
        MovieBaseInfo.movielink=bList.get(position).movielink;
        Intent intent = new Intent(this, MovieDataActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bList.clear();
    }
}
