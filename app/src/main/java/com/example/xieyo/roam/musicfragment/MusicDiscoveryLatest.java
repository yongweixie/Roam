package com.example.xieyo.roam.musicfragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.MyAdapter.MusicDiscoveryAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.searchfragment.SearchMusicAllFragment;
import com.example.xieyo.roam.tools.MusicApi;
import com.example.xieyo.roam.tools.Mv;
import com.example.xieyo.roam.tools.VideoApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

import static com.example.xieyo.roam.musicactivity.SearchActivity.getSearchViewtext;

public class MusicDiscoveryLatest extends LazyFragment implements BaseQuickAdapter.RequestLoadMoreListener{
    private RecyclerView ry;
    private static MusicDiscoveryAdapter mAdapter;
    private static List<Mv> mList=new ArrayList<>();
    private int page=1;
    public int setContentView() {
        return R.layout.musicdiscovery_latest;
    }


    public void init() {
        ry = rootView.findViewById(R.id.rv_discovery_latest);
        mAdapter=new MusicDiscoveryAdapter(mList);

        mAdapter.setOnLoadMoreListener(new MusicDiscoveryLatest(),ry);
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

                    case 2:
                        return 6;
                    default:
                        return 2;
                }
            }
        });

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();
                Message msg = new Message();

                mList.addAll(VideoApi.getQQMvlist(1,4,1)) ;
                //Log.i("latest", "getdata: ");
                handler.sendMessage(msg);

            }
        };
        new Thread(runnable).start();
    }
    @Override
    public void lazyLoad() {



    }
    private static final class MvHandler extends Handler {
        WeakReference<MusicDiscoveryLatest> mMainActivityWeakReference;

        public MvHandler(MusicDiscoveryLatest mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();

        }
    }
    final private MusicDiscoveryLatest.MvHandler handler=new MusicDiscoveryLatest.MvHandler(this);

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

    @Override
    public void onLoadMoreRequested() {

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();
                page++;
                Message msg = new Message();
                mList.addAll(VideoApi.getQQMvlist(page,4,1)) ;
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();

    }
}
