package com.example.xieyo.roam;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.LinearLayout;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jzvd.Jzvd;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity implements View.OnClickListener {


    TabLayout mTabLayout;
    ViewPager mViewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Bitmap head;// 头像Bitmap
    private static String path = "/storage/emulated/0/myHead/";// sd路径
    CircleImageView circleImage;
    List<Fragment> fragments;
    private MainAdapter adapter;
    private TextView quit;
    private LinearLayout moviefav, bookfav, bookdigest, moviedigest;
    ImageView btn_slidingmenu, btn_search;
    String[] titile = {"", "", ""};
    private int[] tabIcons = {
            R.drawable.selector_book,
            R.drawable.selector_music,
            R.drawable.selector_movie
    };
    Music mc = new Music();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        quit = findViewById(R.id.quit);
        quit.setOnClickListener(this);
        moviedigest = findViewById(R.id.moviedigest);
        moviedigest.setOnClickListener(this);

        bookdigest = findViewById(R.id.bookdigest);
        bookdigest.setOnClickListener(this);

        bookfav = findViewById(R.id.bookfav);
        bookfav.setOnClickListener(this);

        moviefav = findViewById(R.id.moviefav);
        moviefav.setOnClickListener(this);


        btn_slidingmenu = (ImageView) findViewById(R.id.btn_slidingmenu);
        btn_slidingmenu.setOnClickListener(this);
        btn_search = (ImageView) findViewById(R.id.main_search);
        btn_search.setOnClickListener(this);


        View headerView = navigationView.getHeaderView(0);//获取头布局
        circleImage = headerView.findViewById(R.id.useravatar);
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTypeDialog();
            }
        });
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            circleImage.setImageDrawable(drawable);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.logout:

                        DateBaseUtils dateBaseUtils = new DateBaseUtils(MainActivity.this);
                        DateBaseUtils.setLoginState(false);
                        Intent intent = new Intent(MainActivity.this, StartActivity.class);
                        startActivity(intent);

                        break;

                }
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
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
        switch (v.getId()) {
            case R.id.btn_slidingmenu://点击菜单，跳出侧滑菜单
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
                break;
            case R.id.main_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.quit:
                DateBaseUtils dateBaseUtils = new DateBaseUtils(this);
                DateBaseUtils.setLoginState(false);
                intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                break;

            case R.id.bookdigest:
                intent = new Intent(this, FavDigest.class);
                intent.putExtra("extra_data", "zhaichao");
                //启动Intent
                startActivity(intent);
                break;
            case R.id.moviedigest:
                intent = new Intent(this, FavDigest.class);
                intent.putExtra("extra_data", "taici");
                //启动Intent
                startActivity(intent);
                break;
            case R.id.moviefav:
                intent = new Intent(this, FavMusic_Movie.class);
                intent.putExtra("extra_data", "movie");
                //启动Intent
                startActivity(intent);
                break;
            case R.id.bookfav:
                intent = new Intent(this, FavMusic_Movie.class);
                intent.putExtra("extra_data", "book");
                //启动Intent
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
        if (!mBackKeyPressed) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {//退出程序
//        this.finish();
//        System.exit(0);
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }
    }

    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        circleImage.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }
}


