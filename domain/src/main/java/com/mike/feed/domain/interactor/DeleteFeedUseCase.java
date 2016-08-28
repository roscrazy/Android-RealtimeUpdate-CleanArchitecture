package com.mike.feed.domain.interactor;

import android.support.annotation.NonNull;

import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.repository.FeedRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/25/16.
 */
public class DeleteFeedUseCase extends UseCase {

    @NonNull
    FeedRepository mRepository;

    @NonNull
    String mFeedKey;

    @Inject
    public DeleteFeedUseCase(String  feed, FeedRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.mRepository = repository;
        this.mFeedKey = feed;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return mRepository.deleteFeed(mFeedKey);
    }
}
