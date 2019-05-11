package com.example.xieyo.roam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.MyAdapter.BookFragAdapter;
import com.example.xieyo.roam.baseinfo.MovieBaseInfo;
import com.example.xieyo.roam.bookbean.BookFragList;
import com.example.xieyo.roam.movieactivity.MovieDataActivity;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.view.SpacesItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class BookRecommenedActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {


    private RecyclerView ry;
    private static BookFragAdapter mAdapter;
    private static List<BookFragList> bList = new ArrayList<>();
    private static AVLoadingIndicatorView avi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_new_all);
        Intent intent = getIntent();

        avi = findViewById(R.id.loadingview);
        avi.show();
        LinearLayout backbutton = findViewById(R.id.back);
        TextView headertext = findViewById(R.id.header_text);

        headertext.setText("我的收藏");
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ry = findViewById(R.id.ry_book_new_all);
        DateBaseUtils dateBaseUtils=new DateBaseUtils(this);
        bList.addAll(DateBaseUtils.getBookRecommened());

        mAdapter = new BookFragAdapter(bList);
        mAdapter.setOnItemClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);

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


        avi.hide();

    }


    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        MovieBaseInfo.movielink = bList.get(position).booklink;
        Intent intent = new Intent(this, MovieDataActivity.class);
        startActivity(intent);
    }


    protected void onDestroy() {
        super.onDestroy();
        bList.clear();

    }

}