package com.mike.feed.viewmodel;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;
import android.view.View;

import com.mike.feed.domain.interactor.UseCase;
import com.mike.feed.model.FeedModel;
import com.mike.feed.util.ImageLoader;
import com.mike.feed.view.adapter.FeedAdapter;

/**
 * Created by MinhNguyen on 9/7/16.
 */
public class FeedItemViewModel extends BaseObservable implements ViewModel {

    private FeedModel mFeedModel;
    private FeedAdapter.OnDeleteClicked mOnDeleteClicked;
    private int mPosition;


    public FeedItemViewModel(FeedModel mFeedModel,
                             FeedAdapter.OnDeleteClicked mOnDeleteClicked, int mPosition) {
        this.mFeedModel = mFeedModel;
        this.mOnDeleteClicked = mOnDeleteClicked;
        this.mPosition = mPosition;
    }

    public void onDeleteClicked(View view){
        if(mOnDeleteClicked != null)
            mOnDeleteClicked.onDeleteClicked(mFeedModel, mPosition);
    }

    public String getTitle(){
        return mFeedModel.getTitle();
    }

    public String getBody(){
        return mFeedModel.getBody();
    }



    public void setFeedModel(FeedModel feedModel){
        this.mFeedModel = feedModel;
    }

    @Override
    public void destroy() {
        // do nothing here
    }

}
