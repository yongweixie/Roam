package com.example.xieyo.roam.bookactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.baseinfo.BookBaseInfo;
import com.example.xieyo.roam.R;


public class FindBookEntrance extends BaseActivity{
    private static SearchView mSearchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_find_entrance);
        LinearLayout backbutton=findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        InitTag();
        InitSearchView();
    }

    public void InitTag()
    {

        String tag[]={"影视原著","名著经典"};
        String tag1[]={"文学","诗歌","小说","散文","戏剧","传记","哲学","历史","艺术"};
        String tag2[]={"心理","教育","生活","摄影","绘画","语言","旅行","家居","美食"};
        String tag3[]={"建筑","法律","设计","科技","互联网","计算机","经济","管理","广告"};
        String tag4[]={"互联网","编程","交互设计","算法","科技","神经网络","程序","通信","web"};
        String tag5[]={"漫画","推理","绘本","科幻","悬疑","东野圭吾","金庸","武侠","三毛"};


        TagView tagGroup=findViewById(R.id.tag_group);
        TagView tagGroup1=findViewById(R.id.tag_group1);
        TagView tagGroup2=findViewById(R.id.tag_group2);
        TagView tagGroup3=findViewById(R.id.tag_group3);
        TagView tagGroup4=findViewById(R.id.tag_group4);
        TagView tagGroup5=findViewById(R.id.tag_group5);

        Tag mtag;

        for (int i=0;i<2;i++)
        {
            mtag=new Tag(tag[i]);
            mtag.id=i;
            mtag.tagTextColor= ContextCompat.getColor(this, R.color.white);
            mtag.radius=40;
            mtag.tagTextSize=14.5F;
            mtag.layoutColor=ContextCompat.getColor(this, R.color.alpha_5_white);
            tagGroup.addTag(mtag);
        }
        for (int i=0;i<9;i++)
        {
            mtag=new Tag(tag1[i]);
            mtag.id=i;
            mtag.tagTextColor= ContextCompat.getColor(this, R.color.white);
            mtag.radius=40;
            mtag.tagTextSize=14.5F;
            mtag.layoutColor=ContextCompat.getColor(this, R.color.alpha_5_white);
            tagGroup1.addTag(mtag);

        }
        for (int i=0;i<9;i++)
        {
            mtag=new Tag(tag2[i]);
            mtag.id=i;
            mtag.tagTextColor= ContextCompat.getColor(this, R.color.white);
            mtag.radius=40;
            mtag.tagTextSize=14.5F;
            mtag.layoutColor=ContextCompat.getColor(this, R.color.alpha_5_white);
            tagGroup2.addTag(mtag);

        }
        for (int i=0;i<9;i++)
        {
            mtag=new Tag(tag3[i]);
            mtag.id=i;
            mtag.tagTextColor= ContextCompat.getColor(this, R.color.white);
            mtag.radius=40;
            mtag.tagTextSize=14.5F;
            mtag.layoutColor=ContextCompat.getColor(this, R.color.alpha_5_white);
            tagGroup3.addTag(mtag);
        }
        for (int i=0;i<8;i++)
        {
            mtag=new Tag(tag4[i]);
            mtag.id=i;
            mtag.tagTextColor= ContextCompat.getColor(this, R.color.white);
            mtag.radius=40;
            mtag.tagTextSize=14.5F;
            mtag.layoutColor=ContextCompat.getColor(this, R.color.alpha_5_white);
            tagGroup4.addTag(mtag);
        }
        for (int i=0;i<9;i++)
        {
            mtag=new Tag(tag5[i]);
            mtag.id=i;
            mtag.tagTextColor= ContextCompat.getColor(this, R.color.white);
            mtag.radius=40;
            mtag.tagTextSize=14.5F;
            mtag.layoutColor=ContextCompat.getColor(this, R.color.alpha_5_white);
            tagGroup5.addTag(mtag);
        }
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                //SearchActivity.setSearchViewtext(tag.text);
                BookBaseInfo. findtag=tag.text;

                Intent intent=new Intent(getApplicationContext(),BookListbyTag.class);
                startActivity(intent);
            }
        });

        tagGroup1.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                BookBaseInfo. findtag=tag.text;

                Intent intent=new Intent(getApplicationContext(),BookListbyTag.class);
                startActivity(intent);
            }
        });

        tagGroup2.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                BookBaseInfo. findtag=tag.text;
                Intent intent=new Intent(getApplicationContext(),BookListbyTag.class);
                startActivity(intent);
            }
        });

        tagGroup3.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                BookBaseInfo. findtag=tag.text;
                Intent intent=new Intent(getApplicationContext(),BookListbyTag.class);
                startActivity(intent);
            }
        });

        tagGroup4.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                BookBaseInfo. findtag=tag.text;
                Intent intent=new Intent(getApplicationContext(),BookListbyTag.class);
                startActivity(intent);
            }
        });

        tagGroup5.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                BookBaseInfo. findtag=tag.text;
                Intent intent=new Intent(getApplicationContext(),BookListbyTag.class);
                startActivity(intent);
            }
        });
    }

    private void InitSearchView()
    {
        mSearchView = findViewById(R.id.searchview);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.onActionViewExpanded();
        //设置搜索框展开时是否显示提交按钮，可不显示
        mSearchView.setSubmitButtonEnabled(false);
        //让键盘的回车键设置成搜索
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //搜索框是否展开，false表示展开
        mSearchView.setIconified(true);
        //获取焦点
        mSearchView.setFocusable(true);
        mSearchView.setFocusableInTouchMode(true);
        //mSearchView.requestFocusFromTouch();
        mSearchView.requestFocus();
        //设置提示词
        mSearchView.setQueryHint("请输入要搜索的书名");
        //设置输入框文字颜色
        EditText editText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.alpha_50_white));
        editText.setTextColor(ContextCompat.getColor(this, R.color.white));

        mSearchView.findViewById(android.support.v7.appcompat.R.id.search_plate).setBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
        setSearchvVewListener();
    }

    private void setSearchvVewListener() {

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setIconified(false);
            }
        });

        //搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // Toast.makeText(NextActivity.this, "Close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(NextActivity.this, "Open", Toast.LENGTH_SHORT).show();
            }
        });
        //搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                BookBaseInfo.searchtext=s;
                Intent intent=new Intent(getApplication(),BookSearch.class);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //  LogUtil.e(NextActivity.class, "TextChange --> " + s);
                return false;
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchView.setIconified(true);
    }
}
