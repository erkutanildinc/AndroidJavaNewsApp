package com.anilerkut.newapplication;

import com.anilerkut.newapplication.model.NewsModel;

import java.util.List;

public interface onCategoryListener<NewsApiResponse> {

    void onGategoryData(List<NewsModel> list,String message,String category);
    void onError(String message);
}
