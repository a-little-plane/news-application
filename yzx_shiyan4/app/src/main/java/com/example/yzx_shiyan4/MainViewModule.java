package com.example.yzx_shiyan4;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

//定义ViewModule类的内容，NewsItem合并后的父类
public class MainViewModule extends ViewModel {
    private MutableLiveData<List<NewsItem>> bookList;
    private MutableLiveData<String> errMessage=new MutableLiveData<>();

    public MainViewModule(){
        bookList=new MutableLiveData<>();
        bookList.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<NewsItem>> getBookList() {
        return bookList;
    }

    public MutableLiveData<String> getErrMessage() {
        return errMessage;
    }

}
