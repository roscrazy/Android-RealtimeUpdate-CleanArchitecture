package com.mike.feed.viewmodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.mike.feed.R;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.domain.Feed;
import com.mike.feed.domain.Written;
import com.mike.feed.domain.interactor.DefaultSubscriber;
import com.mike.feed.domain.interactor.NewFeedUseCase;
import com.mike.feed.domain.interactor.NewFeedUseCaseFactory;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.FeedModel;
import com.mike.feed.model.WrittenModel;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 9/8/16.
 */
@FragmentScope
public class NewFeedViewModel extends SubscriptionViewModel implements ViewModel {

    public interface DataListener{

        //I don't know wether it's a good idea that we call this method to notify view to close from VM
        public void close();
    }

    @NonNull
    NewFeedUseCaseFactory mNewFeedUseCaseFactory;

    @NonNull
    private FeedModelMapper mMapper;


    private String mTitle;
    private String mBody;
    private String mImage;

    @NonNull
    private Context mContext;

    @NonNull
    DataListener mDataListener;

    public NewFeedViewModel(Context context, NewFeedUseCaseFactory newFeedUseCaseFactory, FeedModelMapper mapper, DataListener dataListener, FeedModel feedModel) {
        this.mMapper = mapper;
        this.mNewFeedUseCaseFactory =newFeedUseCaseFactory;
        this.mContext = context;
        this.mDataListener = dataListener;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String mBody) {
        this.mBody = mBody;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public void onSubmitClicked(View view){

        Feed feed = new Feed();
        feed.setImage(mImage);
        feed.setBody(mBody);
        feed.setTitle(mTitle);

        NewFeedUseCase newFeedUseCase = mNewFeedUseCaseFactory.create(feed);
        newFeedUseCase.execute(new NewFeedSubscriber());
        unsubscribeOnUnbindView(newFeedUseCase);
    }

    @Override
    public void destroy() {
        super.destroy();
    }


    protected final class NewFeedSubscriber extends DefaultSubscriber<Written> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            e.printStackTrace();
            Toast.makeText(mContext, R.string.error_msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNext(Written written) {
            // can do whatever with WrittenModel here
            WrittenModel writtenModel = mMapper.transform(written);
            mDataListener.close();
        }
    }
}
