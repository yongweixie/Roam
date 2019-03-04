package com.example.xieyo.roam.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xieyo.roam.R;

public class MyMusicItemLayout extends LinearLayout {
    private ImageView mIcon, mPlay;
    private TextView mName;
    private TextView mDesc;
    private Context mContext;

    public MyMusicItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }
    public MyMusicItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }
    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyMusicItemLayout);
        String name = ta.getString(R.styleable.MyMusicItemLayout_musicItem_name);
        String desc = ta.getString(R.styleable.MyMusicItemLayout_musicItem_desc);
        Drawable icon = ta.getDrawable(R.styleable.MyMusicItemLayout_musicItem_icon);
        ColorStateList mDrawableTintList = ta.getColorStateList(R.styleable.MyMusicItemLayout_musicItem_icon_color);
        ta.recycle();

        mContext = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(R.layout.mymusic_item_layout, this, true);

        mIcon = mView.findViewById(R.id.musicitem_icon);
        mName = mView.findViewById(R.id.musicitem_name);
        mDesc = mView.findViewById(R.id.musicitem_desc);

        mName.setText(name);
        mDesc.setText(desc);
        mIcon.setImageDrawable(icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIcon.setImageTintList(mDrawableTintList);
            mIcon.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
        }


    }
}
