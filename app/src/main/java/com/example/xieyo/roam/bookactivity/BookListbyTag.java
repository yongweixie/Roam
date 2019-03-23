package com.example.xieyo.roam.bookactivity;

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
import com.example.xieyo.roam.baseinfo.BookBaseInfo;
import com.example.xieyo.roam.MyAdapter.BookFragAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.bookbean.BookFragList;
import com.example.xieyo.roam.tools.BookApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BookListbyTag extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.RequestLoadMoreListener{


    private RecyclerView ry;
    private static BookFragAdapter mAdapter;
    private static List<BookFragList> bList=new ArrayList<>();
    private static  int page =0;
    private static TextView headertext;
    private static AVLoadingIndicatorView avi;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_bytag);
        avi=findViewById(R.id.loadingview);
        avi.show();
        LinearLayout backbutton = findViewById(R.id.back);
        bList.clear();
        headertext=findViewById(R.id.header_text);
        headertext.setText(BookBaseInfo.findtag);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ry = findViewById(R.id.ry_list_tag);
        mAdapter=new BookFragAdapter(bList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnLoadMoreListener(new BookListbyTag(),ry);
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
                bList.addAll(BookApi.getListbyTag(BookBaseInfo.findtag,0));
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();


    }

    private static final class Listndler extends Handler {
        WeakReference<BookListbyTag> mMainActivityWeakReference;

        public Listndler(BookListbyTag mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            avi.hide();
            mAdapter.notifyDataSetChanged();
        }
    }
    final static BookListbyTag.Listndler handler=new BookListbyTag.Listndler(new BookListbyTag());

    private static final class Listndler2 extends Handler {
        WeakReference<BookListbyTag> mMainActivityWeakReference;

        public Listndler2(BookListbyTag mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
        }
    }
    final static BookListbyTag.Listndler2 handler2=new BookListbyTag.Listndler2(new BookListbyTag());

    @Override
    public void onLoadMoreRequested() {

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();
                page++;
                Message msg = new Message();
                bList.addAll(BookApi.getListbyTag(BookBaseInfo.findtag,page));
                handler2.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //Toast.makeText(getActivity(), bList.get(position).booklink, Toast.LENGTH_LONG).show();
        BookBaseInfo.booklink=bList.get(position).booklink;
        Intent intent = new Intent(this, BookDataActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bList.clear();
    }
}
