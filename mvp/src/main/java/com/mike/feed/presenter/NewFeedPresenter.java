package com.mike.feed.presenter;

import android.support.annotation.NonNull;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.mike.feed.domain.Feed;
import com.mike.feed.domain.Written;
import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.interactor.DefaultSubscriber;
import com.mike.feed.domain.interactor.NewFeedUseCase;
import com.mike.feed.domain.interactor.NewFeedUseCaseFactory;
import com.mike.feed.domain.repository.FeedRepository;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.WrittenModel;
import com.mike.feed.view.NewFeedView;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 8/26/16.
 */
@FragmentScope
public class NewFeedPresenter extends BasePresenter<NewFeedView> {

    @NonNull
    NewFeedUseCaseFactory mNewFeedUseCaseFactory;

    @NonNull
    private FeedModelMapper mMapper;


    @Inject
    public NewFeedPresenter(NewFeedUseCaseFactory newFeedUseCaseFactory, FeedModelMapper mapper) {
        this.mNewFeedUseCaseFactory = newFeedUseCaseFactory;
        this.mMapper = mapper;
    }

    public void submit(String title, String body, String img) {
        Feed feed = new Feed();
        feed.setBody(body);
        feed.setTitle(title);
        feed.setImage(img);

        NewFeedUseCase newFeedUseCase = mNewFeedUseCaseFactory.create(feed);
        newFeedUseCase.execute(new NewFeedSubscriber());
        unsubscribeOnUnbindView(newFeedUseCase);
    }


    
    protected final class NewFeedSubscriber extends DefaultSubscriber<Written> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            e.printStackTrace();
            view().showErrorMsg();
        }

        @Override
        public void onNext(Written written) {
            // can do whatever with WrittenModel here
            WrittenModel writtenModel = mMapper.transform(written);

            view().close();
        }
    }
}
