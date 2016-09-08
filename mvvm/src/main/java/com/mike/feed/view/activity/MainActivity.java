package com.mike.feed.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mike.feed.R;
import com.mike.feed.view.fragment.MainFragment;
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


        findViewById(R.id.fabAddNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewFeedActivity.class);
                startActivity(intent);
            }
        });
    }

}
