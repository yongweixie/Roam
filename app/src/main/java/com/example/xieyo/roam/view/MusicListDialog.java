package com.example.xieyo.roam.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.xieyo.roam.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MusicListDialog extends Dialog implements View.OnClickListener{
    //在构造方法里提前加载了样式
    private static Context context;//上下文
    private int layoutResID;//布局文件id
    private int[] listenedItem;//监听的控件id
    private static TextView tv_title;
    private static TextView tv_Desc;
    private static ImageView iv_cover;

    public MusicListDialog(Context context,int layoutResID,int[] listenedItem){
        super(context, R.style.MyDialog);//加载dialog的样式
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItem = listenedItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //提前设置Dialog的一些样式
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(layoutResID);
        tv_title=findViewById(R.id.list_title);
        tv_Desc=findViewById(R.id.musiclistdesc);
        iv_cover=findViewById(R.id.list_cover);

        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*4/5;// 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);//点击外部Dialog消失
        //遍历控件id添加点击注册
        for(int id:listenedItem){
            findViewById(id).setOnClickListener(this);
        }
    }
    public static void setData(String title,String Url,String Desc)
    {
        tv_title.setText(title);
        tv_Desc.setText(Desc);

        RequestOptions options1 = new RequestOptions().placeholder(R.drawable.default_cover)
                .optionalTransform(new RoundedCornersTransformation(15,0, RoundedCornersTransformation.CornerType.ALL))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(context).load(Url).apply(options1)
                .into(iv_cover);


    }
    private OnCenterItemClickListener listener;
    public interface OnCenterItemClickListener {
        void OnCenterItemClick(MusicListDialog dialog, View view);
    }
    //很明显我们要在这里面写个接口，然后添加一个方法
    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        dismiss();//注意：我在这里加了这句话，表示只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
        listener.OnCenterItemClick(this,v);
    }
}
