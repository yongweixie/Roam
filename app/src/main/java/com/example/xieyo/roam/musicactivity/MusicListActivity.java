package com.example.xieyo.roam.musicactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.baseinfo.MusicBaseInfo;
import com.example.xieyo.roam.MyAdapter.MusicHallAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.tools.MusicApi;
import com.example.xieyo.roam.musicbean.MusicHallList;
import com.example.xieyo.roam.view.SpacesItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends BaseMusicActivity implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView ry;
    private static MusicHallAdapter mAdapter;
    private static List<MusicHallList> mList=new ArrayList<>();

    private final static MusicListActivity.Listndler handler=new MusicListActivity.Listndler(new MusicListActivity());
    private final static MusicListActivity.Listndler2 handler2=new MusicListActivity.Listndler2(new MusicListActivity());
    private static AVLoadingIndicatorView avi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist);
        avi = findViewById(R.id.loadingview);
        avi.show();
        LinearLayout backbutton = findViewById(R.id.back);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Init();
    }
    int page=1;
    private static final class Listndler extends Handler {
        WeakReference<MusicListActivity> mMainActivityWeakReference;

        public Listndler(MusicListActivity mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();

        }
    }
    private static final class Listndler2 extends Handler {
        WeakReference<MusicListActivity> mMainActivityWeakReference;

        public Listndler2(MusicListActivity mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            avi.hide();
            mAdapter.notifyDataSetChanged();
        }
    }
    public void Init() {


        ry = findViewById(R.id.ry_music_list);

        mAdapter=new MusicHallAdapter(mList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(this,ry);
        //mAdapter.disableLoadMoreIfNotFullPage();


        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,6);

        ry.setLayoutManager(gridLayoutManager);
        ry.addItemDecoration(new SpacesItemDecoration(4));
        ry.setAdapter(mAdapter);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {//表示需要占据几个位置的span
                //因为在声明gridlayoutManager的时候进行了设置，so每一行2个span
                switch (mAdapter.getItemViewType(position)) {
                    case 1:
                        return 6;
                    case 2:
                        return 3;
                    default:
                        return 3;//占据一个位置
                }
            }
        });

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                page++;

                // TODO: http request.
                //Bundle data = new Bundle();
                Message msg = new Message();
                mList.addAll(MusicApi.getMusicList(1,6, MusicBaseInfo.AllMusicListFrom));
                handler2.sendMessage(msg);
            }

        };
        new Thread(runnable).start();
    }

    @Override
    public void onLoadMoreRequested() {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                page++;

                // TODO: http request.
                //Bundle data = new Bundle();
                    Message msg = new Message();
                    mList.addAll(MusicApi.getMusicList(page,6, MusicBaseInfo.AllMusicListFrom));
                    handler.sendMessage(msg);
            }

        };
        new Thread(runnable).start();

    }
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            MusicBaseInfo.MusicListId=mList.get(position).id;
            MusicBaseInfo.MusicListImageUrl =mList.get(position).imageUri;
            MusicBaseInfo.CurrentMusicListFrom=mList.get(position).from;
            MusicBaseInfo.MusicListTitle=mList.get(position).title;
            MusicBaseInfo.MusicListPlayCount=mList.get(position).playCount;
            Intent intent = new Intent(this, OnlineMusicActivity.class);
            startActivity(intent);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList.clear();
    }
}
