package com.example.xieyo.roam.bookactivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.baseinfo.BookBaseInfo;
import com.example.xieyo.roam.baseinfo.MovieBaseInfo;
import com.example.xieyo.roam.MyAdapter.BookDigestAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.tools.BookApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;
import com.example.xieyo.roam.bookbean.BookDigestData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BookDigestList extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.RequestLoadMoreListener,BaseQuickAdapter.OnItemLongClickListener,ActionSheet.ActionSheetListener{
    private RecyclerView ry;
    private static BookDigestAdapter mAdapter;
    private static List<BookDigestData> bList=new ArrayList<>();
    private static AVLoadingIndicatorView avi;
    private static  int page =0;
    private static TextView headertext;
    private View  mview ;
    private String  mtext ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_digest_list);
        avi=findViewById(R.id.loadingview);
        avi.show();
        headertext=findViewById(R.id.header_text);
        headertext.setText("全部书摘");
        LinearLayout backbutton = findViewById(R.id.back);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ry = findViewById(R.id.ry_list_digest);
        mAdapter=new BookDigestAdapter(R.layout.book_digest_item,bList);
        ry.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        ry.addItemDecoration(new SpacesItemDecoration(10));
        ry.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new BookDigestList(),ry);
        mAdapter.setOnItemLongClickListener(this);
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                Message msg = new Message();
                bList.addAll(BookApi.getDigestList(BookBaseInfo.booklink,page));

                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();


    }

    private static final class Listndler extends Handler {
        WeakReference<BookDigestList> mMainActivityWeakReference;

        public Listndler(BookDigestList mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            avi.hide();
            mAdapter.notifyDataSetChanged();
        }
    }
    final static BookDigestList.Listndler handler=new BookDigestList.Listndler(new BookDigestList());

    private static final class Listndler2 extends Handler {
        WeakReference<BookDigestList> mMainActivityWeakReference;

        public Listndler2(BookDigestList mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
            if(msg.arg1==-1)
            {
                mAdapter.loadMoreEnd();
            }
        }
    }
    final static BookDigestList.Listndler2 handler2=new BookDigestList.Listndler2(new BookDigestList());

    @Override
    public void onLoadMoreRequested() {

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();
                page++;
                Message msg = new Message();
                //bList.addAll(BookApi.getDigestList(BookBaseInfo.booklink,page));
                List<BookDigestData> getList= BookApi.getDigestList(MovieBaseInfo.movielink,page);
                bList.addAll(getList);
                if (getList.size()<10)
                    msg.arg1=-1;
                handler2.sendMessage(msg);
            }
        };
        new Thread(runnable).start();
    }

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //Toast.makeText(getActivity(), bList.get(position).booklink, Toast.LENGTH_LONG).show();

    }
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position)
    {
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("保存到手机", "收藏本句")
                .setCancelableOnTouchOutside(true)
                .setListener(this).show();
        mview=view;
        mtext=bList.get(position).content.substring(0,5);
        return false;
    }
    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

        if(index==0)
        {
            String path="/storage/emulated/0/Roam/Book/"+mtext+".png";

            saveBitmap(mview,path);
        }
    }
    public void saveBitmap(View view, String filePath) {

        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        //存储
        FileOutputStream outStream = null;
        File Folder = new File("/storage/emulated/0/Roam/Book/","newFolder");
        if(!Folder.exists())
        {
            Folder.mkdirs();
        }
        File file = new File(filePath);
        if (file.isDirectory()) {//如果是目录不允许保存
            Toast.makeText(this, "该路径为目录路径", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            Toast.makeText(this, "图片保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage() + "#");
            Toast.makeText(this, "图片保存失败", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                bitmap.recycle();
                if (outStream != null) {
                    outStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
       // Toast.makeText(getApplicationContext(), "dismissed isCancle = " + isCancle, 0).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bList.clear();
    }
}
