package com.example.xieyo.roam.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xieyo.roam.R;


public class LocalMusicListLayout extends LinearLayout {
    private ImageView malbumbmp,mmusic_more;
    private TextView mtitle;
    private TextView martist_album;
    private Context mContext;

    public LocalMusicListLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }
    public LocalMusicListLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }
    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LocalMusicListLayout);
        String title = ta.getString(R.styleable.LocalMusicListLayout_music_title);
        String artist_album = ta.getString(R.styleable.LocalMusicListLayout_music_albumbmp);
        //Drawable bmp = ta.getDrawable(R.styleable.LocalMusicListLayout_music_albumbmp);
        ta.recycle();

        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(R.layout.music_list_layout, this, true);

        //malbumbmp = mView.findViewById(R.id.music_albumbmp);
        mtitle = mView.findViewById(R.id.music_title);
        martist_album = mView.findViewById(R.id.music_artist_album);
        mmusic_more= mView.findViewById(R.id.music_more);
        mtitle.setText(title);
        martist_album.setText(artist_album);
       // malbumbmp.setImageDrawable(bmp);

    }
}