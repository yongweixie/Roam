package com.example.xieyo.roam.musicfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.example.xieyo.roam.R;
import com.example.xieyo.roam.musicactivity.LocalMusicActivity;
import com.example.xieyo.roam.musicactivity.RecentMusicActivity;

public class MyMusicFragment extends Fragment  implements OnClickListener {
    private View localview,historyview,favoriteview,downloadview;
    private TextView songnum;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mymusic_fragment, null);
        return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // final User user ;

        localview =  getActivity().findViewById(R.id.localview);
        localview.setOnClickListener(this);
        historyview =  getActivity().findViewById(R.id.historyview);
        historyview.setOnClickListener(this);
        favoriteview =  getActivity().findViewById(R.id.favoriteview);
        favoriteview.setOnClickListener(this);
        downloadview =  getActivity().findViewById(R.id.downloadview);
        downloadview.setOnClickListener(this);
        songnum=  getActivity().findViewById(R.id.musicitem_desc);

}
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.localview:
                //Toast.makeText(getActivity(), "本地歌曲", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(), LocalMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.historyview:
                //Toast.makeText(getActivity(), "播放历史", Toast.LENGTH_LONG).show();
                Intent intent1=new Intent(getActivity(),RecentMusicActivity.class);
                startActivity(intent1);
                break;
            case R.id.favoriteview:
                break;
            case R.id.downloadview:
                break;

        }

    }
}