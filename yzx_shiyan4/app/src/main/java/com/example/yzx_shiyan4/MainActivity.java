package com.example.yzx_shiyan4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_PAGE = 1;
    private static final String KEY_DATA = "key_data";
    List<String> urlList;
    SwipeRefreshLayout refreshLayout=findViewById(R.id.refreshLayout);
    int page_i=0;
    private MainViewModule viewModule=new ViewModelProvider(this).get(MainViewModule.class);;
    private DatabaseHelper dbHelper= new DatabaseHelper(MainActivity.this);
    private ListView lv=findViewById(R.id.listView);
    private NewsItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yzx_main);

        iniUrlList();

        MutableLiveData<List<NewsItem>> bookList =viewModule.getBookList();
        MutableLiveData<String> errMessage = viewModule.getErrMessage();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateWebPage();
            }
        });

        //点击进入详情页
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem item = (NewsItem) lv.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, NewsItemActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(KEY_DATA,item);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        updateWebPage();

        //显示listview，listview初始化
        bookList.observe(this, new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(List<NewsItem> NewsItems) {
                refreshLayout.setRefreshing(false);

                adapter = new NewsItemAdapter(MainActivity.this, NewsItems);
                lv.setAdapter(adapter);

                // 将更新后的数据存储到数据库中
                DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
                for (NewsItem item : NewsItems) {
                    dbHelper.insertData(item);
                }
            }
        });

        errMessage.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                refreshLayout.setRefreshing(false);
                showToast(s);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.opt_menu, menu);

        // 获取搜索框
        MenuItem searchItem = menu.findItem(R.id.opt_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // 设置搜索框的监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 执行模糊查询并转换为 NewsItem 列表
//                adapter.swapCursor(cursor);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void updateWebPage() {
        refreshLayout.setRefreshing(true);
        String url = urlList.get(page_i);
        new newsItemGetThread(viewModule,url).start();
    }

    //初始化新闻页面的 URL 列表
    private void iniUrlList() {
        urlList=new ArrayList<>();
        String url=String.format("https://m.thepaper.cn/");
        urlList.add(url);
    }

    public static NewsItem getIntentBookItem(Intent intent){
        Bundle b=intent.getExtras();
        NewsItem item=(NewsItem) b.getSerializable(KEY_DATA);
        return item;
    }

    private void showToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }


}