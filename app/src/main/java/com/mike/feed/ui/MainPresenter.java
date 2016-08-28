package com.mike.feed.ui;

import android.support.annotation.NonNull;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.mike.domain.Deleted;
import com.mike.domain.FeedChangedInfo;
import com.mike.domain.executor.PostExecutionThread;
import com.mike.domain.executor.ThreadExecutor;
import com.mike.domain.interactor.DefaultSubscriber;
import com.mike.domain.interactor.DeleteFeedUseCase;
import com.mike.domain.interactor.FeedChangedUseCase;
import com.mike.domain.repository.FeedRepository;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.FeedChangedInfoModel;
import com.mike.feed.model.FeedModel;
import com.mike.feed.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 8/25/16.
 */
@FragmentScope
public class MainPresenter extends BasePresenter<MainView> {


    @NonNull
    private FeedRepository mRepository;
    @NonNull
    private ThreadExecutor mThreadExecutor;
    @NonNull
    private PostExecutionThread mPostExecutionThread;
    @NonNull
    private final FeedChangedUseCase mFeedChangedUseCase;
    @NonNull
    private final FeedModelMapper mMapper;


    DeleteFeedUseCase mDeleteFeedUseCase;

    /**
     * Should use another collection which support searching.
     * Maybe a {@link java.util.HashMap} could be considered. But it will take more memory.
     */
    List<String> mKeyStore;




    @Inject
    public MainPresenter(FeedChangedUseCase feedChangedUseCase, FeedModelMapper mapper, FeedRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.mFeedChangedUseCase = feedChangedUseCase;
        this.mMapper = mapper;

        this.mRepository = repository;
        this.mThreadExecutor = threadExecutor;
        this.mPostExecutionThread = postExecutionThread;

        mKeyStore = new ArrayList<>();
    }

    public void init(){
        mFeedChangedUseCase.execute(new FeedChangedSubscriber());
        unsubscribeOnUnbindView(mFeedChangedUseCase);
    }

    void handleError(Throwable throwable){
        view().showErrorMsg();
    }

    void handleFeedChanged(FeedChangedInfoModel feedChangedInfo){
        switch (feedChangedInfo.getType()){
            case Added: {
                int index = 0;
                if (feedChangedInfo.getPreviousChildKey() != null) {
                    index = getIndexForKey(feedChangedInfo.getPreviousChildKey()) + 1;
                }
                view().addItem(feedChangedInfo.getFeed(), index);
                mKeyStore.add(index, feedChangedInfo.getKey());
                break;
            }
            case Removed: {
                int index = 0;

                if (feedChangedInfo.getKey() != null) {
                    index = getIndexForKey(feedChangedInfo.getKey());
                }
                mKeyStore.remove(index);
                view().removeItem(index);
                break;
            }
            case Moved: {
                int oldIndex = getIndexForKey(feedChangedInfo.getKey());

                int newIndex = feedChangedInfo.getPreviousChildKey() == null
                        ? 0 : (getIndexForKey(feedChangedInfo.getPreviousChildKey()) + 1);

                mKeyStore.remove(oldIndex);
                mKeyStore.add(newIndex, feedChangedInfo.getKey());

                view().moveItem(oldIndex, newIndex);
                break;
            }
            case Changed:
                int index = getIndexForKey(feedChangedInfo.getKey());
                view().updateItem(feedChangedInfo.getFeed(), index);
                break;
        }


    }

    int getIndexForKey(String key) {
        int index = mKeyStore.indexOf(key);
        if(index == -1)
            throw new IllegalArgumentException("Key not found");
        return index;
    }

    void deleteFeed(FeedModel feedModel, int index){
        String key = mKeyStore.get(index);

        /**
         * I am not sure whether it could be a good approach when I init a UseCase in a presenter
         * Presenter should not know anything about FeedRepository, ThreadExecutor, PostExecutionThread, and it's difficult to test whether the UseCase have executed.
         * But for dynamic param to a UseCase, I think it can be an acceptable option.
         * Some people do something like "newFeedUseCase.setFeed(feed)" before executing, but I don't think it's better than this approach.
         * I am more than happy if you could provide me some better approach or your opinion.
         */
        mDeleteFeedUseCase = new DeleteFeedUseCase(key, mRepository, mThreadExecutor, mPostExecutionThread);
        mDeleteFeedUseCase.execute(new DeleteFeedSubscriber());
        unsubscribeOnUnbindView(mDeleteFeedUseCase);

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
