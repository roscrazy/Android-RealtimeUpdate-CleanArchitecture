package com.mike.feed.view;

import com.mike.feed.model.FeedModel;

/**
 * Created by MinhNguyen on 8/25/16.
 */
public interface MainView {

    void removeItem(int index);

    void moveItem(int oldIndex, int newIndex);

    void updateItem(FeedModel data, int index);

    void addItem(FeedModel data, int index);

    void showErrorMsg();


}
