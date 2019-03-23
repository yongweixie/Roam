package com.example.xieyo.roam.musicfragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.baseinfo.MusicBaseInfo;
import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.MyAdapter.MusicHallAdapter;
import com.example.xieyo.roam.musicactivity.MusicListActivity;
import com.example.xieyo.roam.musicactivity.OnlineMusicActivity;
import com.example.xieyo.roam.tools.MusicApi;
import com.example.xieyo.roam.musicbean.MusicHallList;
import com.example.xieyo.roam.view.SpacesItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MusicHallFragment extends LazyFragment implements BaseQuickAdapter.OnItemClickListener {


    private RecyclerView ry;
    private static MusicHallAdapter mAdapter;
    private static List<MusicHallList> mList=new ArrayList<>();
   // private Handler handler = new Handler();

    public int setContentView() {
        return R.layout.musichall_fragment;
    }
    public void init() {


        ry = rootView.findViewById(R.id.ry_music_hall_list);
        mAdapter=new MusicHallAdapter(mList);
        mAdapter.setOnItemClickListener(this);


        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),6);

        ry.setLayoutManager(gridLayoutManager);
        ry.addItemDecoration(new SpacesItemDecoration(4));
        ry.setAdapter(mAdapter);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {//表示需要占据几个位置的span
                //因为在声明gridlayoutManager的时候进行了设置，so每一行2个span
                switch (mAdapter.getItemViewType(position)) {
                    case 1:
                        return 6;//占据两个位置的span
                    case 2:
                        return 2;
                    default:
                        return 2;//占据一个位置
                }
            }
        });

        gridLayoutManager.setInitialPrefetchItemCount(20);
        ry.setItemViewCacheSize(200);
        ry.setHasFixedSize(true);
        ry.setNestedScrollingEnabled(false);

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();

                for (int i=0;i<3;i++)
                {
                    MusicHallList titlelist=new MusicHallList();
                    titlelist.from=i+1;
                    titlelist.type=1;
                    Message msg = new Message();
                    mList.add(titlelist);
                    mList.addAll(MusicApi.getMusicList(1,6,i+1));
                    handler.sendMessage(msg);
                }

            }
        };
        new Thread(runnable).start();
    }

    private static final class Listndler extends Handler {
        WeakReference<MusicHallFragment> mMainActivityWeakReference;

        public Listndler(MusicHallFragment mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    }
    final Listndler handler=new Listndler(this);


    @Override
    public void lazyLoad() {

    }

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(adapter.getItemViewType(position)==1)
        {
            Intent intent=new Intent(getContext(), MusicListActivity.class);
            startActivity(intent);
            MusicBaseInfo.AllMusicListFrom=mList.get(position).from;
        }
        else
        {
            //String title = mList.get(position).title;
            // Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
            MusicBaseInfo.MusicListId=mList.get(position).id;
            MusicBaseInfo.MusicListImageUrl =mList.get(position).imageUri;
            MusicBaseInfo.CurrentMusicListFrom=mList.get(position).from;
            MusicBaseInfo.MusicListTitle=mList.get(position).title;
            MusicBaseInfo.MusicListPlayCount=mList.get(position).playCount;
            Intent intent = new Intent(getContext(), OnlineMusicActivity.class);
            getContext().startActivity(intent);
        }
    }

}