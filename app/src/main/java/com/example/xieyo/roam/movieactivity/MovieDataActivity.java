package com.example.xieyo.roam.movieactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.baseinfo.MovieBaseInfo;
import com.example.xieyo.roam.MyAdapter.MovieFragAdapter;
import com.example.xieyo.roam.MyAdapter.RatingviewAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.moviebean.MovieData;
import com.example.xieyo.roam.moviebean.MovieFragList;
import com.example.xieyo.roam.tools.MovieApi;
import com.example.xieyo.roam.tools.ratingview;
import com.example.xieyo.roam.view.SpacesItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;
import com.willy.ratingbar.BaseRatingBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDataActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener{
    private static NestedScrollView nestedScrollView;
    private static AVLoadingIndicatorView avi;
    private static TextView  name,auther,cast,pubdate,longtime,introduce,numraters,averagerating;
    private static Context con;
    private RecyclerView ry, ry2;
    private static RatingviewAdapter mAdapter;
    private static MovieFragAdapter mAdapter2;
    private static List<ratingview> list = new ArrayList<>();
    private static List<MovieFragList> blist = new ArrayList<>();
    static MovieData moviedata = new MovieData();
    private static String nostartext = "";
    private static TextView nostar;
    private static BaseRatingBar ratingBar;
    private static ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_data);
        LinearLayout backbutton = findViewById(R.id.back);
        TextView headertext=findViewById(R.id.header_text);

        headertext.setText("电影");
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nestedScrollView =findViewById(R.id.bookdata_scroll_view);
        nestedScrollView.setVisibility(View.GONE);
        avi = findViewById(R.id.loadingview);
        avi.show();
        con=getApplicationContext();
        Initry();
        name=findViewById(R.id.movie_name);
        cast=findViewById(R.id.movie_cast);
        pubdate=findViewById(R.id.movie_pubdate);
        auther=findViewById(R.id.movie_auther);
        longtime=findViewById(R.id.movie_longtime);
        introduce=findViewById(R.id.data_intro);
        numraters=findViewById(R.id.numraters);
        averagerating=findViewById(R.id.averagerating);
        nostar=findViewById(R.id.nostar);
        ratingBar = findViewById(R.id.RatingBar);
        cover = findViewById(R.id.movie_cover);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO: http request.
                Message msg = new Message();

                moviedata = MovieApi.getMovieData(MovieBaseInfo.movielink);
                // Log.i("123456", "run: "+mbookdata.introduce);
              blist.addAll(MovieApi.getotherlike(MovieBaseInfo.movielink));
                List<String> ls = new ArrayList<>();
                //  Log.i("123456", "run: "+mbookdata.star.size());
                ls.addAll(moviedata.star);
                if (ls.size() == 5) {
                    for (int i = 4; i >= 0; i--) {
                        ratingview rv = new ratingview();
                        rv.star = i + 1 + "星";
                        rv.percent = ls.get(4 - i);
                        rv.process = Float.valueOf(ls.get(4 - i).split("%")[0]);
                        list.add(rv);
                    }
                } else {
                    nostartext = "评分人数不足";
                }

                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }

    public void Initry() {
        ry = (RecyclerView) findViewById(R.id.ry_ratingview);
        ry.setLayoutManager(new LinearLayoutManager(this));
        ry.addItemDecoration(new SpacesItemDecoration(0));

        mAdapter = new RatingviewAdapter(R.layout.ratingview, list);
        ry.setAdapter(mAdapter);

        ry2 = findViewById(R.id.ry_bookdata);
        ry2.setLayoutManager(new GridLayoutManager(this, 3));
        ry2.addItemDecoration(new SpacesItemDecoration(10));
        mAdapter2 = new MovieFragAdapter(blist);
        mAdapter2.setOnItemClickListener(this);

        ry2.setAdapter(mAdapter2);

        ry2.setItemViewCacheSize(100);
        ry2.setHasFixedSize(true);
        ry2.setNestedScrollingEnabled(false);

    }
    private static class Listndler extends Handler {
        WeakReference<MovieDataActivity> mMainActivityWeakReference;

        public Listndler(MovieDataActivity mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            avi.hide();
            nestedScrollView.setVisibility(View.VISIBLE);
            introduce.setText(moviedata.introduce);
            averagerating.setText(moviedata.averagerating);
            numraters.setText(moviedata.numraters + "人评价");
            // ScaleRatingBar ratingBar = new ScaleRatingBar(mContext);
            //Log.i("123456", "handleMessage: "+mbookdata.averagerating);
            ratingBar.setRating(Float.valueOf(moviedata.averagerating) / 2);
            RequestOptions options = new RequestOptions()
                    .optionalTransform(new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL))
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(con).load(moviedata.coveruri)
                    .apply(options)
                    .into(cover);
            nostar.setText(nostartext);
            name.setText(moviedata.moviename);
            auther.setText("导演："+moviedata.auther);
            longtime.setText("时长："+moviedata.longtime);
            cast.setText("主演："+moviedata.cast);
            pubdate.setText("出版年："+moviedata.pubdate);
            mAdapter.notifyDataSetChanged();
            mAdapter2.notifyDataSetChanged();
        }
    }

    private static MovieDataActivity.Listndler handler = new MovieDataActivity.Listndler(new MovieDataActivity());

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //Toast.makeText(getActivity(), bList.get(position).booklink, Toast.LENGTH_LONG).show();
        finish();
        MovieBaseInfo.movielink=blist.get(position).movielink;
        Intent intent = new Intent(this, MovieDataActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
        blist.clear();
        nostartext = "";
    }

}
