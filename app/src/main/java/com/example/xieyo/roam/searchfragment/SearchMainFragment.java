package com.example.xieyo.roam.searchfragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.MyAdapter.ItemFragmentAdapter;
import com.example.xieyo.roam.R;

import java.util.ArrayList;
import java.util.List;

public class SearchMainFragment  extends LazyFragment {

    private static TextView tv_data;
    private ViewPager viewpager;
    private List<Fragment> mFragments;
    private ItemFragmentAdapter imAdapter;
    private TabLayout tablayout;
    private String[] names = {"音乐", "电影","图书","视频"};


    public int setContentView() {
        return R.layout.frag_search_main;
    }

    public void init() {

        viewpager = rootView.findViewById(R.id.searchviewpager);
        tablayout = rootView.findViewById(R.id.searchtablayout);

        mFragments = new ArrayList<>();
        mFragments.add(new SearchMusicFragment());
        mFragments.add(new SearchMovieFragment());
        mFragments.add(new SearchBookFragment());
        mFragments.add(new SearchVideoFragment());


        imAdapter = new ItemFragmentAdapter(getChildFragmentManager(), names, mFragments, getContext());
        viewpager.setAdapter(imAdapter);
        tablayout.setupWithViewPager(viewpager);
    }
    @Override
    public void lazyLoad() {

    }
    public void StartSearch(String keyword)
    {

    }

}