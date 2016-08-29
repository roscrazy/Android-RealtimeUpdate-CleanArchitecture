package com.mike.feed.view.base;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.mike.feed.R;

public class BaseHostActivity extends BaseActivity{


    protected int getContentResId() {
        return R.layout.activity_host_fragment;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
    }


    @Override
    protected void setUpComponent() {
    }



    protected void replace(BaseFragment fragment, String tag) {
        replace(fragment, tag, false, false);


    }

    protected void replace(BaseFragment fragment, String tag, boolean anim, boolean addBackStack) {
        if (fragment != null && getFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (anim)
                addAnimation(ft);

            ft.replace(R.id.flHostFragment, fragment, tag);

            if (addBackStack)
                ft.addToBackStack(tag);

            ft.commit();
        }
    }

    protected void add(BaseFragment fragment, String tag) {
        add(fragment, tag, false, false);
    }

    protected void add(BaseFragment fragment, String tag, boolean anim, boolean addBackStack) {
        if (fragment != null && getFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            if (anim)
                addAnimation(ft);

            ft.add(R.id.flHostFragment, fragment, tag);

            if (addBackStack)
                ft.addToBackStack(tag);

            ft.commit();
        }
    }

    protected void addAnimation(FragmentTransaction transaction){
        // add default animation
    }







}
