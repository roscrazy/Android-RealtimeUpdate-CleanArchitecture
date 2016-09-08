package com.mike.feed.viewmodel;

import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.mike.feed.domain.Deleted;
import com.mike.feed.domain.FeedChangedInfo;
import com.mike.feed.domain.interactor.DefaultSubscriber;
import com.mike.feed.domain.interactor.DeleteFeedUseCase;
import com.mike.feed.domain.interactor.DeleteFeedUseCaseFactory;
import com.mike.feed.domain.interactor.FeedChangedUseCase;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.FeedChangedInfoModel;
import com.mike.feed.model.FeedModel;
import com.mike.feed.view.adapter.FeedAdapter;

import java.util.List;

/**
 * View model for the MainFragment
 */
public class MainViewModel extends SubscriptionViewModel implements ViewModel, FeedAdapter.OnDeleteClicked {

    private static final String TAG = "MainViewModel";


    public interface DataListener {

        void removeItem(int index);

        void moveItem(int oldIndex, int newIndex);

        void updateItem(FeedModel data, int index);

        void addItem(FeedModel data, int index);

        void showErrorMsg();
    }




    private ObservableInt mRecyclerViewVisibility;


    @NonNull
    private final FeedChangedUseCase mFeedChangedUseCase;

    @NonNull
    private final FeedModelMapper mMapper;

    @NonNull
    private DeleteFeedUseCaseFactory mDeleteFeedUseCaseFactory;


    /**
     * Should use another collection which support searching.
     */
    @NonNull
    private List<String> mKeyStore;


    @NonNull
    private DataListener mDataListener;


    public MainViewModel(@NonNull DataListener dataListener,  @NonNull List<String> keyStore
            , @NonNull DeleteFeedUseCaseFactory deleteFeedUseCaseFactory
            , @NonNull FeedChangedUseCase feedChangedUseCase
            , @NonNull FeedModelMapper mapper) {

        this.mDataListener = dataListener;
        this.mKeyStore = keyStore;
        this.mDeleteFeedUseCaseFactory = deleteFeedUseCaseFactory;
        this.mMapper = mapper;
        this.mFeedChangedUseCase = feedChangedUseCase;
        mRecyclerViewVisibility = new ObservableInt(View.INVISIBLE);
    }

    public void init(){
        this.mFeedChangedUseCase.execute(new FeedChangedSubscriber());
        unsubscribeOnUnbindView(mFeedChangedUseCase);
    }



    @Override
    public void onDeleteClicked(FeedModel feed, int index) {
        String key = mKeyStore.remove(index);

        DeleteFeedUseCase deleteFeedUseCase = mDeleteFeedUseCaseFactory.create(key);
        deleteFeedUseCase.execute(new DeleteFeedSubscriber());
        unsubscribeOnUnbindView(deleteFeedUseCase);
    }

    @Override
    public void destroy() {
        super.destroy();
        mDataListener = null;

    }


    void handleError(Throwable throwable){
        mDataListener.showErrorMsg();
    }

    void handleFeedChanged(FeedChangedInfoModel feedChangedInfo){
        switch (feedChangedInfo.getType()){
            case Added: {
                int index = 0;
                if (feedChangedInfo.getPreviousChildKey() != null) {
                    index = getIndexForKey(feedChangedInfo.getPreviousChildKey()) + 1;
                }
                mDataListener.addItem(feedChangedInfo.getFeed(), index);
                mKeyStore.add(index, feedChangedInfo.getKey());
                break;
            }
            case Removed: {
                int index = 0;

                if (feedChangedInfo.getKey() != null) {
                    index = getIndexForKey(feedChangedInfo.getKey());
                }
                mKeyStore.remove(index);
                mDataListener.removeItem(index);
                break;
            }
            case Moved: {
                int oldIndex = getIndexForKey(feedChangedInfo.getKey());

                int newIndex = feedChangedInfo.getPreviousChildKey() == null
                        ? 0 : (getIndexForKey(feedChangedInfo.getPreviousChildKey()) + 1);

                mKeyStore.remove(oldIndex);
                mKeyStore.add(newIndex, feedChangedInfo.getKey());

                mDataListener.moveItem(oldIndex, newIndex);
                break;
            }
            case Changed:
                int index = getIndexForKey(feedChangedInfo.getKey());
                mDataListener.updateItem(feedChangedInfo.getFeed(), index);
                break;
        }


    }


    public int getIndexForKey(String key) {
        int index = mKeyStore.indexOf(key);
        if(index == -1)
            throw new IllegalArgumentException("Key not found");
        return index;
    }




    @RxLogSubscriber
    protected final class DeleteFeedSubscriber extends DefaultSubscriber<Deleted> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            handleError(e);
        }

        @Override
        public void onNext(Deleted feedChangedInfo) {
            // do nothing
        }
    }


    @RxLogSubscriber
    protected final class FeedChangedSubscriber extends DefaultSubscriber<FeedChangedInfo> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            handleError(e);
        }

        @Override
        public void onNext(FeedChangedInfo feedChangedInfo) {
            FeedChangedInfoModel model = mMapper.transform(feedChangedInfo);
            handleFeedChanged(model);
        }
    }



}
