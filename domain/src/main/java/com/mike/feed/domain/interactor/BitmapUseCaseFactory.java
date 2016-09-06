package com.mike.feed.domain.interactor;

import android.support.annotation.NonNull;

import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.repository.BitmapRepository;

import javax.inject.Inject;

import rx.Observable;


public class BitmapUseCaseFactory{

    private ThreadExecutor mThreadExecutor;
    private PostExecutionThread mPostExecutionThread;
    private BitmapRepository mRepository;


    @Inject
    public BitmapUseCaseFactory(ThreadExecutor threadExecutor
            , PostExecutionThread postExecutionThread, BitmapRepository mRepository) {

        this.mThreadExecutor = threadExecutor;
        this.mPostExecutionThread = postExecutionThread;
        this.mRepository = mRepository;
    }

    public BitmapUseCase create(@NonNull String url, int reqWidth, int reqHeight){
        return new BitmapUseCase(mThreadExecutor, mPostExecutionThread, mRepository, url, reqWidth, reqHeight);
    }


}
