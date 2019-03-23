package com.example.xieyo.roam.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class RectImageView extends android.support.v7.widget.AppCompatImageView {

    public RectImageView(Context context) {
        this(context, null);
    }

    public RectImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setScaleType(ScaleType.FIT_XY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);


        int halfWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width / 31 * 48, widthMode);
        super.onMeasure(widthMeasureSpec, halfWidthMeasureSpec);
    }
}