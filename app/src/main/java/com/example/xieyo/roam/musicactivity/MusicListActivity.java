package com.example.xieyo.roam.musicactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.BaseInfo;
import com.example.xieyo.roam.MyAdapter.MusicHallAdapter;
import com.example.xieyo.roam.MyAdapter.MusicListRecyclerAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.Service.PlayService;
import com.example.xieyo.roam.musicfragment.MusicHallFragment;
import com.example.xieyo.roam.searchfragment.SearchMusicAllFragment;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.tools.Music;
import com.example.xieyo.roam.tools.MusicApi;
import com.example.xieyo.roam.tools.MusicHallList;
import com.example.xieyo.roam.view.SpacesItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.example.xieyo.roam.musicactivity.SearchActivity.getSearchViewtext;

public class MusicListActivity extends BaseMusicActivity implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView ry;
    private static MusicHallAdapter mAdapter;
    private static List<MusicHallList> mList=new ArrayList<MusicHallList>();

    private final static MusicListActivity.Listndler handler=new MusicListActivity.Listndler(new MusicListActivity());

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist);
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

    public void Init() {


        ry = findViewById(R.id.ry_music_list);
        mList=MusicApi.getMusicList(1,6,BaseInfo.AllMusicListFrom);
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
                    mList.addAll(MusicApi.getMusicList(page,6,BaseInfo.AllMusicListFrom));
                    handler.sendMessage(msg);
            }

        };
        new Thread(runnable).start();

    }
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            BaseInfo.MusicListId=mList.get(position).id;
            BaseInfo.MusicListImageUrl =mList.get(position).imageUri;
            BaseInfo.CurrentMusicListFrom=mList.get(position).from;
            BaseInfo.MusicListTitle=mList.get(position).title;
            BaseInfo.MusicListPlayCount=mList.get(position).playCount;
            Intent intent = new Intent(this, OnlineMusicActivity.class);
            startActivity(intent);
        }
}
