package com.example.xieyo.roam.bookactivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.xieyo.roam.BaseActivity;
import com.example.xieyo.roam.MyAdapter.RatingviewAdapter;
import com.example.xieyo.roam.R;
import com.example.xieyo.roam.tools.ratingview;
import com.example.xieyo.roam.view.SpacesItemDecoration;

public class BillboardActivity extends BaseActivity {
    WebView webview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_billboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        webview = findViewById(R.id.webview_billboard);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webview.loadUrl("https://book.douban.com/annual/2018?source=navigation");

    }

    @Override
    protected void onDestroy() {

        if (webview != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，
            webview.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webview.clearHistory();
            webview.removeAllViews();
            webview.destroy();

        }
        super.onDestroy();
    }
}
