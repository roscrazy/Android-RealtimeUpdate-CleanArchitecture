package com.mike.feed.ui;

import android.support.annotation.NonNull;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.mike.domain.Feed;
import com.mike.domain.Written;
import com.mike.domain.executor.PostExecutionThread;
import com.mike.domain.executor.ThreadExecutor;
import com.mike.domain.interactor.DefaultSubscriber;
import com.mike.domain.interactor.NewFeedUseCase;
import com.mike.domain.repository.FeedRepository;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.WrittenModel;
import com.mike.feed.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 8/26/16.
 */
@FragmentScope
public class NewFeedPresenter extends BasePresenter<NewFeedView> {


    @NonNull
    private FeedRepository mRepository;
    @NonNull
    private ThreadExecutor mThreadExecutor;
    @NonNull
    private PostExecutionThread mPostExecutionThread;
    @NonNull
    private FeedModelMapper mMapper;

    NewFeedUseCase newFeedUseCase;

    @Inject
    public NewFeedPresenter(FeedRepository mRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, FeedModelMapper mapper) {
        this.mRepository = mRepository;
        this.mThreadExecutor = threadExecutor;
        this.mPostExecutionThread = postExecutionThread;
        this.mMapper = mapper;
    }

    public void submit(String title, String body, String img){
        Feed feed = new Feed();
        feed.setBody(body);
        feed.setTitle(title);
        feed.setImage(img);

        /**
         * I am not sure whether it could be a good approach when I init a UseCase in a presenter
         * Presenter should not know anything about FeedRepository, ThreadExecutor, PostExecutionThread, and it's difficult to test whether the UseCase have executed.
         * But for dynamic param to a UseCase, I think it can be an acceptable option.
         * Some people do something like "newFeedUseCase.setFeed(feed)" before executing, but I don't think it's better than this approach.
         * I am more than happy if you could provide me some better approach or your opinion.
         */
        newFeedUseCase = new NewFeedUseCase(feed, mRepository, mThreadExecutor, mPostExecutionThread);
        newFeedUseCase.execute(new NewFeedSubscriber());
        unsubscribeOnUnbindView(newFeedUseCase);
    }


    @RxLogSubscriber
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
