package com.example.xieyo.roam.MyAdapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.bookbean.BookDigestData;


import java.util.List;

public class BookDigestAdapter extends BaseQuickAdapter<BookDigestData, BaseViewHolder> {

    public BookDigestAdapter(@LayoutRes int layoutResId, @Nullable List<BookDigestData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookDigestData item) {
        //可链式调用赋值
        helper.setText(R.id.content, item.content)
                .setText(R.id.from, item.from);
    }
}