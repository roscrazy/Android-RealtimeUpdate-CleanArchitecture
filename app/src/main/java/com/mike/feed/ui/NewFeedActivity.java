package com.mike.feed.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mike.feed.ui.base.BaseHostActivity;

/**
 * Created by MinhNguyen on 8/26/16.
 */
public class NewFeedActivity extends BaseHostActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            NewFeedFragment fragment = NewFeedFragment.createInstance();
            replace(fragment, NewFeedFragment.class.getSimpleName());
        }
    }


}
