package com.mike.domain.interactor;

import android.support.annotation.NonNull;

import com.mike.domain.Feed;
import com.mike.domain.executor.PostExecutionThread;
import com.mike.domain.executor.ThreadExecutor;
import com.mike.domain.repository.FeedRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/25/16.
 */
public class NewFeedUseCase extends UseCase {

    @NonNull
    FeedRepository repository;

    @NonNull
    Feed feed;

    @Inject
    public NewFeedUseCase(Feed feed, FeedRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
        this.feed = feed;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return repository.writeFeed(feed);
    }
}
