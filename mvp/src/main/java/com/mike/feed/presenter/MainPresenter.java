package com.mike.feed.presenter;

import android.support.annotation.NonNull;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.domain.Deleted;
import com.mike.feed.domain.FeedChangedInfo;
import com.mike.feed.domain.interactor.DefaultSubscriber;
import com.mike.feed.domain.interactor.DeleteFeedUseCase;
import com.mike.feed.domain.interactor.DeleteFeedUseCaseFactory;
import com.mike.feed.domain.interactor.FeedChangedUseCase;
import com.mike.feed.domain.interactor.UseCase;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.FeedChangedInfoModel;
import com.mike.feed.model.FeedModel;
import com.mike.feed.view.MainView;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 8/25/16.
 */
@FragmentScope
public class MainPresenter extends BasePresenter<MainView> {

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


    public MainPresenter(FeedChangedUseCase feedChangedUseCase, FeedModelMapper mapper, DeleteFeedUseCaseFactory deleteFeedUseCaseFactory, List<String> keyStore) {
        this.mFeedChangedUseCase = feedChangedUseCase;
        this.mMapper = mapper;
        this.mKeyStore = keyStore;
        this.mDeleteFeedUseCaseFactory = deleteFeedUseCaseFactory;
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

    private int getIndexForKey(String key) {
        int index = mKeyStore.indexOf(key);
        if(index == -1)
            throw new IllegalArgumentException("Key not found");
        return index;
    }

    public void deleteFeed(FeedModel feedModel, int index){
        if(index >= mKeyStore.size() || index < 0)
            throw new IllegalArgumentException(String.format("index = %d out of the bound", index));

        String key = mKeyStore.get(index);

        DeleteFeedUseCase deleteFeedUseCase = mDeleteFeedUseCaseFactory.create(key);
        deleteFeedUseCase.execute(new DeleteFeedSubscriber(deleteFeedUseCase));
        unsubscribeOnUnbindView(deleteFeedUseCase);
    }



    protected final class DeleteFeedSubscriber extends DefaultSubscriber<Deleted> {

        WeakReference<UseCase> mUseCase;

        public DeleteFeedSubscriber(UseCase useCase) {
            this.mUseCase = new WeakReference<UseCase>(useCase);
        }

        @Override
        public void onCompleted() {
            removeUnsubscribe(mUseCase.get());
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            handleError(e);
            removeUnsubscribe(mUseCase.get());
        }

        @Override
        public void onNext(Deleted feedChangedInfo) {
            // do nothing
        }
    }



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
