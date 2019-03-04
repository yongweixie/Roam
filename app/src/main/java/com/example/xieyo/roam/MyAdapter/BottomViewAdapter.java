package com.example.xieyo.roam.MyAdapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.tools.Music;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class BottomViewAdapter extends BaseQuickAdapter<Music, BaseViewHolder> {

    public BottomViewAdapter(@LayoutRes int layoutResId, @Nullable List<Music> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Music item) {
        //可链式调用赋值
        helper.setText(R.id.tv_bottom_play_bar_title, item.title)
                .setText(R.id.tv_bottom_play_bar_artist, item.artist);


            ImageView logoview = helper.getView(R.id.iv_play_bar_cover);

            RequestOptions options = new RequestOptions().placeholder(R.drawable.default_cover)
                    .optionalTransform(new RoundedCornersTransformation(5,0, RoundedCornersTransformation.CornerType.ALL))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(mContext).load(item.musicbmpUri)
                    .apply(options)
                    .into(logoview);

        //获取当前条目position
        //int position = helper.getLayoutPosition();
    }
}