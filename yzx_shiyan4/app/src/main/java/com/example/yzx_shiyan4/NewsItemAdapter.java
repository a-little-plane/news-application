package com.example.yzx_shiyan4;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

//将类中list的数据转存到界面中，将爬取的数据进行输出展示，建立listview的view单位
public class NewsItemAdapter extends ArrayAdapter<NewsItem> {
    private Context context;
    private List<NewsItem> list;

    public NewsItemAdapter(@NonNull Context context, List<NewsItem> list) {
        super(context, android.R.layout.simple_list_item_1,list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        if(v==null){
            v= LayoutInflater.from(context).inflate(R.layout.row_view,parent,false);
        }
        NewsItem NewsItem =list.get(position);
        TextView tv_title=v.findViewById(R.id.row_view_title);
        TextView tv_time=v.findViewById(R.id.row_view_time);
        ImageView iv=v.findViewById(R.id.row_view_iv);
        tv_title.setText(NewsItem.getTitle());
        tv_time.setText(NewsItem.getTime());

        Glide.with(context).load(Uri.parse(NewsItem.getImgSrc())).into(iv);
        return v;

    }
    public void updateList(List<NewsItem> newList) {
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }
}
