package com.example.xieyo.roam.searchfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.baseinfo.MusicBaseInfo;
import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.MyAdapter.MusicListRecyclerAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.service.PlayService;
import com.example.xieyo.roam.musicactivity.SearchActivity;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.musicbean.Music;
import com.example.xieyo.roam.tools.MusicApi;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.example.xieyo.roam.musicactivity.SearchActivity.getSearchViewtext;

public class SearchMusicAllFragment extends LazyFragment  implements BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemChildClickListener,BaseQuickAdapter.RequestLoadMoreListener {


    private RecyclerView ry;
    private static MusicListRecyclerAdapter mAdapter;
    private static List<Music> mList=new ArrayList<Music>();
   private final static SearchMusicAllFragment.Listndler handler=new SearchMusicAllFragment.Listndler(new SearchMusicAllFragment());
    private final static SearchMusicAllFragment.Myplayhandler playhandler=new SearchMusicAllFragment.Myplayhandler(new SearchMusicAllFragment());

    private  static int mfrom=0;
    private static Context con;

    public int setContentView() {
        return R.layout.frag_search_music_all;
    }
    int page=1;
    public void init() {
        ry = rootView.findViewById(R.id.ry_music_search_list_all);
        con=getContext();
        mAdapter=new MusicListRecyclerAdapter(mList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnLoadMoreListener(new SearchMusicAllFragment(),ry);
        mAdapter.openLoadAnimation();
        ry.setLayoutManager(new LinearLayoutManager(getContext()));
        ry.setAdapter(mAdapter);

    }
    private static final class Listndler extends Handler {
        WeakReference<SearchMusicAllFragment> mMainActivityWeakReference;

        public Listndler(SearchMusicAllFragment mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
        }
    }

    private static final class Myplayhandler extends Handler {
        WeakReference<SearchMusicAllFragment> mMainActivityWeakReference;

        private Myplayhandler(SearchMusicAllFragment mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg2==1)
            {
                SearchActivity.frenchlist(mList.get(msg.arg1));
                int index= MusicBaseInfo.Currentmusiclist.size()-1;
                initList(MusicBaseInfo.Currentmusiclist);

                DateBaseUtils.setIndex(index);
                MusicBaseInfo.CurrentMusicIndex=index;

                Intent intent = new Intent(con, PlayService.class);
                intent.putExtra("index", index);
                intent.putExtra("flag", PlayService.FLAG_PLAY);
                con.startService(intent);
                SearchActivity.frenchview(index);
            }
            if(msg.arg2==0)
            {
                Toast.makeText(con, "版权原因暂时无法播放", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void lazyLoad() {
                // TODO: http request.
                //Bundle data = new Bundle()
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK){
                    //Toast.makeText(getActivity(), "按了返回键", Toast.LENGTH_SHORT).show();
//                    cleanData();
//                    mAdapter.notifyDataSetChanged();
                    SearchMusicFragment.setPage(0);
                    return true;
                }
                return false;
            }
        });


    }
    public static void StartAllSearch(final String text)
    {
        int from=SearchMusicPartFragment.getmorefrom();
        mList.clear();
        Music titlelist=new Music();
        titlelist.from=from;
        titlelist.type=3;
        mList.add(titlelist);
        mList.addAll(MusicApi.getSearchList(text,10,1,from));
        mfrom=from;
        mAdapter.notifyDataSetChanged();
        mAdapter.disableLoadMoreIfNotFullPage();

    }
    int currentposition=-1;



    public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {

        // String title = mList.get(position).title;
        if(currentposition!=position&&mList.get(position).type==2)
        {
            Runnable runnable = new Runnable(){
                @Override
                public void run() {
                    // TODO: http request.
                    //Bundle data = new Bundle();
                    if(getRource(mList.get(position).path))
                    {
                        Message msg = new Message();
                        msg.arg1=position;
                        msg.arg2=1;
                        playhandler.sendMessage(msg);
                    }
                    else
                    {
                        Message msg = new Message();
                        msg.arg2=0;
                        playhandler.sendMessage(msg);
                    }

                }
            };
            new Thread(runnable).start();
            currentposition=position;
        }

    }


    private static boolean getRource(String source) {
        try {
            URL url = new URL(source);//创建URL对象。
            URLConnection uc = url.openConnection();//创建一个连接对象。
            InputStream in = uc.getInputStream();//获取连接对象输入字节流。如果地址无效则会抛出异常。
            if (source.equalsIgnoreCase(uc.getURL().toString())) return false;//用于请求地址是否重定向。
            in.close();
            return true;
        } catch (Exception e) {
            System.out.println("截图路径不存在");
            // log.info("截图路径不存在：source={},exception={}", new Object[]{source, e.getStackTrace().toString()});
            return false;
        }
    }
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        if (view.getId()==R.id.viewback)
        {
//            cleanData();
//            mAdapter.notifyDataSetChanged();
            SearchMusicFragment.setPage(0);
        }
    }
    @Override
    public void onLoadMoreRequested() {

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();
                page++;
                Message msg = new Message();
                mList.addAll(MusicApi.getSearchList(getSearchViewtext(),10,page,mfrom));
                handler.sendMessage(msg);
            }
        };
        new Thread(runnable).start();

    }
    private static void initList(List<Music> musiclist ) {

        String[] lists=new String[musiclist.size()];
        for (int i=0;i<musiclist.size();i++)
        {
            lists[i]=musiclist.get(i).path;
        }
        // 播放路径作为参数传递给Service并做相应初始化
        Intent intent = new Intent(con, PlayService.class);
        intent.putExtra("list", lists);
        intent.putExtra("flag", PlayService.FLAG_LOAD_PATH);
        con.startService(intent);
    }
    public static void cleanData()
    {
        mList.clear();
    }
    public void onResume() {
        super.onResume();

    }
}