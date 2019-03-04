package com.example.xieyo.roam.musicactivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.BaseInfo;
import com.example.xieyo.roam.MyAdapter.BottomViewAdapter;
import com.example.xieyo.roam.MyAdapter.MainAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.Service.PlayService;
import com.example.xieyo.roam.searchfragment.SearchEntranceFragment;
import com.example.xieyo.roam.searchfragment.SearchMainFragment;
import com.example.xieyo.roam.searchfragment.SearchMusicAllFragment;
import com.example.xieyo.roam.searchfragment.SearchMusicFragment;
import com.example.xieyo.roam.searchfragment.SearchMusicPartFragment;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.tools.Music;
import com.example.xieyo.roam.view.NoScrollViewPager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener,BaseQuickAdapter.OnItemClickListener,View.OnClickListener{
    private static SearchView mSearchView;
    private static Activity act;
    private static NoScrollViewPager mViewPager;
    List<Fragment> fragments;
    private MainAdapter adapter;
    private static String searchtext;
    String[] titile = {"", "", ""};
    private static Context con;

    private  static ImageView control_bar_paly_pause,control_bar_musiclsit;
    private static SimpleDateFormat format;
    private static ProgressBar processbar;
    private static RecyclerView ry_control_bar;
    private static BottomViewAdapter cbAdapter;
    private static LinearLayout controlbarview;
    SearchActivity.MusicIdReceiver idReceiver;
    SearchActivity.MaxReceiver maxReceiver;
    SearchActivity.ProgressReceiver receiver;
    SearchActivity.MusicStateReceiver statereceiver;
    public void onCreate(Bundle savedInstanceState)
    {
        act=this;
        con=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Initview();
    }

    private void Initview() {

        InitSearchView();
        mViewPager=findViewById(R.id.search_viewpager);
        mViewPager.setScanScroll(false);
        fragments = new ArrayList<>();
        fragments.add(new SearchEntranceFragment());
        fragments.add(new SearchMainFragment());
        adapter = new MainAdapter(getSupportFragmentManager(), fragments, titile);
        mViewPager.setAdapter(adapter);
        InitBottomView();
        initReceiver();
}

    public static  void frenchlist(Music list)
    {
        controlbarview.setVisibility(View.VISIBLE);

        BaseInfo.Currentmusiclist.add(list);
        cbAdapter.notifyDataSetChanged();
        DateBaseUtils dateBaseUtils=new DateBaseUtils(con);
        DateBaseUtils.SetMusicList(BaseInfo.Currentmusiclist);
        //Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
      //  initList(BaseInfo.Currentmusiclist);

    }
    public static  void frenchview(int index)
    {

        ry_control_bar.scrollToPosition(index);
    }
    private void initReceiver() {
        format = new SimpleDateFormat("mm:ss");

        receiver = new SearchActivity.ProgressReceiver();
        IntentFilter filter = new IntentFilter(PlayService.ACTION_PROGRESS);
        registerReceiver(receiver, filter);

        maxReceiver = new SearchActivity.MaxReceiver();
        IntentFilter maxFilter = new IntentFilter(PlayService.ACTION_MAX);
        registerReceiver(maxReceiver, maxFilter);

        idReceiver=new SearchActivity.MusicIdReceiver();
        IntentFilter idFilter=new IntentFilter(PlayService.ACTION_MUSIC_ID);
        registerReceiver(idReceiver, idFilter);

        statereceiver=new SearchActivity.MusicStateReceiver();
        IntentFilter stateFilter=new IntentFilter(PlayService.ACTION_MUSIC_STATE);
        registerReceiver(statereceiver, stateFilter);

    }
    private void InitBottomView()
    {
        processbar=findViewById(R.id.buttom_control_processbar);

        control_bar_paly_pause=findViewById(R.id.iv_play_bar_play);
        control_bar_musiclsit=findViewById(R.id.iv_play_bar_playlist);
        controlbarview=findViewById(R.id.controlbarview);


        DateBaseUtils dateBaseUtils=new DateBaseUtils(this);
        BaseInfo.Currentmusiclist=DateBaseUtils.getMusicList();
        BaseInfo.CurrentMusicIndex=DateBaseUtils.getIndex();
        initList(BaseInfo.Currentmusiclist);


        control_bar_paly_pause.setOnClickListener(this);
        control_bar_musiclsit.setOnClickListener(this);

        cbAdapter=new BottomViewAdapter(R.layout.bottom_control_bar, BaseInfo.Currentmusiclist);
        cbAdapter.setOnItemClickListener(this);

        ry_control_bar=findViewById(R.id.ry_control_bar);
        ry_control_bar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        ry_control_bar.setAdapter(cbAdapter);
        if (BaseInfo.Currentmusiclist.size()==0)
        {
            controlbarview.setVisibility(View.GONE);
        }
        else
        {
            controlbarview.setVisibility(View.VISIBLE);
            cbAdapter.notifyDataSetChanged();
            ry_control_bar.scrollToPosition(BaseInfo.CurrentMusicIndex);
        }
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(ry_control_bar);
        ry_control_bar.scrollToPosition(BaseInfo.CurrentMusicIndex);
        ry_control_bar.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.e(TAG,"scrollStateChanged");

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    int  first = manager.findFirstVisibleItemPosition();
                    int last = manager.findLastVisibleItemPosition();
                    if (first == last ) {
                        //  PlayManager.play(first)
                        // startServicce(PlayService.FLAG_NEXT);
                        Intent intent = new Intent(getApplicationContext(), PlayService.class);
                        intent.putExtra("index", first);
                        intent.putExtra("flag", PlayService.FLAG_PLAY);
                        startService(intent);
                        DateBaseUtils.setIndex(first);
                        BaseInfo.CurrentMusicIndex=first;
                    }
                }


            }
        });
        startServicce(PlayService.FLAG_MAXPROGRESS);
    }
    // 向Service发出指令



    private  void startServicce(int flag) {
        Intent intent = new Intent(this, PlayService.class);
        intent.putExtra("flag", flag);
        startService(intent);
    }

    private  void initList(List<Music> musiclist ) {

        String[] lists=new String[musiclist.size()];
        for (int i=0;i<musiclist.size();i++)
        {
            lists[i]=musiclist.get(i).path;
        }
        // 播放路径作为参数传递给Service并做相应初始化
        Intent intent = new Intent(this, PlayService.class);
        intent.putExtra("list", lists);
        intent.putExtra("flag", PlayService.FLAG_LOAD_PATH);
        startService(intent);
    }

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter==cbAdapter)
        {
            startActivity(new Intent(this, MusicPlayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.local_back:
                finish();
                break;
            case R.id.iv_play_bar_playlist:
                //
                break;
            case R.id.iv_play_bar_play:
                startServicce(PlayService.FLAG_PlAY_PAUSE);

                break;
            default:

                break;

        }
    }
    private void InitSearchView()
    {
        mSearchView = findViewById(R.id.searchview);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.onActionViewExpanded();
        //设置搜索框展开时是否显示提交按钮，可不显示
        mSearchView.setSubmitButtonEnabled(false);
        //让键盘的回车键设置成搜索
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //搜索框是否展开，false表示展开
        mSearchView.setIconified(false);
        //获取焦点
        mSearchView.setFocusable(true);
        mSearchView.setFocusableInTouchMode(true);
       //mSearchView.requestFocusFromTouch();
       mSearchView.requestFocus();
        //设置提示词
        mSearchView.setQueryHint("请输入关键字");
        //设置输入框文字颜色
        EditText editText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.alpha_50_white));
        editText.setTextColor(ContextCompat.getColor(this, R.color.white));

        mSearchView.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
        setSearchvVewListener();
    }
    public static void setSearchViewtext(String text)
    {

        mViewPager.setCurrentItem(1,false);
        EditText editText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setText(text);
        mSearchView.clearFocus();
        SearchMusicPartFragment.StartPartSearch(text);
        searchtext=text;
    }
    public static String getSearchViewtext()
    {
        return searchtext;
    }

    private void setSearchvVewListener() {
        //搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // Toast.makeText(NextActivity.this, "Close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(NextActivity.this, "Open", Toast.LENGTH_SHORT).show();
            }
        });
        //搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
               mViewPager.setCurrentItem(1,false);
               if(SearchMusicFragment.getPage()==0)
                SearchMusicPartFragment.StartPartSearch(s);
                if(SearchMusicFragment.getPage()==1)
                    SearchMusicAllFragment.StartAllSearch(s);
                searchtext=s;

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //  LogUtil.e(NextActivity.class, "TextChange --> " + s);
                return false;
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
        if (isFromUser) {// 手动拖拽更新进度
            Intent intent = new Intent(this, PlayService.class);
            intent.putExtra("flag", PlayService.FLAG_PROGRESS);
            intent.putExtra("progress", progress);
            startService(intent);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    // 接收播放进度的广播接收器
    private static class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收当前进度，更新SeekBar的进度
            int progress = intent.getIntExtra("progress", 0);
            processbar.setProgress(progress);
            // String curTime = TestAct2.format.format(new Date(progress));
            // curTimeTextView.setText(curTime);
        }
    }
    // 接收最大进度的广播接收器
    private static class MaxReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int max = intent.getIntExtra("max", 0);
            String maxTime = format.format(new Date(max));
            //  maxTimeTextView.setText(maxTime);
            processbar.setMax(max);
        }
    }
    private  class MusicStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int state=intent.getIntExtra("state",-1);
            if (state==1)
            {
                control_bar_paly_pause.setImageResource(R.drawable.icon_bottom_pause);
            }
            if(state==0)
            {
                control_bar_paly_pause.setImageResource(R.drawable.icon_bottom_play);
            }
        }
    }
    private  class MusicIdReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取editor对象
            // int index=intent.getIntExtra("index", MainActivity.current_music_index);
            //  Log.i("12234567", "onReceive: "+index);

        }
    }
}
