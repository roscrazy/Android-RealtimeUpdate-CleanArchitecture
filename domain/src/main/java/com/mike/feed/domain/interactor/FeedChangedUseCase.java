package com.mike.feed.domain.interactor;

import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.repository.FeedRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/25/16.
 */
public class FeedChangedUseCase extends UseCase{

    FeedRepository repository;

    @Inject
    public FeedChangedUseCase(FeedRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return repository.registerFeedChangedEvent();
    }
}
