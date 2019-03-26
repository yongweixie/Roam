package com.example.xieyo.roam.movieactivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import com.example.xieyo.roam.baseinfo.MovieBaseInfo;
import com.example.xieyo.roam.MyAdapter.MovieDigestAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.bookactivity.BookDigestList;
import com.example.xieyo.roam.moviebean.MovieDigestData;
import com.example.xieyo.roam.tools.BmobApi;
import com.example.xieyo.roam.tools.MovieApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MovieDigestList extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.RequestLoadMoreListener,BaseQuickAdapter.OnItemLongClickListener,ActionSheet.ActionSheetListener{
    private RecyclerView ry;
    private static MovieDigestAdapter mAdapter;
    private static List<MovieDigestData> bList=new ArrayList<>();
    private static AVLoadingIndicatorView avi;
    private static  int page =0;
    private static TextView headertext;
    private View mview ;
    private String  mtext ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_digest_list);
        avi=findViewById(R.id.loadingview);
        avi.show();
        headertext=findViewById(R.id.header_text);
        headertext.setText("全部台词");
        LinearLayout backbutton = findViewById(R.id.back);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ry = findViewById(R.id.ry_list_digest);
        mAdapter=new MovieDigestAdapter(R.layout.book_digest_item,bList);
        ry.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        ry.addItemDecoration(new SpacesItemDecoration(10));
        ry.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new MovieDigestList(),ry);
        mAdapter.setOnItemLongClickListener(this);
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                Message msg = new Message();
                bList.addAll(MovieApi.getDigestList(MovieBaseInfo.movielink,page));
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();


    }

    private static final class Listndler extends Handler {
        WeakReference<MovieDigestList> mMainActivityWeakReference;

        public Listndler(MovieDigestList mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            avi.hide();
            mAdapter.notifyDataSetChanged();
        }
    }
    final static MovieDigestList.Listndler handler=new MovieDigestList.Listndler(new MovieDigestList());

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
    final static MovieDigestList.Listndler2 handler2=new MovieDigestList.Listndler2(new BookDigestList());

    @Override
    public void onLoadMoreRequested() {

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();
                page++;
                Message msg = new Message();
                List<MovieDigestData> getList=MovieApi.getDigestList(MovieBaseInfo.movielink,page);
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
        mtext=bList.get(position).content+"@_@"+bList.get(position).from;
        return false;
    }
    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

        if(index==0)
        {
            String path="/storage/emulated/0/Roam/Movie/"+mtext.substring(0,5)+".png";
            saveBitmap(mview,path);
        }
        if (index==1)
        {
            BmobApi.UpLoadData(mtext,"taici");
            Toast.makeText(MovieDigestList.this, "收藏成功",Toast.LENGTH_LONG).show();
        }
    }
    public void saveBitmap(View view, String filePath) {

        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas((bitmap));
        view.draw(canvas);

        //存储
        FileOutputStream outStream = null;
        File Folder = new File("/storage/emulated/0/Roam/Movie/","newFolder");
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
        //其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    file.getAbsolutePath(), filePath, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 通知图库更新
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            mediaScanIntent.setData(uri);
                            sendBroadcast(mediaScanIntent);
                        }
                    });
        } else {
            String relationDir = file.getParent();
            File file1 = new File(relationDir);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file1.getAbsoluteFile())));
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
        page=0;
    }
}
