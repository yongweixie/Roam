package com.example.xieyo.roam.bookactivity;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.baseinfo.BookBaseInfo;
import com.example.xieyo.roam.MyAdapter.BookFragAdapter;
import com.example.xieyo.roam.MyAdapter.RatingviewAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.bookbean.BookData;
import com.example.xieyo.roam.bookbean.BookFragList;
import com.example.xieyo.roam.tools.BmobApi;
import com.example.xieyo.roam.tools.BookApi;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.tools.ratingview;
import com.example.xieyo.roam.view.SpacesItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;
import com.willy.ratingbar.BaseRatingBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class BookDataActivity extends BaseActivity  implements BaseQuickAdapter.OnItemClickListener{

    private RecyclerView ry, ry2;
    private static RatingviewAdapter mAdapter;
    private static BookFragAdapter mAdapter2;
    private static List<ratingview> list = new ArrayList<>();
    private static List<BookFragList> blist = new ArrayList<>();
    private static TextView data1, data2, data3, numraters, averagerating,book_name,book_auther,book_pubdate,book_translator,book_page;
    private static BaseRatingBar ratingBar;
    private static ImageView cover;
    private static AVLoadingIndicatorView avi;
    private static NestedScrollView nestedScrollView;
    static BookData mbookdata = new BookData();
    private static TextView nostar;
    private static String nostartext = "";
    private static Context con;
    private static String bmobtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_data);
        LinearLayout backbutton = findViewById(R.id.back);
        TextView headertext=findViewById(R.id.header_text);

        headertext.setText("图书");
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView fav=findViewById(R.id.fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobApi.UpLoadData(bmobtext,"book",getApplication());
                Toast.makeText(BookDataActivity.this, "收藏成功",Toast.LENGTH_LONG).show();

            }
        });

        nestedScrollView =findViewById(R.id.bookdata_scroll_view);
        nestedScrollView.setVisibility(View.GONE);
        avi = findViewById(R.id.loadingview);
        avi.show();
        con=getApplicationContext();
        Initry();
        data1 = findViewById(R.id.data_intro);
        data2 = findViewById(R.id.data_artistintro);
        data3 = findViewById(R.id.data_catlog);
        cover = findViewById(R.id.book_cover);
        numraters = findViewById(R.id.numraters);
        averagerating = findViewById(R.id.averagerating);
        ratingBar = findViewById(R.id.RatingBar);
        nostar = findViewById(R.id.nostar);
        book_name=findViewById(R.id.book_name);
        book_auther=findViewById(R.id.book_auther);
        book_pubdate=findViewById(R.id.book_pubdate);
        book_page=findViewById(R.id.book_page);
        book_translator=findViewById(R.id.book_translator);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO: http request.
                Message msg = new Message();

                mbookdata = BookApi.getBookData(BookBaseInfo.booklink);
                // Log.i("123456", "run: "+mbookdata.introduce);
                blist.addAll(BookApi.getotherlike(BookBaseInfo.booklink));
                List<String> ls = new ArrayList<>();
                //  Log.i("123456", "run: "+mbookdata.star.size());
                ls.addAll(mbookdata.star);
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
//        for (int i=0;i<5;i++)
//        {
//            ratingview rt=new ratingview();
//            rt.process=10+i*20;
//            rt.star=i+"星";
//            list.add(rt);
//        }
        // 填充数据
        mAdapter = new RatingviewAdapter(R.layout.ratingview, list);
        ry.setAdapter(mAdapter);

        ry2 = findViewById(R.id.ry_bookdata);
        ry2.setLayoutManager(new GridLayoutManager(this, 3));
        ry2.addItemDecoration(new SpacesItemDecoration(10));
        mAdapter2 = new BookFragAdapter(blist);
        mAdapter2.setOnItemClickListener(this);

        ry2.setAdapter(mAdapter2);

        ry2.setItemViewCacheSize(100);
        ry2.setHasFixedSize(true);
        ry2.setNestedScrollingEnabled(false);

    }

    private static class Listndler extends Handler {
        WeakReference<BookDataActivity> mMainActivityWeakReference;

        public Listndler(BookDataActivity mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            avi.hide();
            nestedScrollView.setVisibility(View.VISIBLE);
            data1.setText(mbookdata.introduce);
            data2.setText(mbookdata.artistintroduce);
            data3.setText(mbookdata.catalog);
            averagerating.setText(mbookdata.averagerating);
            numraters.setText(mbookdata.numraters + "人评价");
            // ScaleRatingBar ratingBar = new ScaleRatingBar(mContext);
            //Log.i("123456", "handleMessage: "+mbookdata.averagerating);
            ratingBar.setRating(Float.valueOf(mbookdata.averagerating) / 2);
            RequestOptions options = new RequestOptions()
                    .optionalTransform(new RoundedCornersTransformation(5, 0, RoundedCornersTransformation.CornerType.ALL))
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(con).load(mbookdata.coveruri)
                    .apply(options)
                    .into(cover);
            nostar.setText(nostartext);
            book_name.setText(mbookdata.bookname);
            book_auther.setText("作者："+mbookdata.bookauther);
            book_page.setText("页数："+mbookdata.page);
            book_translator.setText("译者："+mbookdata.translator);
            book_pubdate.setText("出版年："+mbookdata.pubdate);
            bmobtext=mbookdata.bookname+"@_@"+mbookdata.coveruri+"@_@"+BookBaseInfo.booklink;
            mAdapter.notifyDataSetChanged();
            mAdapter2.notifyDataSetChanged();
        }
    }

    private static BookDataActivity.Listndler handler = new BookDataActivity.Listndler(new BookDataActivity());
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //Toast.makeText(getActivity(), bList.get(position).booklink, Toast.LENGTH_LONG).show();
        finish();
        BookBaseInfo.booklink=blist.get(position).booklink;
        Intent intent = new Intent(this, BookDataActivity.class);
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
