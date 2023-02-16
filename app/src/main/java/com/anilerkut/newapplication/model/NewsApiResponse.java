package com.anilerkut.newapplication.model;

import java.io.Serializable;
import java.util.List;

public class NewsApiResponse implements Serializable {

    String status;
    int totalResults;
    List<NewsModel> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<NewsModel> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsModel> articles) {
        this.articles = articles;
    }
}
