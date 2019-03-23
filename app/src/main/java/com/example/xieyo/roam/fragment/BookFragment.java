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
import com.example.xieyo.roam.baseinfo.BookBaseInfo;
import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.MyAdapter.BookFragAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.bookactivity.BillboardActivity;
import com.example.xieyo.roam.bookactivity.BookDataActivity;
import com.example.xieyo.roam.bookactivity.BookDigest;
import com.example.xieyo.roam.bookactivity.ConcernedBookAll;
import com.example.xieyo.roam.bookactivity.Douban250All;
import com.example.xieyo.roam.bookactivity.FindBookEntrance;
import com.example.xieyo.roam.bookactivity.NewBookAll;
import com.example.xieyo.roam.bookbean.BookFragList;
import com.example.xieyo.roam.tools.BookApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BookFragment extends LazyFragment implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    private RecyclerView ry;
    private static BookFragAdapter mAdapter;
    private static List<BookFragList> bList = new ArrayList<>();

    public int setContentView() {
        return R.layout.frag_book;
    }

    public void init() {


        ry = rootView.findViewById(R.id.ry_book_frag_list);
        mAdapter = new BookFragAdapter(bList);
        mAdapter.setOnItemClickListener(this);
        View headerView = getLayoutInflater().inflate(R.layout.book_frag_header, null);
        mAdapter.addHeaderView(headerView);
        LinearLayout findbook = headerView.findViewById(R.id.find_book);
        LinearLayout book_billboard = headerView.findViewById(R.id.book_billboard);
        LinearLayout book_list = headerView.findViewById(R.id.book_list);
        LinearLayout book_recommended = headerView.findViewById(R.id.book_recommended);
        book_recommended.setOnClickListener(this);
        book_list.setOnClickListener(this);
        book_billboard.setOnClickListener(this);
        findbook.setOnClickListener(this);

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
                BookFragList titlelist = new BookFragList();
                titlelist.from = 1;
                titlelist.type = 1;
                Message msg = new Message();
                bList.add(titlelist);
                bList.addAll(BookApi.getnewBook(6));
                handler.sendMessage(msg);

                msg = new Message();
                titlelist = new BookFragList();
                titlelist.from = 2;
                titlelist.type = 1;
                bList.add(titlelist);
                bList.addAll(BookApi.getMostconcerned(3));
                handler.sendMessage(msg);

                msg = new Message();
                titlelist = new BookFragList();
                titlelist.from = 3;
                titlelist.type = 1;
                bList.add(titlelist);
                bList.addAll(BookApi.getTop250(6));
                handler.sendMessage(msg);

                msg = new Message();
                titlelist = new BookFragList();
                titlelist.from = 4;
                titlelist.type = 1;
                bList.add(titlelist);
                bList.addAll(BookApi.getBestSelling(1));
                handler.sendMessage(msg);

                msg = new Message();
                titlelist = new BookFragList();
                titlelist.from = 5;
                titlelist.type = 1;
                bList.add(titlelist);
                bList.addAll(BookApi.getBestSelling(2));
                handler.sendMessage(msg);

            }
        };
        new Thread(runnable).start();
    }

    private static final class Listndler extends Handler {
        WeakReference<BookFragment> mMainActivityWeakReference;

        public Listndler(BookFragment mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();

        }
    }

    final BookFragment.Listndler handler = new BookFragment.Listndler(this);


    @Override
    public void lazyLoad() {

    }

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (bList.get(position).from == 1 && bList.get(position).type == 1) {
            Intent intent = new Intent(getContext(), NewBookAll.class);
            startActivity(intent);
        }

        if (bList.get(position).from == 2 && bList.get(position).type == 1) {
            Intent intent = new Intent(getContext(), ConcernedBookAll.class);
            startActivity(intent);
        }
        if (bList.get(position).from == 3 && bList.get(position).type == 1) {
            Intent intent = new Intent(getContext(), Douban250All.class);
            startActivity(intent);
        }

        if(bList.get(position).type!=1)
        {
            //Toast.makeText(getActivity(), bList.get(position).booklink, Toast.LENGTH_LONG).show();
            BookBaseInfo.booklink=bList.get(position).booklink;
            Intent intent = new Intent(getContext(), BookDataActivity.class);
            startActivity(intent);
        }
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.find_book:
                Intent intent = new Intent(getContext(), FindBookEntrance.class);
                startActivity(intent);
                break;
            case R.id.book_billboard:
                startActivity(new Intent(getContext(), BillboardActivity.class));
                break;
            case R.id.book_list:
                startActivity(new Intent(getContext(), BookDigest.class));
                break;
            default:

                break;

        }
    }
}