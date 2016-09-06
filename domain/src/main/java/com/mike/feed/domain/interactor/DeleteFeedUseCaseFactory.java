package com.mike.feed.domain.interactor;

import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.repository.FeedRepository;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 9/6/16.
 */
public class DeleteFeedUseCaseFactory {
    private FeedRepository mRepository;
    private ThreadExecutor mThreadExecutor;
    private PostExecutionThread mPostExecutionThread;

    @Inject
    public DeleteFeedUseCaseFactory(FeedRepository mRepository, ThreadExecutor mThreadExecutor, PostExecutionThread mPostExecutionThread) {
        this.mRepository = mRepository;
        this.mThreadExecutor = mThreadExecutor;
        this.mPostExecutionThread = mPostExecutionThread;
    }

    public DeleteFeedUseCase create(String feed){
        return new DeleteFeedUseCase(feed, mRepository, mThreadExecutor, mPostExecutionThread);
    }
}
