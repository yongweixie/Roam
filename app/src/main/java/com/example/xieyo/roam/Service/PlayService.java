package com.example.xieyo.roam.Service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.xieyo.roam.BaseInfo;

import java.util.Timer;
import java.util.TimerTask;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener {
    // 操作标记
    public static final int FLAG_LOAD_PATH = 0;// 加载播放列表
    public static final int FLAG_PLAY = 1;// 开始/暂停
    public static final int FLAG_PREVIOUS = 2;// 上一首
    public static final int FLAG_NEXT = 3;// 下一首
    public static final int FLAG_PROGRESS = 4;// 更新进度
    public static final int FLAG_PlAY_PAUSE = 5;//
    public static final int FLAG_MAXPROGRESS = 6;// 最大进度

    public static final String ACTION_PROGRESS = "progress_action";// 发送当前进度的广播的动作
    public static final String ACTION_MAX = "max_action";// 发送最大进度的广播的动作
    public static final String ACTION_MUSIC_STATE = "musicstate_action";//当前正在播放的音乐以及状态，广播出去刷新界面
    public static final String ACTION_MUSIC_ID = "musicid_action";//当前正在播放的音乐以及状态，广播出去刷新界面


    // 播放列表
    private String[] lists;
    // 媒体播放器
    private MediaPlayer player;
//    private PLMediaPlayer mMediaPlayer = new PLMediaPlayer(this);
    //PLMediaPlayer mMediaPlayer = new PLMediaPlayer(mContext, mAVOptions);

    // 当前播放音乐的下角标
    private Timer timer;
    private int Getindex;
    private  int curIndex;
    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        // TODO Auto-generated method stub
        mp.start();
        sendMaxProgress();

    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化媒体播放器
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);

        curIndex= BaseInfo.CurrentMusicIndex;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int flag = intent.getIntExtra("flag", -1);
            Getindex=intent.getIntExtra("index",-1);
            switch (flag) {
                case FLAG_LOAD_PATH:// 加载播放列表
                    lists = intent.getStringArrayExtra("list");
                    if (Getindex!=0)
                    {
                        sendCurrentId();
                    }
                    break;
                case FLAG_PLAY:// 播放/暂停
//                    if (player.isPlaying()) {
//                        // 暂停
//                        player.pause();
//                    } else {
//                        // 播放
//                        play(curIndex);
//                    }
                    player.reset();
                    play(Getindex);
                    curIndex=Getindex;
                    sendCurrentId();

                    break;
                case FLAG_PlAY_PAUSE:
                    if (player.isPlaying()) {
                        // 暂停
                        player.pause();
                        sendCurrentState();
                    } else {
                        // 播放
                        play(curIndex);
                    }
                    break;
                case FLAG_PREVIOUS:// 上一曲
                    if (curIndex > 0) {
                        // 重置播放器资源
                        player.reset();
                        play(--curIndex);
                    }
                    if (curIndex == 0) {
                        // 重置播放器资源
                        player.reset();
                        play(curIndex);
                    }
                    sendCurrentId();

                    break;
                case FLAG_NEXT:// 下一首
                    if (curIndex < lists.length - 1) {
                        player.reset();
                        play(++curIndex);
                    }
                    if (curIndex ==lists.length - 1) {
                        player.reset();
                        play(curIndex);
                    }
                    sendCurrentId();

                    break;
                case FLAG_PROGRESS:// 更新播放进度
                    int progress = intent.getIntExtra("progress", 0);
                    player.seekTo(progress);
                    break;
                case FLAG_MAXPROGRESS:
                    sendMaxProgress();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // 播放指定歌曲
    private void play(int index) {
        try {
            // 设置播放文件的路径
            player.setDataSource(lists[index]);
            // 播放器执行准备工作，获取播放音乐信息，例如音乐长度
           // player.prepare();
            player.prepareAsync();

            // 通知SeekBar设置最大值
        } catch (Exception e) {
            e.printStackTrace();

        }
        // media.prepare();

        // 播放音乐
        // 音乐播放后，控制SeekBar进度
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("1622", "timer" + Thread.currentThread().getName());
                // 每隔1s发送一次广播，携带当前进度。主界面不断接收到广播，执行SeekBat更新进度的操作
                Intent intent = new Intent(ACTION_PROGRESS);
                intent.putExtra("progress", player.getCurrentPosition());// 获取当前播放进度
                sendBroadcast(intent);
            }
        }, 0, 100);

        sendCurrentState();
    }

    // 获取音乐最大进度，并使用广播通知SeekBar
    private void sendMaxProgress() {
        Intent intent = new Intent(ACTION_MAX);
        intent.putExtra("max", player.getDuration());// 每首音乐的完整时间
        sendBroadcast(intent);
    }
    private void sendCurrentId() {
        Intent intent = new Intent(ACTION_MUSIC_ID);
        intent.putExtra("index", curIndex);// 当前播放ID
        sendBroadcast(intent);
    }

    private void sendCurrentState()
    {
        //播放状态
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(ACTION_MUSIC_STATE);
                if(player.isPlaying())
                {
                    intent.putExtra("state", 1);//
                }
                else
                {
                    intent.putExtra("state", 0);//
                }
                sendBroadcast(intent);
            }
        }, 0, 300);


    }


    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        player.stop();
        player.release();// 释放资源
    }
}
