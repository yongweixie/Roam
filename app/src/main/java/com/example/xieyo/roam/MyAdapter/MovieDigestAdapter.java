package com.example.xieyo.roam.MyAdapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.bookbean.BookDigestData;
import com.example.xieyo.roam.moviebean.MovieDigestData;

import java.util.List;

public class MovieDigestAdapter extends BaseQuickAdapter<MovieDigestData, BaseViewHolder> {

    public MovieDigestAdapter(@LayoutRes int layoutResId, @Nullable List<MovieDigestData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MovieDigestData item) {
        //可链式调用赋值
        helper.setText(R.id.content, item.content)
                .setText(R.id.from, item.from);

    }
}