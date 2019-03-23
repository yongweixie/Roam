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
import com.example.xieyo.roam.launch.StartActivity;
import com.example.xieyo.roam.musicactivity.SearchActivity;
import com.example.xieyo.roam.musicbean.Music;
import com.example.xieyo.roam.tools.DateBaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.Jzvd;


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
                    case    R.id.logout:

                        DateBaseUtils dateBaseUtils=new DateBaseUtils(MainActivity.this);
                        DateBaseUtils.setLoginState(false);
                        Intent intent=new Intent(MainActivity.this, StartActivity.class);
                        startActivity(intent);

                        break;

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
        mViewPager.setOffscreenPageLimit(2);
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
            case R.id.quit:
                DateBaseUtils dateBaseUtils=new DateBaseUtils(this);
                DateBaseUtils.setLoginState(false);
                intent=new Intent(this, StartActivity.class);
                startActivity(intent);
                break;
        }
    }
//    public void onBackPressed() {
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
//
//
//    }
    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    @Override
    public void onBackPressed() {
        if(!mBackKeyPressed){
        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        mBackKeyPressed = true;
        new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
        @Override
        public void run() {
        mBackKeyPressed = false;
        }
        }, 2000);
        } else{//退出程序
//        this.finish();
//        System.exit(0);
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}


