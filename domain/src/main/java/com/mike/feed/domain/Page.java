package com.mike.feed.domain;

import java.util.List;

/**
 * Created by MinhNguyen on 8/24/16.
 */
public class Page {
    private int total;
    private int currentPage;

    private List<Feed> feeds;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }
}
