package com.example.xieyo.roam.MyAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;


public class ItemFragmentAdapter extends FragmentPagerAdapter {
    private String[] names;
    private List<Fragment> fragments;
    private Context context;

    public ItemFragmentAdapter(FragmentManager fm, String[] names, List<Fragment> fragments, Context context) {
        super(fm);
        this.names = names;
        this.fragments = fragments;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override

    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView(viewPages.get(position));
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return names[position];
    }
}