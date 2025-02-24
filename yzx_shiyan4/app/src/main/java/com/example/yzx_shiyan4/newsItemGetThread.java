package com.example.yzx_shiyan4;

import androidx.lifecycle.MutableLiveData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

//数据获取，存到newsItemGetThread类的viewModule中
public class newsItemGetThread extends Thread{
    private MainViewModule viewModule;
    private String url;

    public newsItemGetThread(MainViewModule viewModule, String url) {
        this.viewModule = viewModule;
        this.url = url;
    }

    @Override
    public void run() {
        MutableLiveData<List<NewsItem>> bookList=viewModule.getBookList();
        MutableLiveData<String> errMessage=viewModule.getErrMessage();

        List<NewsItem> list=bookList.getValue();
        list.clear();

        try{
            Document doc = Jsoup.connect(url).timeout(10000).get();
            Elements dls = doc.select("div[class*=index_wrapper__9rz3z]");
            for(Element div : dls){
                String href= div.select("a").first().attr("abs:href");
                String title = div.select("img").first().attr("alt");
                String time= div.select("div[class=adm-space adm-space-horizontal]").first().text();
                String imgSrc= div.select("img").first().attr("abs:src");
                list.add(new NewsItem(title,time,href,imgSrc));
            }
            bookList.postValue(list);
        }
        catch (IOException e) {
            e.printStackTrace();
            errMessage.postValue(e.toString());
        }

    }
}
