package com.example.xieyo.roam.musicfragment;


import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.MyAdapter.MainAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;


public class MusicDiscoveryFragment  extends LazyFragment  {

    private static NoScrollViewPager mViewPager;
    List<Fragment> fragments;
    private MainAdapter adapter;
    String[] titile = {"", "", ""};
    TextView tv_recommend;
    TextView tv_rank;
    TextView tv_latest;
    public int setContentView() {
        return R.layout.musicdiscovery_fragment;
    }

    public void init() {


         tv_recommend=rootView.findViewById(R.id.tv_recommend);
         tv_rank=rootView.findViewById(R.id.tv_rank);
         tv_latest=rootView.findViewById(R.id.tv_latest);

         tv_latest.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 setSelect(0);
             }
         });
        tv_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect(2);

            }
        });
        tv_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect(1);

            }
        });
        mViewPager=rootView.findViewById(R.id.mv_music_viewpager);
        mViewPager.setScanScroll(false);
        //mViewPager.setScanScroll(false);
        fragments = new ArrayList<>();
        fragments.add(new MusicDiscoveryLatest());
        fragments.add(new MusicDiscoveryRecommend());
        fragments.add(new MusicDiscoveryRank());

        //fragments.add(new SearchMusicAllFragment());
        adapter = new MainAdapter(getActivity().getSupportFragmentManager(), fragments, titile);

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        setSelect(0);
    }
    public void lazyLoad() {

    }
    public static void setPage(int index)
    {
        mViewPager.setCurrentItem(index,true);
    }
    public static int getPage()
    {
        return mViewPager.getCurrentItem();
    }
    public void setSelect(int index)
    {
        TextPaint tp ,tp2,tp3;
        switch (index)
        {
            case 0:
                setPage(0);
                tv_recommend.setTextColor(ContextCompat.getColor(getContext(), R.color.alpha_70_white));
                 tp = tv_recommend.getPaint();
                tp.setFakeBoldText(false);
                tv_rank.setTextColor(ContextCompat.getColor(getContext(), R.color.alpha_70_white));
                 tp2 = tv_rank.getPaint();
                tp2.setFakeBoldText(false);
                tv_latest.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                 tp3 = tv_latest.getPaint();
                tp3.setFakeBoldText(true);

                break;
            case 1:
                setPage(1);
                tv_recommend.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                 tp = tv_recommend.getPaint();
                tp.setFakeBoldText(true);
                tv_rank.setTextColor(ContextCompat.getColor(getContext(), R.color.alpha_70_white));
                 tp2 = tv_rank.getPaint();
                tp2.setFakeBoldText(false);
                tv_latest.setTextColor(ContextCompat.getColor(getContext(), R.color.alpha_70_white));
                 tp3 = tv_latest.getPaint();
                tp3.setFakeBoldText(false);
                break;
            case 2:
                setPage(2);
                tv_recommend.setTextColor(ContextCompat.getColor(getContext(), R.color.alpha_70_white));
                tp = tv_recommend.getPaint();
                tp.setFakeBoldText(false);
                tv_rank.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                tp2 = tv_rank.getPaint();
                tp2.setFakeBoldText(true);
                tv_latest.setTextColor(ContextCompat.getColor(getContext(), R.color.alpha_70_white));
                tp3 = tv_latest.getPaint();
                tp3.setFakeBoldText(false);
                break;

        }
    }

}