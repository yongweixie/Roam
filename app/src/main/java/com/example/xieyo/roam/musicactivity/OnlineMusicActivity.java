package com.example.xieyo.roam.musicactivity;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.baseinfo.BaseInfo;
import com.example.xieyo.roam.baseinfo.MusicBaseInfo;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.MyAdapter.BottomViewAdapter;
import com.example.xieyo.roam.MyAdapter.MusicListRecyclerAdapter;
import com.example.xieyo.roam.bookactivity.BookDigestList;
import com.example.xieyo.roam.service.PlayService;
import com.example.xieyo.roam.tools.BmobApi;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.musicbean.Music;
import com.example.xieyo.roam.tools.MusicApi;
import com.example.xieyo.roam.musicbean.MusicList;
import com.example.xieyo.roam.view.MusicListDialog;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class OnlineMusicActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener, MusicListDialog.OnCenterItemClickListener {

    private RecyclerView ry;
    private MusicListRecyclerAdapter mAdapter;
    private List<Music> mList = new ArrayList<>();
    private List<Music> music = new ArrayList<>();

    private Context con;
    OnlineMusicActivity.MusicIdReceiver idReceiver;
    OnlineMusicActivity.MaxReceiver maxReceiver;
    OnlineMusicActivity.ProgressReceiver receiver;
    OnlineMusicActivity.MusicStateReceiver statereceiver;
    private static ImageView control_bar_paly_pause, control_bar_musiclsit;
    private static SimpleDateFormat format;
    private static ProgressBar processbar;
    private RecyclerView ry_control_bar;
    private BottomViewAdapter cbAdapter;
    private static LinearLayout controlbarview;
    private Toolbar mToolbar;
    private LinearLayout headerbar;
    private MusicListDialog dialog;
    private String musiclistdesc = new String();
    private static AVLoadingIndicatorView avi;
    private static TextView songnum;
    private static String bmobtext;
    TextView tv_list_title;
    TextView tv_playcount;
    ImageView iv_userface;
    TextView tv_username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlinemusic);
        con = getApplicationContext();

        //  InitToolBar();
        initview();

        initReceiver();
    }

    private void showDialog(String title, String Url, String Desc) {
        dialog = new MusicListDialog(this, R.layout.dialog_musiclist, new int[]{R.id.list_cover, R.id.dialog_view});
        dialog.setOnCenterItemClickListener((MusicListDialog.OnCenterItemClickListener) this);

        dialog.show();
        dialog.setData(title, Url, Desc);
        //调用点击函数
    }


    public void OnCenterItemClick(MusicListDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.list_cover:
                //Toast.makeText(getApplicationContext(),"点击了",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private final class Listndler extends Handler {
        WeakReference<OnlineMusicActivity> mMainActivityWeakReference;

        public Listndler(OnlineMusicActivity mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MusicList ml = (MusicList) msg.obj;
            mAdapter.notifyDataSetChanged();
            avi.hide();

            mAdapter.loadMoreComplete();
            RequestOptions options = new RequestOptions().placeholder(R.drawable.default_cover)
                    .optionalTransform((new CircleCrop()))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();

            Glide.with(getApplication()).load(ml.userface).apply(options)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .into(iv_userface);
            tv_list_title.setText(MusicBaseInfo.MusicListTitle);
            tv_playcount.setText(ml.playCount);
            tv_username.setText(ml.creator);
            songnum.setText("(共" + mList.size() + "首)");
            bmobtext= MusicBaseInfo.MusicListImageUrl+"@_@"+MusicBaseInfo.MusicListTitle+"@_@"+ ml.creator+"@_@"+MusicBaseInfo.MusicListId+"@_@"+MusicBaseInfo.CurrentMusicListFrom;
            musiclistdesc = ml.desc;

        }
    }

    final OnlineMusicActivity.Listndler handler = new OnlineMusicActivity.Listndler(this);

    public void initview() {
        avi = findViewById(R.id.loadingview);
        avi.show();
        ry = (RecyclerView) findViewById(R.id.ry_onlinemusic_list);
        ry.setLayoutManager(new LinearLayoutManager(this));

        // ry.addItemDecoration(new SpacesItemDecoration(4));

        // 填充数据
        mAdapter = new MusicListRecyclerAdapter(mList);
        mAdapter.setOnItemClickListener(this);
        View headerscroolView = getLayoutInflater().inflate(R.layout.head_online_music_scrool, null);
        mAdapter.addHeaderView(headerscroolView);

        ImageView iv_list_cover = headerscroolView.findViewById(R.id.iv_musiclist_cover);
        iv_list_cover.setOnClickListener(this);
        tv_list_title = headerscroolView.findViewById(R.id.tv_musiclist_title);
        tv_playcount = headerscroolView.findViewById(R.id.tv_playCount);
        iv_userface = headerscroolView.findViewById(R.id.iv_userface);
        tv_username = headerscroolView.findViewById(R.id.tv_username);
        ImageView favlist = headerscroolView.findViewById(R.id.iv_listen_favourite);
        favlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobApi.UpLoadData(bmobtext,"favlist");
                Toast.makeText(OnlineMusicActivity.this, "收藏成功",Toast.LENGTH_LONG).show();

            }
        });
        LinearLayout backbutton = findViewById(R.id.back);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RequestOptions options1 = new RequestOptions().placeholder(R.drawable.default_cover)
                .optionalTransform(new RoundedCornersTransformation(15, 0, RoundedCornersTransformation.CornerType.ALL))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(getApplication()).load(MusicBaseInfo.MusicListImageUrl).apply(options1)
                .into(iv_list_cover);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // TODO: http request.
                Message msg = new Message();
                mList.addAll(MusicApi.getMusicfromList(MusicBaseInfo.MusicListId, MusicBaseInfo.CurrentMusicListFrom));
                msg.obj = MusicApi.getMusitListdata(MusicBaseInfo.MusicListId, MusicBaseInfo.CurrentMusicListFrom);
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();


        View headerView = getLayoutInflater().inflate(R.layout.music_list_header_, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        songnum = headerView.findViewById(R.id.songnum);

        mAdapter.addHeaderView(headerView);
        final TextView tv_musiclistname = findViewById(R.id.musiclist_name);

        headerbar = findViewById(R.id.online_header_background);
        ry.setAdapter(mAdapter);
        ry.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int mAlpha = 0;

            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                int minHeight = 50;
                int maxHeight = 500;
                if (rv.computeVerticalScrollOffset() <= minHeight) {
                    mAlpha = 0;
                }
                // 滑动距离大于定义得最大距离
                else if (rv.computeVerticalScrollOffset() > maxHeight) {
                    mAlpha = 255;
                }
                // 滑动距离处于最小和最大距离之间
                else {
                    // （滑动距离 - 开始变化距离）：最大限制距离 = mAlpha ：255
                    mAlpha = (rv.computeVerticalScrollOffset() - minHeight) * 255 / (maxHeight - minHeight);
                }

                // 初始状态 标题栏/导航栏透明等
                if (mAlpha <= 0) {
                    //setViewBackgroundAlpha(headerbar, 255);
                    headerbar.setBackgroundColor(getResources().getColor(R.color.app_content_color));
                    tv_musiclistname.setText("歌单");
                }
                //  终止状态：标题栏/导航栏 不在进行变化
                else if (mAlpha >= 255) {
                    //setViewBackgroundAlpha(headerbar, 0);
                    headerbar.setBackgroundColor(getResources().getColor(R.color.head_bottom_color));
                    tv_musiclistname.setText(MusicBaseInfo.MusicListTitle);

                }
                // 变化中状态：标题栏/导航栏随ScrollView 的滑动而产生相应变化
                else {
                    //setViewBackgroundAlpha(headerbar, 255-mAlpha);
                }


            }
        });


        processbar = findViewById(R.id.buttom_control_processbar);

        control_bar_paly_pause = findViewById(R.id.iv_play_bar_play);
        control_bar_musiclsit = findViewById(R.id.iv_play_bar_playlist);
        controlbarview = findViewById(R.id.controlbarview);


        DateBaseUtils dateBaseUtils = new DateBaseUtils(con);
        MusicBaseInfo.Currentmusiclist = DateBaseUtils.getMusicList();
        MusicBaseInfo.CurrentMusicIndex = DateBaseUtils.getIndex();
        initList(MusicBaseInfo.Currentmusiclist);


        control_bar_paly_pause.setOnClickListener(this);
        control_bar_musiclsit.setOnClickListener(this);

        cbAdapter = new BottomViewAdapter(R.layout.bottom_control_bar, MusicBaseInfo.Currentmusiclist);
        cbAdapter.setOnItemClickListener(this);

        ry_control_bar = findViewById(R.id.ry_control_bar);
        ry_control_bar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ry_control_bar.setAdapter(cbAdapter);
        if (MusicBaseInfo.Currentmusiclist.size() == 0) {
            controlbarview.setVisibility(View.GONE);
        } else {
            controlbarview.setVisibility(View.VISIBLE);
            cbAdapter.notifyDataSetChanged();
            ry_control_bar.scrollToPosition(MusicBaseInfo.CurrentMusicIndex);
        }
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(ry_control_bar);
        ry_control_bar.scrollToPosition(MusicBaseInfo.CurrentMusicIndex);
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
                    int first = manager.findFirstVisibleItemPosition();
                    int last = manager.findLastVisibleItemPosition();
                    if (first == last) {
                        //  PlayManager.play(first)
                        // startServicce(PlayService.FLAG_NEXT);
                        Intent intent = new Intent(con, PlayService.class);
                        intent.putExtra("index", first);
                        intent.putExtra("flag", PlayService.FLAG_PLAY);
                        startService(intent);
                        DateBaseUtils.setIndex(first);
                        MusicBaseInfo.CurrentMusicIndex = first;
                    }
                }


            }
        });
        startServicce(PlayService.FLAG_MAXPROGRESS);

    }

    //    public void InitToolBar()
//    {
//        mToolbar=findViewById(R.id.tb_toolbar);
//        setSupportActionBar(mToolbar);
//    }
    public void setViewBackgroundAlpha(LinearLayout view, int alpha) {
        if (view == null) return;

        Drawable drawable = view.getBackground();
        if (drawable != null) {
            drawable.mutate().setAlpha(alpha);
        }
    }

    int currentplayID = -1;

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == mAdapter) {
            controlbarview.setVisibility(View.VISIBLE);

            MusicBaseInfo.Currentmusiclist.clear();
            MusicBaseInfo.Currentmusiclist.addAll(mList);
            cbAdapter.notifyDataSetChanged();
            DateBaseUtils dateBaseUtils = new DateBaseUtils(con);
            DateBaseUtils.SetMusicList(MusicBaseInfo.Currentmusiclist);
            //Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            initList(MusicBaseInfo.Currentmusiclist);
            int index = MusicBaseInfo.Currentmusiclist.get(position).Musicindex;
            if (currentplayID != index) {
                Intent intent = new Intent(this, PlayService.class);
                intent.putExtra("index", mList.get(position).Musicindex);
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

        receiver = new OnlineMusicActivity.ProgressReceiver();
        IntentFilter filter = new IntentFilter(PlayService.ACTION_PROGRESS);
        registerReceiver(receiver, filter);

        maxReceiver = new OnlineMusicActivity.MaxReceiver();
        IntentFilter maxFilter = new IntentFilter(PlayService.ACTION_MAX);
        registerReceiver(maxReceiver, maxFilter);

        idReceiver = new OnlineMusicActivity.MusicIdReceiver();
        IntentFilter idFilter = new IntentFilter(PlayService.ACTION_MUSIC_ID);
        registerReceiver(idReceiver, idFilter);

        statereceiver = new OnlineMusicActivity.MusicStateReceiver();
        IntentFilter stateFilter = new IntentFilter(PlayService.ACTION_MUSIC_STATE);
        registerReceiver(statereceiver, stateFilter);

    }

    // 向Service发出指令
    private void startServicce(int flag) {
        Intent intent = new Intent(this, PlayService.class);
        intent.putExtra("flag", flag);
        startService(intent);
    }


    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_musiclist_cover:
                //
                showDialog(MusicBaseInfo.MusicListTitle, MusicBaseInfo.MusicListImageUrl, musiclistdesc);
                break;
            case R.id.iv_play_bar_play:
                startServicce(PlayService.FLAG_PlAY_PAUSE);

                break;
            default:

                break;

        }
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
            //获取editor对象
            // int index=intent.getIntExtra("index", MainActivity.current_music_index);
            //  Log.i("12234567", "onReceive: "+index);

        }
    }

    protected void onDestroy() {
        super.onDestroy();

    }

}
