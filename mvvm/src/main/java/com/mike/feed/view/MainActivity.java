package com.mike.feed.view;

import android.content.Intent;
import android.os.Bundle;

import com.mike.feed.R;
import com.mike.feed.view.base.BaseHostActivity;

import butterknife.OnClick;

public class MainActivity extends BaseHostActivity {


    @Override
    protected int getContentResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            MainFragment mainFragment = MainFragment.createInstance();
            replace(mainFragment, MainFragment.class.getSimpleName());
        }
    }


    @OnClick(R.id.fabAddNew)
    public void onAddNewClicked(){
        Intent intent = new Intent(this, NewFeedActivity.class);
        startActivity(intent);
    }
}
