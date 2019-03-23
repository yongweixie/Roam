package com.example.xieyo.roam.movieactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.baseinfo.MovieBaseInfo;
import com.example.xieyo.roam.R;

public class FindMovieEntrance extends BaseActivity {
    private static SearchView mSearchView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_find_entrance);
        TextView headertext=findViewById(R.id.header_text);

        headertext.setText("分类找电影");
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

        String tag[]={"豆瓣 Top250","IMDB Top250"};
        String tag1[]={"冷门佳片","豆瓣高分","经典电影"};
        String tag2[]={"喜剧","动作","爱情","科幻","动画","纪录片","悬疑","犯罪","奇幻","歌舞","同性"};
        String tag3[]={"中国大陆","美国","香港","日本","韩国","台湾","英国","法国","德国"};
        String tag4[]={"青春","治愈","文艺","女性","小说改编","超级英雄","美食","宗教","励志"};


        TagView tagGroup=findViewById(R.id.tag_group);
        TagView tagGroup1=findViewById(R.id.tag_group1);
        TagView tagGroup2=findViewById(R.id.tag_group2);
        TagView tagGroup3=findViewById(R.id.tag_group3);
        TagView tagGroup4=findViewById(R.id.tag_group4);//

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
        for (int i=0;i<3;i++)
        {
            mtag=new Tag(tag1[i]);
            mtag.id=i;
            mtag.tagTextColor= ContextCompat.getColor(this, R.color.white);
            mtag.radius=40;
            mtag.tagTextSize=14.5F;
            mtag.layoutColor=ContextCompat.getColor(this, R.color.alpha_5_white);
            tagGroup1.addTag(mtag);

        }
        for (int i=0;i<11;i++)
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
        for (int i=0;i<9;i++)
        {
            mtag=new Tag(tag4[i]);
            mtag.id=i;
            mtag.tagTextColor= ContextCompat.getColor(this, R.color.white);
            mtag.radius=40;
            mtag.tagTextSize=14.5F;
            mtag.layoutColor=ContextCompat.getColor(this, R.color.alpha_5_white);
            tagGroup4.addTag(mtag);
        }

        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                //SearchActivity.setSearchViewtext(tag.text);
              //  BookBaseInfo. findtag=tag.text;
                if(tag.text.equals("豆瓣 Top250"))
                {
                    MovieBaseInfo.findtag=tag.text;
                    Intent intent=new Intent(getApplicationContext(),MovieListbyTag.class);
                    startActivity(intent);
                }
                if(tag.text.equals("IMDB Top250"))
                {
                    Intent intent=new Intent(getApplicationContext(),GetIMDB.class);
                    startActivity(intent);
                }

               // Intent intent=new Intent(getApplicationContext(),BookListbyTag.class);
               // startActivity(intent);
            }
        });

        tagGroup1.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                MovieBaseInfo.findtag=tag.text;

                Intent intent=new Intent(getApplicationContext(),MovieListbyTag.class);
                startActivity(intent);
            }
        });

        tagGroup2.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                MovieBaseInfo. findtag=tag.text;
                Intent intent=new Intent(getApplicationContext(),MovieListbyTag.class);
                startActivity(intent);
            }
        });

        tagGroup3.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                MovieBaseInfo. findtag=tag.text;
                Intent intent=new Intent(getApplicationContext(),MovieListbyTag.class);
                startActivity(intent);
            }
        });

        tagGroup4.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                MovieBaseInfo. findtag=tag.text;
                Intent intent=new Intent(getApplicationContext(),MovieListbyTag.class);
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
//        mSearchView.setFocusable(true);
//        mSearchView.setFocusableInTouchMode(true);
//        //mSearchView.requestFocusFromTouch();
//        mSearchView.requestFocus();
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
        //搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setIconified(false);
            }
        });
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
                finish();
                MovieBaseInfo.searchtext=s;
                Intent intent =new Intent(getApplication(),MovieSearch.class);
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
