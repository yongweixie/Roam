package com.example.xieyo.roam.MyAdapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.tools.MusicHallList;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MusicHallAdapter extends BaseMultiItemQuickAdapter<MusicHallList, BaseViewHolder> {
    public MusicHallAdapter(@Nullable List<MusicHallList> data) {
        super( data);

        addItemType(1, R.layout.musichall_title);
        addItemType(2, R.layout.musichall_list);

    }

    @Override
    protected void convert(BaseViewHolder helper, MusicHallList item) {
        switch (helper.getItemViewType())
        {
            case 1:
                if (item.from==2)
                {
                    helper.setBackgroundRes(R.id.colorview,R.color.red);
                    helper.setText(R.id.tv_content,"网易云音乐");


                }
                if(item.from==1)
                {
                    helper.setBackgroundRes(R.id.colorview,R.color.green);
                    helper.setText(R.id.tv_content,"QQ音乐");
                }
                if(item.from==3)
                {
                    helper.setBackgroundRes(R.id.colorview,R.color.blue);
                    helper.setText(R.id.tv_content,"酷狗音乐");
                }
                break;
            case 2:
                helper.setText(R.id.tv_playCount, item.playCount)
                        .setText(R.id.tv_content, item.title);
                ImageView coverview = helper.getView(R.id.iv_musiclist_cover);

                RequestOptions options = new RequestOptions().placeholder(R.drawable.default_cover)
                        .optionalTransform(new RoundedCornersTransformation(15,0, RoundedCornersTransformation.CornerType.ALL))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                Glide.with(mContext).load(item.imageUri)
                        .apply(options)
                        .into(coverview);
                break;
        }

    }

}
