package com.example.xieyo.roam.fragment;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.BaseInfo;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.MyAdapter.BottomViewAdapter;
import com.example.xieyo.roam.MyAdapter.ItemFragmentAdapter;
import com.example.xieyo.roam.Service.PlayService;
import com.example.xieyo.roam.musicfragment.MyMusicFragment;
import com.example.xieyo.roam.musicfragment.MusicDiscoveryFragment;
import com.example.xieyo.roam.musicfragment.MusicHallFragment;
import com.example.xieyo.roam.musicactivity.MusicPlayActivity;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.tools.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicFragment extends Fragment implements View.OnClickListener,BaseQuickAdapter.OnItemClickListener{
    private List<Fragment> mFragments;
    private ItemFragmentAdapter imAdapter;
    private BottomViewAdapter cbAdapter;
    private ViewPager viewpager;
    private Context con;
    private TabLayout tablayout2;
    private String[] names = {"我的音乐", "音乐馆","发现"};
    private static ProgressBar processbar;
    private  static ImageView control_bar_paly_pause,control_bar_musiclsit;
    private static RecyclerView ry_controlbar;
    private static LinearLayout controlbarview;
    //private List<Music> Curmusic=new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.activity_music, container, false);
        con = getContext();
//        DateBaseUtils dateBaseUtils=new DateBaseUtils(con);
//        BaseInfo.Currentmusiclist.clear();
//        BaseInfo.Currentmusiclist.addAll(DateBaseUtils.getMusicList());
//        BaseInfo.CurrentMusicIndex=DateBaseUtils.getIndex();
        viewpager = view1.findViewById(R.id.musicviewpager);
        tablayout2 = view1.findViewById(R.id.musictablayout);
        processbar=view1.findViewById(R.id.buttom_control_processbar);
        ry_controlbar =view1.findViewById(R.id.ry_control_bar);
        controlbarview=view1.findViewById(R.id.controlbarview);

        init();
        initReceiver();

        control_bar_paly_pause=view1.findViewById(R.id.iv_play_bar_play);
        control_bar_musiclsit=view1.findViewById(R.id.iv_play_bar_playlist);
       control_bar_musiclsit.setOnClickListener(this);
        control_bar_paly_pause.setOnClickListener(this);

        if (BaseInfo.Currentmusiclist.size()==0)
        {
            controlbarview.setVisibility(View.GONE);
        }
        else
        {
            controlbarview.setVisibility(View.VISIBLE);
            initList(BaseInfo.Currentmusiclist);
            ry_controlbar.scrollToPosition(BaseInfo.CurrentMusicIndex);
        }
        cbAdapter.notifyDataSetChanged();
        return view1;


    }

    public  void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }


    private  void init() {

        mFragments = new ArrayList<>();
        mFragments.add(new MyMusicFragment());
        mFragments.add(new MusicHallFragment());

        mFragments.add(new MusicDiscoveryFragment());

        imAdapter = new ItemFragmentAdapter(getChildFragmentManager(), names, mFragments, con);
        viewpager.setAdapter(imAdapter);
        tablayout2.setupWithViewPager(viewpager);
        cbAdapter=new BottomViewAdapter(R.layout.bottom_control_bar,BaseInfo.Currentmusiclist);

        ry_controlbar.setLayoutManager(new LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL,false));
        ry_controlbar.setAdapter(cbAdapter);

        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(ry_controlbar);
     //   Log.d("466", "index:"+MainActivity.current_music_index);

        ry_controlbar.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        Intent intent = new Intent(con, PlayService.class);
                        intent.putExtra("index", first);

                        intent.putExtra("flag", PlayService.FLAG_PLAY);
                        con.startService(intent);
                        DateBaseUtils.setIndex(first);
                        BaseInfo.CurrentMusicIndex=first;
                    }
                }


            }
        });

    }





    private void initList(List<Music> musiclist ) {

        String[] lists=new String[musiclist.size()];
        for (int i=0;i<musiclist.size();i++)
        {
            lists[i]=musiclist.get(i).path;
        }
        // 播放路径作为参数传递给Service并做相应初始化
        Intent intent = new Intent(getActivity(), PlayService.class);
        intent.putExtra("list", lists);
        intent.putExtra("flag", PlayService.FLAG_LOAD_PATH);
        getActivity().startService(intent);
    }
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        if (adapter==cbAdapter)
        {
            startActivity(new Intent(con, MusicPlayActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        }

    }

    public void onClick(View v)
    {
        switch (v.getId()){
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
    // 初始化广播接收器
    private void initReceiver() {

        MusicFragment.ProgressReceiver receiver = new MusicFragment.ProgressReceiver();
        IntentFilter filter = new IntentFilter(PlayService.ACTION_PROGRESS);
        getActivity().registerReceiver(receiver, filter);

        MusicFragment.MaxReceiver maxReceiver = new MusicFragment.MaxReceiver();
        IntentFilter maxFilter = new IntentFilter(PlayService.ACTION_MAX);
        getActivity().registerReceiver(maxReceiver, maxFilter);

        MusicFragment.MusicIdReceiver idReceiver=new MusicFragment.MusicIdReceiver();
        IntentFilter idFilter=new IntentFilter(PlayService.ACTION_MUSIC_ID);
        getActivity().registerReceiver(idReceiver, idFilter);

        MusicFragment.MusicStateReceiver statereceiver=new MusicFragment.MusicStateReceiver();
        IntentFilter stateFilter=new IntentFilter(PlayService.ACTION_MUSIC_STATE);
        getActivity().registerReceiver(statereceiver, stateFilter);

    }
    // 向Service发出指令
    private void startServicce(final int flag) {
        Intent intent = new Intent(getActivity(), PlayService.class);
        intent.putExtra("flag", flag);
        con.startService(intent);
    }



    // 接收播放进度的广播接收器
    private static class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收当前进度，更新SeekBar的进度
            int progress = intent.getIntExtra("progress", 0);
            processbar.setProgress(progress);
        }
    }
    // 接收最大进度的广播接收器
    private static class MaxReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int max = intent.getIntExtra("max", 0);
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
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        DateBaseUtils dateBaseUtils=new DateBaseUtils(con);
        BaseInfo.Currentmusiclist.clear();
        BaseInfo.Currentmusiclist.addAll(DateBaseUtils.getMusicList());
        BaseInfo.CurrentMusicIndex=DateBaseUtils.getIndex();
        if (BaseInfo.Currentmusiclist.size()==0)
        {
            controlbarview.setVisibility(View.GONE);
        }
        else
        {
            controlbarview.setVisibility(View.VISIBLE);
            cbAdapter=new BottomViewAdapter(R.layout.bottom_control_bar,BaseInfo.Currentmusiclist);
            cbAdapter.setOnItemClickListener(this);

            ry_controlbar.setLayoutManager(new LinearLayoutManager(con, LinearLayoutManager.HORIZONTAL,false));
            ry_controlbar.setAdapter(cbAdapter);

            // ry_control_bar.scrollToPosition(BaseInfo.CurrentMusicIndex);
        }
        cbAdapter.notifyDataSetChanged();
        initList(BaseInfo.Currentmusiclist);
        ry_controlbar.scrollToPosition(BaseInfo.CurrentMusicIndex);

    }


}
