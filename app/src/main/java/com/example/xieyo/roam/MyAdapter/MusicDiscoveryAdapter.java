package com.example.xieyo.roam.MyAdapter;

import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.musicbean.Mv;

import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class MusicDiscoveryAdapter extends BaseMultiItemQuickAdapter<Mv, BaseViewHolder> {

        public MusicDiscoveryAdapter(@Nullable List<Mv> data) {
        super(data);
            addItemType(1, R.layout.musicdiscovery_title);

            addItemType(2, R.layout.musicdiscovery_mv_layout);
        }
    @Override
    protected void convert(BaseViewHolder helper, Mv item) {

        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
        JzvdStd jzvdStd = helper.getView(R.id.mv_player);

        switch (helper.getItemViewType())
        {
            case 1:
                if (item.titletype==0)
                {
                    helper.setText(R.id.tv_content, "MV日榜");
                }
                if (item.titletype==1)
                {
                    helper.setText(R.id.tv_content, "MV周榜");
                }
                if (item.titletype==2)
                {
                    helper.setText(R.id.tv_content, "MV月榜");
                }
                break;
            case 2:
                //可链式调用赋值
                helper.setText(R.id.mv_title, item.title)
                        .setText(R.id.mv_artist,item.artist);
                jzvdStd.setUp(item.Uri
                        , "" , Jzvd.SCREEN_WINDOW_NORMAL);
                Glide.with(mContext).load(item.coveruri)
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .into(jzvdStd.thumbImageView);
                break;
        }


    }
}