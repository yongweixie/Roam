package com.example.xieyo.roam.musicactivity;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.BaseInfo;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.Service.PlayService;
import com.example.xieyo.roam.MyAdapter.MusicListRecyclerAdapter;
import com.example.xieyo.roam.MyAdapter.BottomViewAdapter;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.tools.ImageUtils;
import com.example.xieyo.roam.tools.Music;
import com.example.xieyo.roam.view.SpacesItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalMusicActivity extends BaseMusicActivity implements   BaseQuickAdapter.OnItemClickListener, View.OnClickListener ,SeekBar.OnSeekBarChangeListener{
    private TextView local_songnum_text;
    private Context mContext;
    private int songnum=0;
    private static ImageView btn_back;
    private List<Music> musiclist = new ArrayList<>();
    private static SimpleDateFormat format;
    private static ProgressBar processbar;
    private  static ImageView control_bar_paly_pause,control_bar_musiclsit;
    private RecyclerView ry_musiclist;
    private MusicListRecyclerAdapter mAdapter;
    private Context mcon;
    private RecyclerView ry_control_bar;
    private BottomViewAdapter cbAdapter;
    private static LinearLayout controlbarview;

    LocalMusicActivity.MusicIdReceiver idReceiver;
    LocalMusicActivity.MaxReceiver maxReceiver;
    LocalMusicActivity.ProgressReceiver receiver;
    LocalMusicActivity.MusicStateReceiver statereceiver;

    protected void onCreate(Bundle savedInstanceState) {
        mcon=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localmusic);
        initView(this);
        initReceiver();
        initList(BaseInfo.Currentmusiclist);

    }

    private void initView(Context context) {
        mContext = context;
        btn_back=findViewById(R.id.local_back);
        btn_back.setOnClickListener(this);
        local_songnum_text=findViewById(R.id.local_songnum_text);
        //获取editor对象

        processbar=findViewById(R.id.buttom_control_processbar);

       control_bar_paly_pause=findViewById(R.id.iv_play_bar_play);
       control_bar_musiclsit=findViewById(R.id.iv_play_bar_playlist);
        controlbarview=findViewById(R.id.controlbarview);

        DateBaseUtils dateBaseUtils=new DateBaseUtils(mcon);
        BaseInfo.Currentmusiclist.clear();
        BaseInfo.Currentmusiclist.addAll(DateBaseUtils.getMusicList()) ;
        BaseInfo.CurrentMusicIndex=DateBaseUtils.getIndex();

        control_bar_paly_pause.setOnClickListener(this);
        control_bar_musiclsit.setOnClickListener(this);

        Music mc =new Music();

        //获取resolver对象：（用于获取手机中音乐文件）
        ContentResolver resolver = this.getContentResolver();
        //创建游标
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        //游标移到第一行
        cursor.moveToFirst();
        do {
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));//根据Media获取相应数据
            String artist = cursor.getString(cursor.getColumnIndex("artist"));//直接输入字符串（另一种写法）
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            String Musicsize=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
           String path=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
            String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DISPLAY_NAME)));


           // Log.i("info","out"+fileName+"///////"+path);
            if(Integer.parseInt(Musicsize)>1024*1024)//1M以上的歌曲
            {
                int albumId  = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                ImageUtils imageUtils=new ImageUtils(mcon);


                mc =new Music();
                mc.title=title;

                mc.artist=artist+" - "+album;
                mc.path=path;
                Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
                mc.musicbmpUri = ContentUris.withAppendedId(artworkUri, albumId).toString();
                mc.Musicindex=songnum;
                mc.musicid="local";
                mc.type=2;
                songnum++;
                musiclist.add(mc);
            }

        }
        while (cursor.moveToNext());
        cursor.close();
        ry_musiclist = (RecyclerView)findViewById(R.id.ry_music_list);
        ry_musiclist.setLayoutManager(new LinearLayoutManager(this));
        ry_musiclist.addItemDecoration(new SpacesItemDecoration(4));
        // 填充数据
        mAdapter = new MusicListRecyclerAdapter(musiclist);
        mAdapter.setOnItemClickListener(this);
        ry_musiclist.setAdapter(mAdapter);

        ry_control_bar=findViewById(R.id.ry_control_bar);

        cbAdapter=new BottomViewAdapter(R.layout.bottom_control_bar,BaseInfo.Currentmusiclist);

        cbAdapter.notifyDataSetChanged();
        ry_control_bar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        ry_control_bar.setAdapter(cbAdapter);
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(ry_control_bar);
        //ry_control_bar.scrollToPosition(MainActivity.current_music_index);
        if (BaseInfo.Currentmusiclist.size()==0)
        {
            controlbarview.setVisibility(View.GONE);
        }
        else
        {
            controlbarview.setVisibility(View.VISIBLE);
            cbAdapter.setOnItemClickListener(this);
            ry_control_bar.scrollToPosition(BaseInfo.CurrentMusicIndex);
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
                    int  first = manager.findFirstVisibleItemPosition();
                    int last = manager.findLastVisibleItemPosition();
                    if (first == last ) {
                        //  PlayManager.play(first)
                        // startServicce(PlayService.FLAG_NEXT);
                        Intent intent = new Intent(mcon, PlayService.class);
                        intent.putExtra("index", first);
                        intent.putExtra("flag", PlayService.FLAG_PLAY);
                        startService(intent);
                        DateBaseUtils.setIndex(first);
                        BaseInfo.CurrentMusicIndex=first;
                    }
                }


            }
        });
//        Log.i("lll", "initView: "+bd.getlocalsongnum());
        startServicce(PlayService.FLAG_MAXPROGRESS);
    }
    int currentplayID=-1;

    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter==mAdapter)
        {
            controlbarview.setVisibility(View.VISIBLE);
            BaseInfo.Currentmusiclist.clear();
            BaseInfo.Currentmusiclist.addAll(musiclist);
            DateBaseUtils dateBaseUtils=new DateBaseUtils(mcon);
            DateBaseUtils.SetMusicList(BaseInfo.Currentmusiclist);
            cbAdapter.notifyDataSetChanged();
            initList(BaseInfo.Currentmusiclist);
            int index=BaseInfo.Currentmusiclist.get(position).Musicindex;
            if (currentplayID!=index)
            {
                Intent intent = new Intent(this, PlayService.class);
                intent.putExtra("index", index);
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

    public void onDestroy() {
        // TODO Auto-generated method stub
        unregisterReceiver(receiver);
        unregisterReceiver(idReceiver);
        unregisterReceiver(maxReceiver);
        super.onDestroy();
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

        receiver = new LocalMusicActivity.ProgressReceiver();
        IntentFilter filter = new IntentFilter(PlayService.ACTION_PROGRESS);
        registerReceiver(receiver, filter);

        maxReceiver = new LocalMusicActivity.MaxReceiver();
        IntentFilter maxFilter = new IntentFilter(PlayService.ACTION_MAX);
        registerReceiver(maxReceiver, maxFilter);

        idReceiver=new LocalMusicActivity.MusicIdReceiver();
        IntentFilter idFilter=new IntentFilter(PlayService.ACTION_MUSIC_ID);
        registerReceiver(idReceiver, idFilter);

        statereceiver=new LocalMusicActivity.MusicStateReceiver();
        IntentFilter stateFilter=new IntentFilter(PlayService.ACTION_MUSIC_STATE);
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

}
