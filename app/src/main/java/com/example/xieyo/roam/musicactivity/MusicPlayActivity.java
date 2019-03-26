package com.example.xieyo.roam.musicactivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.xieyo.roam.baseinfo.MusicBaseInfo;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.movieactivity.MovieDigestList;
import com.example.xieyo.roam.service.PlayService;
import com.example.xieyo.roam.lyricview.Lrc;
import com.example.xieyo.roam.lyricview.LrcHelper;
import com.example.xieyo.roam.lyricview.LrcView;
import com.example.xieyo.roam.tools.BmobApi;
import com.example.xieyo.roam.tools.DisplayUtils;
import com.example.xieyo.roam.tools.FastBlurUtil;
import com.example.xieyo.roam.musicbean.Music;
import com.example.xieyo.roam.tools.MusicApi;
import com.example.xieyo.roam.view.CircleRotatingImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import me.wcy.lrcview.LrcView;



public class MusicPlayActivity  extends BaseMusicActivity implements SeekBar.OnSeekBarChangeListener{

    private ViewPager viewPager;
    private PagerAdapter adapter;
    private List<View> viewPages = new ArrayList<>();
    //包裹点点的LinearLayout
    private ViewGroup group;
    private LinearLayout background;
    private ImageView imageView,iv_listen_favourite;
    private ImageView play_pause;
    //定义一个ImageVIew数组，来存放生成的小园点
    private ImageView[] imageViews;
    private  TextView tv_title,tv_current_time,tv_max_time,tv_artist;
    private  SeekBar seekbar;
    private  static SimpleDateFormat format;
    private static CircleRotatingImageView civ;
    private static LrcView lrcview,singlelinelrc;
    private static Context con;
    private static int bmobid=0;
    MusicPlayActivity.MusicIdReceiver idReceiver;
    MusicPlayActivity.MaxReceiver maxReceiver;
    MusicPlayActivity.ProgressReceiver receiver;
    MusicPlayActivity.MusicStateReceiver statereceiver;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.music_play_layout);
       getWindow().setExitTransition(new Fade().setDuration(2000));
        con=this;
        initView();
        initPointer();
        initList(MusicBaseInfo.Currentmusiclist);
        initReceiver();
        initPageAdapter();
        initPointer();
        viewPager.setCurrentItem(1);
        startServicce(PlayService.FLAG_MAXPROGRESS);

        //pasingJson(getResult(path));
    }

    public void onDestroy() {
        // TODO Auto-generated method stub
        unregisterReceiver(receiver);
        unregisterReceiver(idReceiver);
        unregisterReceiver(maxReceiver);
        unregisterReceiver(statereceiver);
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
    private void initPageAdapter() {
        /**
         * 对于这几个想要动态载入的page页面，使用LayoutInflater.inflate()来找到其布局文件，并实例化为View对象
         */
        LayoutInflater inflater = LayoutInflater.from(this);
        View page1 = inflater.inflate(R.layout.vp_listen_about, null);
        View page2 = inflater.inflate(R.layout.vp_listen_album, null);
        View page3 = inflater.inflate(R.layout.vp_listen_lrc, null);
        civ=page2.findViewById(R.id.circle_image);
        tv_artist=page2.findViewById(R.id.tv_listen_artist);
        singlelinelrc=page2.findViewById(R.id.lrc_view_single);
        lrcview=page3.findViewById(R.id.lrc_view_full);

        lrcview.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
            @Override
            public void onPlay(long time, String content) {
                Intent intent = new Intent(con, PlayService.class);
                intent.putExtra("flag", PlayService.FLAG_PROGRESS);
                intent.putExtra("progress", (int)time);
                startService(intent);
            }
        });
        singlelinelrc.setOnPlayIndicatorLineListener(new LrcView.OnPlayIndicatorLineListener() {
            @Override
            public void onPlay(long time, String content) {
                Intent intent = new Intent(con, PlayService.class);
                intent.putExtra("flag", PlayService.FLAG_PROGRESS);
                intent.putExtra("progress", (int)time);
                startService(intent);
            }
        });
        //
        //添加到集合中
        viewPages.add(page1);
        viewPages.add(page2);
        viewPages.add(page3);

        adapter = new PagerAdapter() {
            //获取当前界面个数
            @Override
            public int getCount() {
                return viewPages.size();
            }

            //判断是否由对象生成页面
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            //移除一个view
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewPages.get(position));
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = viewPages.get(position);
                container.addView(view);
                return view;
            }
        };
        viewPager.setAdapter(adapter);
    }

    //绑定控件
    private void initView() {
        viewPager = findViewById(R.id.vp_listen_data);
        group = findViewById(R.id.viewGroup_indicator);
        tv_title=findViewById(R.id.listen_title_tv);
        tv_current_time=findViewById(R.id.tv_listen_currenttime);
        tv_max_time=findViewById(R.id.tv_listen_maxtime);
        seekbar=findViewById(R.id.listen_seekbar);
        play_pause=findViewById(R.id.iv_listen_play_pause);
        background=findViewById(R.id.listen_background);
        iv_listen_favourite=findViewById(R.id.iv_listen_favourite);
        LinearLayout backbutton = findViewById(R.id.back);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        seekbar.setOnSeekBarChangeListener(this);
        //设置监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面滑动是执行
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            //页面滑动完成后执行
            @Override
            public void onPageSelected(int position) {
                //判断当前是在那个page，就把对应下标的ImageView原点设置为选中状态的图片
                for (int i = 0; i < imageViews.length; i++) {
                    imageViews[position].setBackgroundResource(R.mipmap.icon_listen_point_select);
                    if (position != i) {
                        imageViews[i].setBackgroundResource(R.mipmap.icon_listen_point_unselect);
                    }
                }
            }

            //监听页面的状态，0--静止 1--滑动  2--滑动完成

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //根据页面的个数动态添加圆点指示器
    private void initPointer() {
        //有多少个界面就new多长的数组
        imageViews = new ImageView[viewPages.size()];
        for (int i = 0; i < imageViews.length; i++) {
            //new新的ImageView控件
            imageView = new ImageView(this);
            //设置控件的宽高
            imageView.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
            //设置控件的padding属性
            imageView.setPadding(20, 0, 20, 0);
            imageViews[i] = imageView;
            //初始化第一个page页面的图片的原点为选中状态
            if (i == 1) {
                //表示当前图片
                imageViews[i].setBackgroundResource(R.mipmap.icon_listen_point_select);
                /**
                 * 在java代码中动态生成ImageView的时候
                 * 要设置其BackgroundResource属性才有效
                 * 设置ImageResource属性无效
                 */
            } else {
                imageViews[i].setBackgroundResource(R.mipmap.icon_listen_point_unselect);
            }
            //把new出来的ImageView控件添加到线性布局中
            group.addView(imageViews[i]);
        }
    }
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    private Drawable getForegroundDrawable(Drawable drawable) {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        final float widthHeightSize = (float) (DisplayUtils.getScreenWidth(this)
                *1.0 / DisplayUtils.getScreenHeight(this) * 1.0);

        Bitmap bitmap = drawableToBitmap(drawable);
        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
         int cropBitmapX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

        /*切割部分图片*/
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapX, 0, cropBitmapWidth,
                bitmap.getHeight());
        /*缩小图片*/
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, bitmap.getWidth() / 10, bitmap
                .getHeight() / 10, false);
        /*模糊化*/
        final Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 3, true);

        final Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }

    private void try2UpdateMusicPicBackground(final Drawable drawable) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           //background.setBackgroundDrawable(getForegroundDrawable(drawable));
                           //background.setBackground(createBlurredImageFromBitmap(drawableToBitmap(drawable),3));
                                startChangeAnimation(background,createBlurredImageFromBitmap(drawableToBitmap(drawable),4));
                        }
                    });
                }
            }).start();
    }
    private void try2UpdateMusicUrlPicBackground(final String imageUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadImage(imageUrl);
                    }
                });
            }
        }).start();
    }
    SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
        @Override
        public void onResourceReady(Drawable resource, com.bumptech.glide.request.transition.Transition<? super Drawable> transition) {
            startChangeAnimation(background,createBlurredImageFromBitmap(drawableToBitmap(resource),3));

        }
    };

    public void loadImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl)
                .into(simpleTarget);
    }
    public static Drawable createBlurredImageFromBitmap(Bitmap bitmap, int inSampleSize) {

        RenderScript rs = RenderScript.create(con);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        byte[] imageInByte = stream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
        Bitmap blurTemplate = BitmapFactory.decodeStream(bis, null, options);

        final Allocation input = Allocation.createFromBitmap(rs, blurTemplate);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(15f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurTemplate);
        final Drawable foregroundDrawable= new BitmapDrawable(con.getResources(), blurTemplate);
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }

    public static void startChangeAnimation(LinearLayout layout, Drawable bitmapDrawable) {
       // LinearLayout layout;
       // Drawable drawable=layout.getBackground();
        Drawable oldDrawable = layout.getBackground();
        Drawable oldBitmapDrawable = null;
        if (oldDrawable == null) {
            oldBitmapDrawable = new ColorDrawable(Color.TRANSPARENT);
        } else if (oldDrawable instanceof TransitionDrawable) {
            oldBitmapDrawable = ((TransitionDrawable) oldDrawable).getDrawable(1);
        } else {
            oldBitmapDrawable = oldDrawable;
        }
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                oldBitmapDrawable,
                bitmapDrawable
        });
        layout.setBackground(td);
        td.startTransition(1000);
    }


    /**
     * 颜色渐变动画
     */
    public static void startColorAnimation(View mView, int newColor) {
        int olderColor = ((ColorDrawable) mView.getBackground()).getColor();
        ObjectAnimator objectAnimator;
        objectAnimator = ObjectAnimator.ofInt(mView,
                "backgroundColor", olderColor, newColor)
                .setDuration(800);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.start();
    }


    // 开始/暂停播放
    public void play(View view) {
        startServicce(PlayService.FLAG_PlAY_PAUSE);
    }

    // 上一曲
    public void previous(View view) {
        startServicce(PlayService.FLAG_PREVIOUS);
       // play_pause.setImageResource(R.drawable.icon_listen_pause);
        MusicBaseInfo.CurrentMusicIndex--;
        civ.start();
    }
    public void addtofav(View view) {
       // BmobApi.UpLoadData();
        if(!MusicBaseInfo.Currentmusiclist.get(bmobid).musicid.equals("local"))
        {
            Music bmc=MusicBaseInfo.Currentmusiclist.get(bmobid);
            BmobApi.UpLoadData(bmc.artist+"@_@"+bmc.title+"@_@"+bmc.musicbmpUri+"@_@"+bmc.path+"@_@"+bmc.musicid+"@_@"+bmc.from,"music");
            Toast.makeText(MusicPlayActivity.this, "收藏成功",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MusicPlayActivity.this, "本地歌曲，无需收藏",Toast.LENGTH_LONG).show();

        }

    }
    // 下一曲
    public void next(View view) {
        startServicce(PlayService.FLAG_NEXT);
        MusicBaseInfo.CurrentMusicIndex++;
        civ.start();
    }
    // 初始化广播接收器
    private void initReceiver() {
        format = new SimpleDateFormat("mm:ss");

        receiver = new MusicPlayActivity.ProgressReceiver();
        IntentFilter filter = new IntentFilter(PlayService.ACTION_PROGRESS);
        registerReceiver(receiver, filter);

        maxReceiver = new MusicPlayActivity.MaxReceiver();
        IntentFilter maxFilter = new IntentFilter(PlayService.ACTION_MAX);
        registerReceiver(maxReceiver, maxFilter);

        idReceiver=new MusicPlayActivity.MusicIdReceiver();
        IntentFilter idFilter=new IntentFilter(PlayService.ACTION_MUSIC_ID);
        registerReceiver(idReceiver, idFilter);

        statereceiver=new MusicPlayActivity.MusicStateReceiver();
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
    private   class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 接收当前进度，更新SeekBar的进度
            int progress = intent.getIntExtra("progress", 0);
            seekbar.setProgress(progress);
            lrcview.updateTime(progress);
            singlelinelrc.updateTime(progress);
           String curTime = format.format(new Date(progress));
            tv_current_time.setText(curTime);

        }
    }
    // 接收最大进度的广播接收器
    private  class MaxReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int max = intent.getIntExtra("max", 0);
            String maxTime = format.format(new Date(max));
            tv_max_time.setText(maxTime);
            seekbar.setMax(max);
        }
    }
    private  class MusicStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state=intent.getIntExtra("state",-1);
            if (state==1)
            {
                play_pause.setImageResource(R.drawable.icon_listen_pause);
                civ.start();

            }
            if(state==0)
            {
                play_pause.setImageResource(R.drawable.icon_listen_play);
                civ.stop();
            }

        }

    }


    private static final class LrcHandler extends Handler {
        WeakReference<MusicPlayActivity> mMainActivityWeakReference;

        public LrcHandler(MusicPlayActivity mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           // Bundle data = msg.getData();
            //String val = data.getString("value");
            String val = (String)msg.obj;

            List<Lrc> lrcs = LrcHelper.parseLrcFromString(val);

            //
            // TODO: 更新界面
            lrcview.setLrcData(lrcs);
            singlelinelrc.setLrcData(lrcs);

        }
    }
    final LrcHandler handler=new LrcHandler(this);
    private  class MusicIdReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            //获取editor对象
            final int id=intent.getIntExtra("index", 0);
            bmobid=id;
            tv_title.setText(MusicBaseInfo.Currentmusiclist.get(id).title);
            tv_artist.setText("——   "+ MusicBaseInfo.Currentmusiclist.get(id).artist+"   ——");
            civ.setnewRotateDegrees();//旋转角度重新计算
            civ.setCoverURL(MusicBaseInfo.Currentmusiclist.get(id).musicbmpUri);
               Runnable runnable = new Runnable(){
                @Override
                public void run() {
                    //
                    // TODO: http request.
                    //

                    Message msg = new Message();
                   // Bundle data = new Bundle();
                    if (MusicBaseInfo.Currentmusiclist.get(id).musicid.equals("local"))
                    {
                       // data.putString("value",MusicApi.getLrc(MusicBaseInfo.Currentmusiclist.get(id).title+MusicBaseInfo.Currentmusiclist.get(id).artist));
                        msg.obj= MusicApi.getLrc(MusicBaseInfo.Currentmusiclist.get(id).title+ MusicBaseInfo.Currentmusiclist.get(id).artist);

                    }
                    else
                    {
                        //data.putString("value",MusicApi.getLrcbyID(MusicBaseInfo.Currentmusiclist.get(id).musicid,MusicBaseInfo.Currentmusiclist.get(id).from));
                        msg.obj=MusicApi.getLrcbyID(MusicBaseInfo.Currentmusiclist.get(id).musicid, MusicBaseInfo.Currentmusiclist.get(id).from);
                        //Log.i("1234567889", "onReceive: "+msg.obj);

                    }
                  //  msg.setData(data);
                    handler.sendMessage(msg);
                }
            };
            new Thread(runnable).start();
            // setBlurBackground(drawableToBitmap(MainActivity.mainmusiclist.get(id).albumbmp));

            try2UpdateMusicUrlPicBackground(MusicBaseInfo.Currentmusiclist.get(id).musicbmpUri);



        }
    }

}