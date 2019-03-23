package com.example.xieyo.roam.MyAdapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.musicbean.Music;
import com.example.xieyo.roam.tools.ratingview;

import java.util.List;


public class RatingviewAdapter extends BaseQuickAdapter<ratingview, BaseViewHolder> {

    public RatingviewAdapter(@LayoutRes int layoutResId, @Nullable List<ratingview> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ratingview item) {
        //可链式调用赋值
        helper.setText(R.id.ratingstar, item.star).setText(R.id.percent,item.percent);
        ProgressBar progressBar=helper.getView(R.id.process_star);
        progressBar.setProgress(item.process.intValue());

    }
}