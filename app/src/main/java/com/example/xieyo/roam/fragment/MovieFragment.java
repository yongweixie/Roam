package com.example.xieyo.roam.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.baseinfo.MovieBaseInfo;
import com.example.xieyo.roam.MyAdapter.MovieFragAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.movieactivity.BillboardActivity;
import com.example.xieyo.roam.movieactivity.FindMovieEntrance;
import com.example.xieyo.roam.movieactivity.MovieDataActivity;
import com.example.xieyo.roam.movieactivity.MovieDigest;
import com.example.xieyo.roam.moviebean.MovieFragList;
import com.example.xieyo.roam.tools.MovieApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MovieFragment extends LazyFragment implements BaseQuickAdapter.OnItemClickListener , View.OnClickListener {
    private RecyclerView ry;
    private static MovieFragAdapter mAdapter;
    private static List<MovieFragList> bList = new ArrayList<>();

    public int setContentView() {
        return R.layout.frag_movie;
    }

    public void init() {


        ry = rootView.findViewById(R.id.ry_movie_frag_list);
        ry.setNestedScrollingEnabled(false);
        mAdapter = new MovieFragAdapter(bList);
        mAdapter.setOnItemClickListener(this);
        View headerView = getLayoutInflater().inflate(R.layout.movie_frag_header, null);
        mAdapter.addHeaderView(headerView);

        LinearLayout movie_find = headerView.findViewById(R.id.movie_find);
        LinearLayout movie_billboard = headerView.findViewById(R.id.movie_billboard);
        LinearLayout movie_list = headerView.findViewById(R.id.movie_list);
        LinearLayout movie_recommened = headerView.findViewById(R.id.movie_recommened);
        movie_recommened.setOnClickListener(this);
        movie_list.setOnClickListener(this);
        movie_billboard.setOnClickListener(this);
        movie_find.setOnClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 6);

        ry.setLayoutManager(gridLayoutManager);
        ry.addItemDecoration(new SpacesItemDecoration(20));
        ry.setAdapter(mAdapter);
        ry.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.e(TAG,"scrollStateChanged");


                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getContext()).resumeRequests();
                } else {
                    Glide.with(getContext()).pauseRequests();
                }
            }
        });
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

        gridLayoutManager.setInitialPrefetchItemCount(20);
        ry.setItemViewCacheSize(200);
        ry.setHasFixedSize(true);
        ry.setNestedScrollingEnabled(false);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();
                MovieFragList titlelist = new MovieFragList();
                titlelist.from = 1;
                titlelist.type = 1;
                Message msg = new Message();
                bList.add(titlelist);
                bList.addAll(MovieApi.getNowPlaying(6));
                // handler.sendMessage(msg);


                titlelist = new MovieFragList();
                titlelist.from = 2;
                titlelist.type = 1;
                // msg = new Message();
                bList.add(titlelist);
                bList.addAll(MovieApi.getHotMovie(1, 6));


                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }

    private static final class Listndler extends Handler {
        WeakReference<MovieFragment> mMainActivityWeakReference;

        public Listndler(MovieFragment mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
        }
    }

    final MovieFragment.Listndler handler = new MovieFragment.Listndler(this);


    @Override
    public void lazyLoad() {

    }

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(bList.get(position).type!=1)
        {
            //Toast.makeText(getActivity(), bList.get(position).booklink, Toast.LENGTH_LONG).show();
            MovieBaseInfo.movielink=bList.get(position).movielink;
            Intent intent = new Intent(getContext(), MovieDataActivity.class);
            startActivity(intent);
        }

    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.movie_find:
               Intent intent = new Intent(getContext(), FindMovieEntrance.class);

                startActivity(intent);
                break;
            case R.id.movie_billboard:
                startActivity(new Intent(getContext(), BillboardActivity.class));
                break;
            case R.id.movie_list:
                startActivity(new Intent(getContext(), MovieDigest.class));
                break;
            default:

                break;

        }
    }
}