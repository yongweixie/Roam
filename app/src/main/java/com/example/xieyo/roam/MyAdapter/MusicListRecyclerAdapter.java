package com.example.xieyo.roam.MyAdapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.musicbean.Music;

import java.util.List;


public class MusicListRecyclerAdapter extends BaseMultiItemQuickAdapter<Music, BaseViewHolder> {
    public MusicListRecyclerAdapter(@Nullable List<Music> data) {
        super(data);
        addItemType(1, R.layout.music_list_title_layout);
        addItemType(2, R.layout.music_list_layout_withindex);
        addItemType(3, R.layout.music_list_title_back_layout);

    }

    @Override
    protected void convert(BaseViewHolder helper, Music item) {
        //可链式调用赋值
        switch (helper.getItemViewType())
        {
            case 1:
                helper.addOnClickListener(R.id.viewmore);

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
                helper.setText(R.id.music_title, item.title)
                        .setText(R.id.music_artist_album, item.artist);
                if (helper.getView(R.id.onlinemusicindex)!=null)
                {
                    helper.setText(R.id.onlinemusicindex, item.Musicindex+1+"");
                }
                if (item.from==2)
                {
                    helper.setImageResource(R.id.icon_from,R.drawable.icon_netease);
                }
                if (item.from==1)
                {
                    helper.setImageResource(R.id.icon_from,R.drawable.icon_tencent);
                }
                if (item.from==3)
                {
                    helper.setImageResource(R.id.icon_from,R.drawable.icon_kugou);
                }
                break;
            case 3:
                helper.addOnClickListener(R.id.viewback);

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
        }



    }
}
