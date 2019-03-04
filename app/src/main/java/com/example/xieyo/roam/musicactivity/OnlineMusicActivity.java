package com.example.xieyo.roam.musicactivity;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.BaseInfo;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.Service.PlayService;
import com.example.xieyo.roam.MyAdapter.BottomViewAdapter;
import com.example.xieyo.roam.MyAdapter.MusicListRecyclerAdapter;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.tools.Music;
import com.example.xieyo.roam.tools.MusicApi;
import com.example.xieyo.roam.tools.MusicList;
import com.example.xieyo.roam.view.MusicListDialog;

import org.jsoup.Connection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class OnlineMusicActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,SeekBar.OnSeekBarChangeListener,View.OnClickListener,MusicListDialog.OnCenterItemClickListener {

    private RecyclerView ry;
    private MusicListRecyclerAdapter mAdapter;
    private  List<Music> mList= new ArrayList<>();
    private List<Music> music=new ArrayList<>();

    private Context con;
    OnlineMusicActivity.MusicIdReceiver idReceiver;
    OnlineMusicActivity.MaxReceiver maxReceiver;
    OnlineMusicActivity.ProgressReceiver receiver;
    OnlineMusicActivity.MusicStateReceiver statereceiver;
    private  static ImageView control_bar_paly_pause,control_bar_musiclsit;
    private static SimpleDateFormat format;
    private static ProgressBar processbar;
    private RecyclerView ry_control_bar;
    private BottomViewAdapter cbAdapter;
    private static LinearLayout controlbarview;
    private Toolbar mToolbar;
    private LinearLayout headerbar;
    private MusicListDialog dialog;
    private String musiclistdesc=new String();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlinemusic);
        con=this;
      //  InitToolBar();
        initview();

        initReceiver();
    }
    private void showDialog(String title,String Url,String Desc)
    {
        dialog=new MusicListDialog(this,R.layout.dialog_musiclist,new int[]{R.id.list_cover,R.id.dialog_view});
        dialog.setOnCenterItemClickListener((MusicListDialog.OnCenterItemClickListener) this);

        dialog.show();
        dialog.setData(title,Url,Desc);
        //调用点击函数
    }


    public void OnCenterItemClick(MusicListDialog dialog, View view) {
        switch (view.getId()){
            case R.id.list_cover:
                //Toast.makeText(getApplicationContext(),"点击了",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
    public void initview()
    {

        ry = (RecyclerView)findViewById(R.id.ry_onlinemusic_list);
        ry.setLayoutManager(new LinearLayoutManager(this));
       // ry.addItemDecoration(new SpacesItemDecoration(4));

        // 填充数据
        mAdapter = new MusicListRecyclerAdapter( mList);
        mAdapter.setOnItemClickListener(this);
        View headerscroolView=getLayoutInflater().inflate(R.layout.online_music_scrool_header, null);
        mAdapter.addHeaderView(headerscroolView);

        ImageView iv_list_cover=headerscroolView.findViewById(R.id.iv_musiclist_cover);
        iv_list_cover.setOnClickListener(this);
        final TextView tv_list_title=headerscroolView.findViewById(R.id.tv_musiclist_title);
        final TextView tv_playcount=headerscroolView.findViewById(R.id.tv_playCount);
        final ImageView iv_userface=headerscroolView.findViewById(R.id.iv_userface);
        final TextView tv_username=headerscroolView.findViewById(R.id.tv_username);
        RequestOptions options1 = new RequestOptions().placeholder(R.drawable.default_cover)
                .optionalTransform(new RoundedCornersTransformation(15,0, RoundedCornersTransformation.CornerType.ALL))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        Glide.with(getApplication()).load(BaseInfo.MusicListImageUrl).apply(options1)
                .into(iv_list_cover);

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    MusicList ml=MusicApi.getMusitListdata(BaseInfo.MusicListId, BaseInfo.CurrentMusicListFrom);
                    @Override
                    public void run() {

                        //background.setBackgroundDrawable(getForegroundDrawable(drawable));
                        //background.setBackground(createBlurredImageFromBitmap(drawableToBitmap(drawable),3));

                        RequestOptions options = new RequestOptions().placeholder(R.drawable.default_cover)
                                .optionalTransform((new CircleCrop()))
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                        Glide.with(getApplication()).load(ml.userface).apply(options)
                                .into(iv_userface);
                        tv_list_title.setText(BaseInfo.MusicListTitle);
                        tv_playcount.setText(ml.playCount);
                        tv_username.setText(ml.creator);
                        musiclistdesc=ml.desc;

                    }
                });
            }
        }).start();

        View headerView=getLayoutInflater().inflate(R.layout.music_list_header_, null);

        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        mAdapter.addHeaderView(headerView);
        final TextView tv_musiclistname=findViewById(R.id.musiclist_name);

        headerbar=findViewById(R.id.online_header_background);
        ry.setAdapter(mAdapter);
        ry.setOnScrollListener(new RecyclerView.OnScrollListener() {
             int mAlpha=0;
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                Log.i("12345678", rv.computeVerticalScrollOffset() + "");
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
                    mAlpha = (rv.computeVerticalScrollOffset()- minHeight) * 255 / (maxHeight - minHeight);
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
                    tv_musiclistname.setText(BaseInfo.MusicListTitle);

                }
                // 变化中状态：标题栏/导航栏随ScrollView 的滑动而产生相应变化
                else {
                  //setViewBackgroundAlpha(headerbar, 255-mAlpha);
                }


            }
        });

        music = MusicApi.getMusicfromList(BaseInfo.MusicListId,BaseInfo.CurrentMusicListFrom);
        mList.addAll(music);
        processbar=findViewById(R.id.buttom_control_processbar);

        control_bar_paly_pause=findViewById(R.id.iv_play_bar_play);
        control_bar_musiclsit=findViewById(R.id.iv_play_bar_playlist);
        controlbarview=findViewById(R.id.controlbarview);


        DateBaseUtils dateBaseUtils=new DateBaseUtils(con);
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
                        Intent intent = new Intent(con, PlayService.class);
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

    int currentplayID=-1;

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter==mAdapter)
        {
            controlbarview.setVisibility(View.VISIBLE);

            BaseInfo.Currentmusiclist.clear();
            BaseInfo.Currentmusiclist.addAll(mList);
            cbAdapter.notifyDataSetChanged();
            DateBaseUtils dateBaseUtils=new DateBaseUtils(con);
            DateBaseUtils.SetMusicList(BaseInfo.Currentmusiclist);
            //Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            initList(BaseInfo.Currentmusiclist);
            int index=BaseInfo.Currentmusiclist.get(position).Musicindex;
            if (currentplayID!=index)
            {
                Intent intent = new Intent(this, PlayService.class);
                intent.putExtra("index",  mList.get(position).Musicindex);
                intent.putExtra("flag", PlayService.FLAG_PLAY);
                startService(intent);
                ry_control_bar.scrollToPosition(index);

            }
            currentplayID=index;
            DateBaseUtils.setIndex(index);
            BaseInfo.CurrentMusicIndex=index;
        }
        if (adapter==cbAdapter)
        {
            startActivity(new Intent(this, MusicPlayActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }

    private void initList(List<Music> musiclist ) {

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
    // 初始化广播接收器
    private void initReceiver() {
        format = new SimpleDateFormat("mm:ss");

        receiver = new OnlineMusicActivity.ProgressReceiver();
        IntentFilter filter = new IntentFilter(PlayService.ACTION_PROGRESS);
        registerReceiver(receiver, filter);

        maxReceiver = new OnlineMusicActivity.MaxReceiver();
        IntentFilter maxFilter = new IntentFilter(PlayService.ACTION_MAX);
        registerReceiver(maxReceiver, maxFilter);

        idReceiver=new OnlineMusicActivity.MusicIdReceiver();
        IntentFilter idFilter=new IntentFilter(PlayService.ACTION_MUSIC_ID);
        registerReceiver(idReceiver, idFilter);

        statereceiver=new OnlineMusicActivity.MusicStateReceiver();
        IntentFilter stateFilter=new IntentFilter(PlayService.ACTION_MUSIC_STATE);
        registerReceiver(statereceiver, stateFilter);

    }
    // 向Service发出指令
    private void startServicce(int flag) {
        Intent intent = new Intent(this, PlayService.class);
        intent.putExtra("flag", flag);
        startService(intent);
    }


    public void onClick(View v) {

        switch (v.getId()){
            case R.id.local_back:
                finish();
                break;
            case R.id.iv_musiclist_cover:
                //
                showDialog(BaseInfo.MusicListTitle,BaseInfo.MusicListImageUrl,musiclistdesc);
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
    protected void onDestroy() {
        super.onDestroy();

    }

}
