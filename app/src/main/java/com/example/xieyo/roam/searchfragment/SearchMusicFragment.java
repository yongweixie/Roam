package com.example.xieyo.roam.searchfragment;

import android.support.v4.app.Fragment;

import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.MyAdapter.ItemFragmentAdapter;
import com.example.xieyo.roam.MyAdapter.MainAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class SearchMusicFragment extends LazyFragment {

    private static NoScrollViewPager mViewPager;
    List<Fragment> fragments;
    private MainAdapter adapter;
    String[] titile = {"", "", ""};

    public int setContentView() {
        return R.layout.frag_search_music;
    }

    public void init() {

        mViewPager=rootView.findViewById(R.id.search_music_viewpager);
        //mViewPager.setScanScroll(false);
        fragments = new ArrayList<>();
        fragments.add(new SearchMusicPartFragment());
        fragments.add(new SearchMusicAllFragment());
        adapter = new MainAdapter(getActivity().getSupportFragmentManager(), fragments, titile);
        mViewPager.setAdapter(adapter);
    }
    public void lazyLoad() {

    }
    public static void setPage(int index)
    {
        mViewPager.setCurrentItem(index,false);
    }
    public static int getPage()
    {
        return mViewPager.getCurrentItem();
    }

}
