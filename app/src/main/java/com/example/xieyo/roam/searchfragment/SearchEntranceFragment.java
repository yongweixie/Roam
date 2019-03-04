package com.example.xieyo.roam.searchfragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;



import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.xieyo.roam.LazyFragment;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.musicactivity.SearchActivity;
import com.example.xieyo.roam.tools.MusicApi;


import java.lang.ref.WeakReference;
import java.util.List;


public class SearchEntranceFragment  extends LazyFragment {
    private static TagView tagGroup;
    private static Tag mtag;
    public int setContentView() {
        return R.layout.frag_search_entrance;
    }
    public void init() {

        tagGroup = (TagView)rootView.findViewById(R.id.tag_group);
        //set click listene
//        final List<String> mlist=new ArrayList<>();
//        List<Music> mclist= MusicApi.getMusicfromList("3778678",2);
//        for (int i=0;i<10;i++)
//        {
//            mlist.add( mclist.get(i).title);
//
//        }

        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                // TODO: http request.
                //Bundle data = new Bundle();
                final List<String> mlist=MusicApi.getHotSearch(2);

                for (int i=0;i<10;i++)
                {
                    Message msg = new Message();
                    mtag=new Tag(mlist.get(i));
                    mtag.id=i;
                    mtag.tagTextColor=ContextCompat.getColor(getContext(), R.color.white);
                    mtag.radius=40;
                    mtag.tagTextSize=14.5F;
                    mtag.layoutColor=ContextCompat.getColor(getContext(), R.color.alpha_5_white);
                    msg.obj=mtag;
                    handler.sendMessage(msg);

                }

            }
        };
        new Thread(runnable).start();

        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                SearchActivity.setSearchViewtext(tag.text);
            }
        });

        //set delete listener
        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
            }
        });

//set long click listener
        tagGroup.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(Tag tag, int position) {
            }
        });
    }
    private static final class Listhandler extends Handler {
        WeakReference<SearchEntranceFragment> mMainActivityWeakReference;

        public Listhandler(SearchEntranceFragment mainActivity) {
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            tagGroup.addTag((Tag)msg.obj);
            super.handleMessage(msg);
        }
    }
    final SearchEntranceFragment.Listhandler handler=new SearchEntranceFragment.Listhandler(this);


    @Override
    public void lazyLoad() {

    }


}
