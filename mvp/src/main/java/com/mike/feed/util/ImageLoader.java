package com.mike.feed.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.mike.feed.domain.BitmapItem;
import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.interactor.BitmapUseCase;
import com.mike.feed.domain.interactor.BitmapUseCaseFactory;
import com.mike.feed.domain.interactor.DefaultSubscriber;
import com.mike.feed.domain.interactor.UseCase;
import com.mike.feed.domain.repository.BitmapRepository;
import com.mike.feed.R;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.mapper.BitmapModelMapper;
import com.mike.feed.model.BitmapModel;
import com.mike.utility.cache.MemoryCache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Inject;

@FragmentScope
public class ImageLoader {

    public static final int DEFAULT_IMAGE_ID = R.drawable.ic_loading;
    public static final int DEFAULT_IMAGE_ERROR = R.drawable.ic_error;

    private final MemoryCache mMemoryCache;
    private final Map<ImageView, String> mImageViews;

    private final BitmapModelMapper mMapper;
    private final List<UseCase> mUseCases;
    private final BitmapUseCaseFactory mBitmapUseCaseFactory;

    @Inject
    public ImageLoader(MemoryCache mMemoryCache, BitmapUseCaseFactory bitmapUseCaseFactory, BitmapModelMapper mapper) {
        this.mMemoryCache = mMemoryCache;
        this.mUseCases = new ArrayList<>();
        this.mMapper = mapper;
        this.mBitmapUseCaseFactory = bitmapUseCaseFactory;
        this.mImageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    }


    ImageLoader(MemoryCache mMemoryCache
            , BitmapUseCaseFactory bitmapUseCaseFactory
            , BitmapModelMapper mapper
            , Map<ImageView, String> imageViews
            ,List<UseCase> useCaseList) {

        this.mMemoryCache = mMemoryCache;
        this.mMapper = mapper;
        this.mBitmapUseCaseFactory = bitmapUseCaseFactory;
        this.mImageViews = imageViews;
        this.mUseCases = useCaseList;
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
            BitmapUseCase bitmapUseCase = mBitmapUseCaseFactory.create(url, reqWidth, reqHeight);
            bitmapUseCase.execute(new BitmapSubscriber(imageView, url));
            mUseCases.add(bitmapUseCase);

        }


    }


    public void unsubscribe(){
        for(UseCase useCase : mUseCases){
            useCase.unsubscribe();
        }
    }



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
