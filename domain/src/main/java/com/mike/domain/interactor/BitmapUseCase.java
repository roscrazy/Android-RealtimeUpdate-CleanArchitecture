package com.mike.domain.interactor;

import android.support.annotation.NonNull;

import com.mike.domain.executor.PostExecutionThread;
import com.mike.domain.executor.ThreadExecutor;
import com.mike.domain.repository.BitmapRepository;

import rx.Observable;

/**
 * Created by MinhNguyen on 8/25/16.
 */
public class BitmapUseCase extends UseCase{

    private BitmapRepository mRepository;


    @NonNull
    private String mUrl;

    private int mReqWidth;

    private int mReqHeight;


    public BitmapUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, BitmapRepository mRepository
            , @NonNull String url, int reqWidth, int reqHeight) {
        super(threadExecutor, postExecutionThread);
        this.mRepository = mRepository;
        this.mUrl = url;
        this.mReqHeight = reqHeight;
        this.mReqWidth = reqWidth;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if(mUrl != null) {
            return this.mRepository.loadBitmap(mUrl, mReqWidth, mReqHeight);
        }else{
            throw new NullPointerException("url can't not be null");
        }
    }
}
