package com.example.xieyo.roam.musicactivity;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.MyAdapter.BottomViewAdapter;
import com.example.xieyo.roam.MyAdapter.MusicListRecyclerAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.baseinfo.BaseInfo;
import com.example.xieyo.roam.baseinfo.MusicBaseInfo;
import com.example.xieyo.roam.musicbean.Music;
import com.example.xieyo.roam.musicbean.MusicList;
import com.example.xieyo.roam.service.PlayService;
import com.example.xieyo.roam.tools.BmobApi;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.tools.MusicApi;
import com.example.xieyo.roam.view.SpacesItemDecoration;

import org.json.JSONArray;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class FavMusicActivity extends BaseMusicActivity implements BaseQuickAdapter.OnItemClickListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private TextView local_songnum_text;
    private Context mContext;
    private int songnum = 0;
    private static ImageView btn_back;
    private List<Music> musiclist = new ArrayList<>();
    private static SimpleDateFormat format;
    private static ProgressBar processbar;
    private static ImageView control_bar_paly_pause, control_bar_musiclsit;
    private RecyclerView ry_musiclist;
    private MusicListRecyclerAdapter mAdapter;
    private Context mcon;
    private RecyclerView ry_control_bar;
    private BottomViewAdapter cbAdapter;
    private static LinearLayout controlbarview;

    FavMusicActivity.MusicIdReceiver idReceiver;
    FavMusicActivity.MaxReceiver maxReceiver;
    FavMusicActivity.ProgressReceiver receiver;
    FavMusicActivity.MusicStateReceiver statereceiver;

    protected void onCreate(Bundle savedInstanceState) {
        mcon = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localmusic);
        initView(this);
        initReceiver();
        initList(MusicBaseInfo.Currentmusiclist);

    }

    private void initView(Context context) {
        mContext = context;
        LinearLayout backbutton = findViewById(R.id.back);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        local_songnum_text = findViewById(R.id.songnum);
        //获取editor对象

        processbar = findViewById(R.id.buttom_control_processbar);

        control_bar_paly_pause = findViewById(R.id.iv_play_bar_play);
        control_bar_musiclsit = findViewById(R.id.iv_play_bar_playlist);
        controlbarview = findViewById(R.id.controlbarview);

        DateBaseUtils dateBaseUtils = new DateBaseUtils(mcon);
        MusicBaseInfo.Currentmusiclist.clear();
        MusicBaseInfo.Currentmusiclist.addAll(DateBaseUtils.getMusicList());
        MusicBaseInfo.CurrentMusicIndex = DateBaseUtils.getIndex();

        control_bar_paly_pause.setOnClickListener(this);
        control_bar_musiclsit.setOnClickListener(this);


        ry_musiclist = (RecyclerView) findViewById(R.id.ry_music_list);
        ry_musiclist.setLayoutManager(new LinearLayoutManager(this));
        ry_musiclist.addItemDecoration(new SpacesItemDecoration(4));
        // 填充数据
        mAdapter = new MusicListRecyclerAdapter(musiclist);

        mAdapter.setOnItemClickListener(this);
        ry_musiclist.setAdapter(mAdapter);
        BmobQuery query = new BmobQuery("u" + BaseInfo.account);
        query.addWhereEqualTo("type", "music");
        query.setLimit(500);
        query.order("createdAt");
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {

                    //Log.i("123456", "done: "+ary.toString());
                    try {
                        for (int i = 0; i < ary.length(); i++) {
                            Music mc = new Music();
                            String get = ary.getJSONObject(i).getString("data");
                            //  BmobApi.UpLoadData(bmc.artist+"@_@"+bmc.title+"@_@"+bmc.musicbmpUri+"@_@"+bmc.path+"@_@"+bmc.musicid+"@_@"+bmc.from,"music");
                            Log.i("123456", "done: "+get.toString());

                            mc.title = get.split("@_@")[1];

                            mc.artist = get.split("@_@")[0];
                            mc.path = get.split("@_@")[3];
                            mc.musicbmpUri = get.split("@_@")[2];
                            mc.Musicindex = i;
                            mc.musicid = get.split("@_@")[4];

                            mc.type = 2;
                            mc.from=Integer.valueOf(get.split("@_@")[5]) ;
                            musiclist.add(mc);
                            mAdapter.notifyDataSetChanged();

                        }
                    } catch (Exception ex) {

                        Log.i("123456", "done: "+ex.toString());

                    }

                } else {

                }
            }
        });


        ry_control_bar = findViewById(R.id.ry_control_bar);

        cbAdapter = new BottomViewAdapter(R.layout.bottom_control_bar, MusicBaseInfo.Currentmusiclist);

        cbAdapter.notifyDataSetChanged();
        ry_control_bar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ry_control_bar.setAdapter(cbAdapter);
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(ry_control_bar);
        //ry_control_bar.scrollToPosition(MainActivity.current_music_index);
        if (MusicBaseInfo.Currentmusiclist.size() == 0) {
            controlbarview.setVisibility(View.GONE);
        } else {
            controlbarview.setVisibility(View.VISIBLE);
            cbAdapter.setOnItemClickListener(this);
            ry_control_bar.scrollToPosition(MusicBaseInfo.CurrentMusicIndex);
        }
        ry_control_bar.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.e(TAG,"scrollStateChanged");

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int first = manager.findFirstVisibleItemPosition();
                    int last = manager.findLastVisibleItemPosition();
                    if (first == last) {
                        //  PlayManager.play(first)
                        // startServicce(PlayService.FLAG_NEXT);
                        Intent intent = new Intent(mcon, PlayService.class);
                        intent.putExtra("index", first);
                        intent.putExtra("flag", PlayService.FLAG_PLAY);
                        startService(intent);
                        DateBaseUtils.setIndex(first);
                        MusicBaseInfo.CurrentMusicIndex = first;
                    }
                }


            }
        });
//        Log.i("lll", "initView: "+bd.getlocalsongnum());
        startServicce(PlayService.FLAG_MAXPROGRESS);
    }

    int currentplayID = -1;

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mAdapter) {
            controlbarview.setVisibility(View.VISIBLE);
            MusicBaseInfo.Currentmusiclist.clear();
            MusicBaseInfo.Currentmusiclist.addAll(musiclist);
            DateBaseUtils dateBaseUtils = new DateBaseUtils(mcon);
            DateBaseUtils.SetMusicList(MusicBaseInfo.Currentmusiclist);
            cbAdapter.notifyDataSetChanged();
            initList(MusicBaseInfo.Currentmusiclist);
            int index = MusicBaseInfo.Currentmusiclist.get(position).Musicindex;
            if (currentplayID != index) {
                Intent intent = new Intent(this, PlayService.class);
                intent.putExtra("index", index);
                intent.putExtra("flag", PlayService.FLAG_PLAY);
                startService(intent);
                ry_control_bar.scrollToPosition(index);

            }
            currentplayID = index;
            DateBaseUtils.setIndex(index);
            MusicBaseInfo.CurrentMusicIndex = index;
        }
        if (adapter == cbAdapter) {
            startActivity(new Intent(this, MusicPlayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }

    }

    public void onClick(View v) {

        switch (v.getId()) {
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

    public void onDestroy() {
        // TODO Auto-generated method stub
        unregisterReceiver(receiver);
        unregisterReceiver(idReceiver);
        unregisterReceiver(maxReceiver);
        super.onDestroy();
    }

    private void initList(List<Music> musiclist) {

        String[] lists = new String[musiclist.size()];
        for (int i = 0; i < musiclist.size(); i++) {
            lists[i] = musiclist.get(i).path;
        }
        // 播放路径作为参数传递给Service并做相应初始化
        Intent intent = new Intent(this, PlayService.class);
        intent.putExtra("list", lists);
        intent.putExtra("flag", PlayService.FLAG_LOAD_PATH);
        startService(intent);
    }

    // 初始化广播接收器
    private void initReceiver() {
        format = new SimpleDateFormat("mm:ss");

        receiver = new FavMusicActivity.ProgressReceiver();
        IntentFilter filter = new IntentFilter(PlayService.ACTION_PROGRESS);
        registerReceiver(receiver, filter);

        maxReceiver = new FavMusicActivity.MaxReceiver();
        IntentFilter maxFilter = new IntentFilter(PlayService.ACTION_MAX);
        registerReceiver(maxReceiver, maxFilter);

        idReceiver = new FavMusicActivity.MusicIdReceiver();
        IntentFilter idFilter = new IntentFilter(PlayService.ACTION_MUSIC_ID);
        registerReceiver(idReceiver, idFilter);

        statereceiver = new FavMusicActivity.MusicStateReceiver();
        IntentFilter stateFilter = new IntentFilter(PlayService.ACTION_MUSIC_STATE);
        registerReceiver(statereceiver, stateFilter);

    }

    // 向Service发出指令
    private void startServicce(int flag) {
        Intent intent = new Intent(this, PlayService.class);
        intent.putExtra("flag", flag);
        startService(intent);
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

    private class MusicStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra("state", -1);
            if (state == 1) {
                control_bar_paly_pause.setImageResource(R.drawable.icon_bottom_pause);
            }
            if (state == 0) {
                control_bar_paly_pause.setImageResource(R.drawable.icon_bottom_play);
            }
        }
    }

    private class MusicIdReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

}
