package com.mike.feed.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.mike.feed.domain.BitmapItem;
import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.interactor.BitmapUseCase;
import com.mike.feed.domain.interactor.DefaultSubscriber;
import com.mike.feed.domain.interactor.UseCase;
import com.mike.feed.domain.repository.BitmapRepository;
import com.mike.feed.R;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.mapper.BitmapModelMapper;
import com.mike.feed.model.BitmapModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;

@FragmentScope
public class ImageLoader {

    private static final int DEFAULT_IMAGE_ID = R.drawable.ic_loading;
    private static final int DEFAULT_IMAGE_ERROR = R.drawable.ic_error;

    private MemoryCache mMemoryCache;
    private BitmapUseCase mBitmapUseCase;

    private Map<ImageView, String> mImageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    BitmapModelMapper mMapper;
    ThreadExecutor mThreadExecutor;
    PostExecutionThread mPostExecutionThread;
    BitmapRepository mRepository;
    List<UseCase> mUseCases;

    @Inject
    public ImageLoader(MemoryCache mMemoryCache, ThreadExecutor mThreadExecutor
            , PostExecutionThread mPostExecutionThread, BitmapRepository mRepository, BitmapModelMapper mapper) {
        this.mMemoryCache = mMemoryCache;
        this.mThreadExecutor = mThreadExecutor;
        this.mPostExecutionThread = mPostExecutionThread;
        this.mRepository = mRepository;
        this.mUseCases = new ArrayList<>();
        this.mMapper = mapper;
    }

    public void displayImage(final ImageView imageView, final String url, int reqWidth, int reqHeight){

        // Already added to queue
        if(mImageViews.get(imageView) != null && mImageViews.get(imageView).equals(url))
            return;

        mImageViews.put(imageView, url);

        Bitmap bitmap = mMemoryCache.get(url);

        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }else{
            imageView.setImageResource(DEFAULT_IMAGE_ID);
            /**
             * I am not sure whether it could be a good approach when I init a UseCase in a presenter
             * Presenter should not know anything about Repository, ThreadExecutor, PostExecutionThread, and it's difficult to test whether the UseCase have executed.
             * But for dynamic param to a UseCase, I think it can be an acceptable option.
             * Some people do something like "newFeedUseCase.init(url, width, height)" before execute, but I don't think it's better than this approach.
             * I am more than happy if you could provide me some better option or your opinion.
             */

            mBitmapUseCase = new BitmapUseCase(mThreadExecutor, mPostExecutionThread, mRepository, url, reqWidth, reqHeight);

            mBitmapUseCase.execute(new BitmapSubscriber(imageView, url));
            mUseCases.add(mBitmapUseCase);

        }


    }


    public void unsubscribe(){
        for(UseCase useCase : mUseCases){
            useCase.unsubscribe();
        }
    }


    @RxLogSubscriber
    protected final class BitmapSubscriber extends DefaultSubscriber<BitmapItem> {
        // Avoid memory leak
        private WeakReference<ImageView> mImageView;
        private String mUrl;
        public BitmapSubscriber(ImageView mImageView, String url) {
            this.mImageView = new WeakReference<ImageView>(mImageView);
            this.mUrl = url;
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            e.printStackTrace();
            ImageView imageView = mImageView.get();
            if(imageView != null){
                imageView.setImageResource(DEFAULT_IMAGE_ERROR);
            }
        }

        @Override
        public void onNext(BitmapItem bitmapItem) {
            BitmapModel model = mMapper.transform(bitmapItem);
            ImageView imageView = mImageView.get();
            // Check whether the ImageView have been set to other image
            // if yet, then we will not set the bitmap to the ImageView.
            if(mUrl != null && imageView != null
                    && mUrl.equals(mImageViews.get(imageView))){

                imageView.setImageBitmap(model.getBitmap());
                mMemoryCache.put(mUrl, model.getBitmap());
            }
        }
    }
}
