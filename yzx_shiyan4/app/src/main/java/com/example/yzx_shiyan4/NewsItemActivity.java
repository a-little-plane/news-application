package com.example.yzx_shiyan4;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

//设置详情页面
public class NewsItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity_main);

        WebView webView=findViewById(R.id.webView);
        NewsItem newsItem = MainActivity.getIntentBookItem(getIntent());

        WebSettings settings = webView.getSettings();

        //启用 WebView 自带的缩放控制，自适应显示
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);  // 启用 DOM 存储（localStorage, sessionStorage）
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setDatabaseEnabled(true);  // 启用数据库存储
        settings.setAppCacheEnabled(true);  // 启用应用缓存

        //设置 WebViewClient，处理加载错误
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WebView", "Loading URL: " + url);  // 打印加载的 URL
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e("WebView", "Error loading URL: " + description);  // 打印错误信息
            }
        });

        // 设置 ActionBar 的标题为新闻标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(newsItem.getTitle());
        }

        //点击进入网站，加载连接
        webView.loadUrl(newsItem.getHref());

//        加载网页链接
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}