package com.example.yzx_shiyan4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.example.yzx_shiyan4.NewsItem;

import java.util.ArrayList;
import java.util.List;

//数据插入数据库
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 2;
    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建新闻表，并指定 _id 作为主键列
        String createTableQuery = "CREATE TABLE IF NOT EXISTS news_items (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // 添加主键列 _id
                "title TEXT, " +
                "time TEXT, " +
                "href TEXT, " +
                "imgSrc TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级时删除原表并重新创建
        db.execSQL("DROP TABLE IF EXISTS news_items");
        onCreate(db);
    }

    // 插入数据时，主键 _id 会自动增长
    public long insertData(NewsItem newsItem) {
        db = this.getWritableDatabase(); // 确保每次插入前初始化数据库连接

        // 插入数据
        ContentValues values = new ContentValues();
        values.put("title", newsItem.getTitle());
        values.put("time", newsItem.getTime());
        values.put("href", newsItem.getHref());
        values.put("imgSrc", newsItem.getImgSrc());

        // 执行插入操作，返回插入行的ID
        long rowId = db.insert("news_items", null, values);

        return rowId;
    }

    // 模糊查询
    public Cursor fuzzyQuery(String match) {
        db = this.getWritableDatabase(); // 确保每次查询前初始化数据库连接
        if (TextUtils.isEmpty(match)) {
            return queryAll(); // 当输入为空时查询所有数据
        }
        String sql = "SELECT * FROM news_items WHERE title LIKE ?";
        String[] args = new String[]{"%" + match + "%"};
        Cursor c = db.rawQuery(sql, args);

        Log.i("TAG", "fuzzyQuery executed: " + c.getCount() + " rows found.");

        return c;
    }


    // 查询所有数据
    public Cursor queryAll() {
        // 查询 news_items 表的所有数据
        String sql = "SELECT * FROM news_items";
        Cursor cursor = db.rawQuery(sql, null);  // 执行查询操作
        return cursor;
    }
}
