package com.example.xieyo.roam.musicfragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.MyAdapter.MusicDiscoveryAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.searchfragment.SearchMusicAllFragment;
import com.example.xieyo.roam.tools.Mv;
import com.example.xieyo.roam.tools.VideoApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

public class MusicDiscoveryRank extends LazyFragment {
    private RecyclerView ry;
    private static MusicDiscoveryAdapter mAdapter;
    private static List<Mv> mList=new ArrayList<>();
    public int setContentView() {
        return R.layout.musicdiscovery_rank;
    }


    public void init() {
        ry = rootView.findViewById(R.id.rv_discovery_rank);
        mAdapter=new MusicDiscoveryAdapter(mList);
        mAdapter.openLoadAnimation();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),6);

        ry.setLayoutManager(gridLayoutManager);
        ry.addItemDecoration(new SpacesItemDecoration(10));
        ry.setAdapter(mAdapter);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {//表示需要占据几个位置的span
                //因为在声明gridlayoutManager的时候进行了设置，so每一行2个span
                switch (mAdapter.getItemViewType(position)) {
                    case 1:
                        return 6;//占据两个位置的span
                    case 2:
                        return 6;
                    case 3:
                        return 6;
                    default:
                        return 2;
                }
            }
        });


    }
    @Override
    public void lazyLoad() {

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();
                for(int i=0;i<3;i++)
                {
                    Message msg = new Message();
                    Mv mmv=new Mv();
                    mmv.titletype=i;
                    mmv.type=1;
                    mList.add(mmv);
                    mList.addAll(VideoApi.getEchoMvlist(i)) ;
                    handler.sendMessage(msg);
                }

            }
        };
        new Thread(runnable).start();

    }
    private static final class MvHandler extends Handler {
        WeakReference<MusicDiscoveryRank> mMainActivityWeakReference;

        public MvHandler(MusicDiscoveryRank mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    }
    final private MusicDiscoveryRank.MvHandler handler=new MusicDiscoveryRank.MvHandler(this);

    @Override
    public void onResume() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK){
                    if (Jzvd.backPress()) {
                    }
                }
                return false;
            }
        });
        super.onResume();
    }
}
