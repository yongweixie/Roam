package com.example.xieyo.roam;

import android.content.Intent;
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
import com.example.xieyo.roam.MyAdapter.BookDigestAdapter;
import com.example.xieyo.roam.baseinfo.BaseInfo;
import com.example.xieyo.roam.baseinfo.BookBaseInfo;
import com.example.xieyo.roam.bookactivity.BookDigestList;
import com.example.xieyo.roam.bookbean.BookDigestData;
import com.example.xieyo.roam.musicbean.FavList;
import com.example.xieyo.roam.musicbean.Music;
import com.example.xieyo.roam.tools.BmobApi;
import com.example.xieyo.roam.tools.BookApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class FavDigest extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemLongClickListener,ActionSheet.ActionSheetListener{
    private RecyclerView ry;
    private static BookDigestAdapter mAdapter;
    private static List<BookDigestData> bList=new ArrayList<>();
    private static AVLoadingIndicatorView avi;
    private static TextView headertext;
    private View mview ;
    private String  mtext ;
    private String extar_data;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_digest_list);
                Intent intent = getIntent();
        extar_data = intent.getStringExtra("extra_data");

        avi=findViewById(R.id.loadingview);
        avi.show();
        headertext=findViewById(R.id.header_text);
        headertext.setText("我的收藏");
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
        mAdapter.setOnItemLongClickListener(this);



        BmobQuery query = new BmobQuery("u" + BaseInfo.account);
        query.addWhereEqualTo("type", extar_data);
        query.setLimit(500);
        query.order("createdAt");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {

                    //Log.i("123456", "done: "+ary.toString());
                    try {
                        for (int i = 0; i < ary.length(); i++) {
                            BookDigestData bl = new BookDigestData();
                            String get = ary.getJSONObject(i).getString("data");
                            //  BmobApi.UpLoadData(bmc.artist+"@_@"+bmc.title+"@_@"+bmc.musicbmpUri+"@_@"+bmc.path+"@_@"+bmc.musicid+"@_@"+bmc.from,"music");
                            bl.content=get.split("@_@")[0];
                            bl.from=get.split("@_@")[1];
                            bList.add(bl);
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception ex) {

                        Log.i("123456", "done: "+ex.toString());

                    }

                } else {

                }
            }
        });
        avi.hide();
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
        mtext=bList.get(position).content;
        return false;
    }
    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

        if(index==0)
        {
            String path="/storage/emulated/0/Roam/Book/"+mtext.substring(0,5)+".png";
            saveBitmap(mview,path);
        }
        if(index==1)
        {
            BmobApi.UpLoadData(mtext,"zhaichao",getApplication());
            Toast.makeText(FavDigest.this, "收藏成功",Toast.LENGTH_LONG).show();
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
