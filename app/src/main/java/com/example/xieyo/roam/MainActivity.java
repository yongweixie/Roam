package com.example.xieyo.roam;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xieyo.roam.fragment.BookFragment;
import com.example.xieyo.roam.MyAdapter.MainAdapter;
import com.example.xieyo.roam.fragment.MovieFragment;
import com.example.xieyo.roam.fragment.MusicFragment;
import com.example.xieyo.roam.musicactivity.SearchActivity;
import com.example.xieyo.roam.tools.Music;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener{


    TabLayout mTabLayout;
    ViewPager mViewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    List<Fragment> fragments;
    private MainAdapter adapter;
    ImageView btn_slidingmenu, btn_search;
    String[] titile = {"", "", ""};
    private int[] tabIcons = {
            R.drawable.selector_book,
            R.drawable.selector_music,
            R.drawable.selector_movie
    };
    Music mc =new Music();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);



        btn_slidingmenu= (ImageView)findViewById(R.id.btn_slidingmenu);
        btn_slidingmenu.setOnClickListener(this);
        btn_search=(ImageView)findViewById(R.id.main_search);
        btn_search.setOnClickListener(this);


        View headerView = navigationView.getHeaderView(0);//获取头布局
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch(item.getItemId()) {
                    //case    R.id.

                }
                Toast.makeText(MainActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });


        fragments = new ArrayList<>();
       fragments.add(new BookFragment());
        fragments.add(new MusicFragment());
        fragments.add(new MovieFragment());
        adapter = new MainAdapter(getSupportFragmentManager(), fragments, titile);

        mViewPager.setAdapter(adapter);
        //mTabLayout.setBackgroundResource(R.drawable.actionbar_book_selected);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);//设置当前页
        setupTabIcons();


    }


    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setCustomView(getTabView(0));
        mTabLayout.getTabAt(1).setCustomView(getTabView(1));
        mTabLayout.getTabAt(2).setCustomView(getTabView(2));

    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_icon_view, null);
        TextView txt_title = (TextView) view.findViewById(R.id.tabtext);
        txt_title.setText(titile[position]);
        ImageView img_title = (ImageView) view.findViewById(R.id.tabicon);
        img_title.setImageResource(tabIcons[position]);
        return view;
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_slidingmenu://点击菜单，跳出侧滑菜单
                if (drawerLayout.isDrawerOpen(navigationView)){
                    drawerLayout.closeDrawer(navigationView);
                }else{
                    drawerLayout.openDrawer(navigationView);
                }
                break;
            case R.id.main_search:
                Intent intent=new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
        }
    }
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }


}


