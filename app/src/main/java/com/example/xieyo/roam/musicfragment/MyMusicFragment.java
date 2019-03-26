package com.example.xieyo.roam.musicfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.xieyo.roam.MyAdapter.BottomViewAdapter;
import com.example.xieyo.roam.MyAdapter.FavListAdapter;
import com.example.xieyo.roam.MyAdapter.MusicHallAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.baseinfo.BaseInfo;
import com.example.xieyo.roam.baseinfo.MusicBaseInfo;
import com.example.xieyo.roam.musicactivity.FavMusicActivity;
import com.example.xieyo.roam.musicactivity.LocalMusicActivity;
import com.example.xieyo.roam.musicactivity.MusicListActivity;
import com.example.xieyo.roam.musicactivity.OnlineMusicActivity;
import com.example.xieyo.roam.musicactivity.RecentMusicActivity;
import com.example.xieyo.roam.musicbean.FavList;
import com.example.xieyo.roam.musicbean.Music;
import com.example.xieyo.roam.musicbean.MusicHallList;
import com.example.xieyo.roam.tools.BmobApi;
import com.example.xieyo.roam.tools.DateBaseUtils;
import com.example.xieyo.roam.view.MyMusicItemLayout;
import com.example.xieyo.roam.view.SpacesItemDecoration;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MyMusicFragment extends Fragment  implements   BaseQuickAdapter.OnItemClickListener{
    private View localview,historyview,favoriteview,downloadview;
    private TextView songnum;
    private RecyclerView ry;
    private static FavListAdapter mAdapter;
    private static List<FavList> mList=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mymusic_fragment, null);
        ry = view.findViewById(R.id.ry_fav_musiclist);
        ry.setLayoutManager(new LinearLayoutManager(getContext()));
        ry.addItemDecoration(new SpacesItemDecoration(4));
        mAdapter=new FavListAdapter(R.layout.fav_musiclist, mList);
        mAdapter.setOnItemClickListener(this);

        View headView = getLayoutInflater().inflate(R.layout.mymusic_frag_header, null);
        mAdapter.addHeaderView(headView);
        MyMusicItemLayout local = headView.findViewById(R.id.localview);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), LocalMusicActivity.class);
                startActivity(intent);
            }
        });
        MyMusicItemLayout fav = headView.findViewById(R.id.favoriteview);
        local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), FavMusicActivity.class);
                startActivity(intent);
            }
        });

        ry.setAdapter(mAdapter);

        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // final User user ;

        localview =  getActivity().findViewById(R.id.localview);
        favoriteview =  getActivity().findViewById(R.id.favoriteview);
        songnum=  getActivity().findViewById(R.id.musicitem_desc);

}


    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        MusicBaseInfo.MusicListId=mList.get(position).ID;
        MusicBaseInfo.MusicListImageUrl =mList.get(position).CoverUrl;
        MusicBaseInfo.CurrentMusicListFrom=mList.get(position).from;
        MusicBaseInfo.MusicListTitle=mList.get(position).title;
       // Log.i("123456", "onItemClick: "+mList.get(position).from);

        Intent intent = new Intent(getContext(), OnlineMusicActivity.class);

        getContext().startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        mList.clear();
//        mAdapter=new FavListAdapter(R.layout.fav_musiclist, mList);
//        mAdapter.setOnItemClickListener(this);
//        ry.setAdapter(mAdapter);

        BmobQuery query = new BmobQuery("u" + BaseInfo.account);
        query.addWhereEqualTo("type", "favlist");
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
                            FavList list = new FavList();
                            String get = ary.getJSONObject(i).getString("data");
                            //  BmobApi.UpLoadData(bmc.artist+"@_@"+bmc.title+"@_@"+bmc.musicbmpUri+"@_@"+bmc.path+"@_@"+bmc.musicid+"@_@"+bmc.from,"music");
                            //Log.i("123456", "done: "+get.toString());

                            list.title = get.split("@_@")[1];

                            list.CoverUrl = get.split("@_@")[0];
                            list.ID = get.split("@_@")[3];
                            list.data = "by "+get.split("@_@")[2];
                            list.from=Integer.valueOf(get.split("@_@")[4]) ;
                            mList.add(list);
                            mAdapter.notifyDataSetChanged();

                        }
                    } catch (Exception ex) {

                        Log.i("123456", "done: "+ex.toString());

                    }

                } else {

                }
            }
        });


    }





}