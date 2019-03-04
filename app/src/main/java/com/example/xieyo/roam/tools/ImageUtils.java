package com.example.xieyo.roam.tools;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import com.example.xieyo.roam.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    private static Context con;
    public  ImageUtils(Context context)
    {
        con=context;
    }
    public static Drawable getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = con.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        if (album_art == null || album_art.equals("")) {
            Bitmap bm = BitmapFactory.decodeResource(con.getResources(), R.drawable.default_cover);
            BitmapDrawable bmpDraw = new BitmapDrawable(bm);
            return bmpDraw;
        }
        Bitmap bm = BitmapFactory.decodeFile(album_art);
        BitmapDrawable bmpDraw = new BitmapDrawable(bm);
        return bmpDraw;
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File("");
        file.mkdirs();// 创建文件夹
        String fileName = "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
// 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void blurByRender(Context context, Bitmap bitmap, View view, float radius) {
        // 得到要处理的区域
        Bitmap dstArea = getDstArea(bitmap, view);
        dstArea = zoomImage(dstArea, 0.8f);

        // 作模糊处理
        RenderScript rs = RenderScript.create(context);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, dstArea);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(dstArea);

        // 设置背景
        view.setBackground(new BitmapDrawable(context.getResources(), dstArea));

        bitmap.recycle();
        rs.destroy();
    }


    private static Bitmap getDstArea(Bitmap bitmap, View view) {
        Bitmap dstArea = Bitmap.createBitmap((int) (view.getMeasuredWidth()), (int) (view.getMeasuredHeight()),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstArea);
        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bitmap, 0, 0, null);
        return dstArea;
    }


    private static Bitmap zoomImage(Bitmap srcBitmap, float scale) {
        // 获取这个图片的宽和高
        float width = srcBitmap.getWidth();
        float height = srcBitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scale, scale);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

}
