package com.anilerkut.newapplication;

import com.anilerkut.newapplication.model.NewsModel;

import java.util.List;

public interface OnFetchDataListener<NewsApiResponse> {

    void onFetchData(List<NewsModel> list,String message);
    void onError(String message);
}
