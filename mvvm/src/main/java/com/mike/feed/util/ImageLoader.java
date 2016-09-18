package com.mike.feed.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mike.feed.R;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.domain.BitmapItem;
import com.mike.feed.domain.interactor.BitmapUseCase;
import com.mike.feed.domain.interactor.BitmapUseCaseFactory;
import com.mike.feed.domain.interactor.DefaultSubscriber;
import com.mike.feed.domain.interactor.UseCase;
import com.mike.feed.mapper.BitmapModelMapper;
import com.mike.feed.model.BitmapModel;
import com.mike.utility.cache.MemoryCache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    private final Map<String, BitmapSubscriber> mSubscribers;

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
        this.mSubscribers = Collections.synchronizedMap(new HashMap<String, BitmapSubscriber>());

    }


    ImageLoader(MemoryCache mMemoryCache
            , BitmapUseCaseFactory bitmapUseCaseFactory
            , BitmapModelMapper mapper
            , Map<ImageView, String> imageViews
            , List<UseCase> useCaseList, Map<String, BitmapSubscriber> subscriberMap) {

        this.mMemoryCache = mMemoryCache;
        this.mMapper = mapper;
        this.mBitmapUseCaseFactory = bitmapUseCaseFactory;
        this.mImageViews = imageViews;
        this.mUseCases = useCaseList;
        this.mSubscribers = subscriberMap;
    }

    public void displayImage(final ImageView imageView, final String url, int reqWidth, int reqHeight) {

        // Already added to queue
        if (mImageViews.get(imageView) != null && mImageViews.get(imageView).equals(url))
            return;

        mImageViews.put(imageView, url);

        Bitmap bitmap = mMemoryCache.get(url);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            // if the image have already put to the queue
            if (mSubscribers.get(url) != null) {
                mSubscribers.get(url).addImageView(imageView);
            } else {
                imageView.setImageResource(DEFAULT_IMAGE_ID);
                BitmapUseCase bitmapUseCase = mBitmapUseCaseFactory.create(url, reqWidth, reqHeight);

                BitmapSubscriber subscriber = new BitmapSubscriber(imageView, url, bitmapUseCase);

                bitmapUseCase.execute(subscriber);
                mSubscribers.put(url, subscriber);
                mUseCases.add(bitmapUseCase);
            }

        }


    }


    public void unsubscribe() {
        for (UseCase useCase : mUseCases) {
            useCase.unsubscribe();
        }
    }


    protected final class BitmapSubscriber extends DefaultSubscriber<BitmapItem> {
        // Avoid memory leak
        private List<WeakReference<ImageView>> mWeakImageViews = new ArrayList<>();
        private WeakReference<UseCase> useCase;

        private String mUrl;

        public BitmapSubscriber(ImageView mImageView, String url, UseCase useCase) {
            this.mUrl = url;
            this.useCase = new WeakReference<UseCase>(useCase);
            addImageView(mImageView);
        }


        public void addImageView(ImageView imageView) {
            mWeakImageViews.add(new WeakReference<ImageView>(imageView));
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            e.printStackTrace();
            for (WeakReference<ImageView> weakImageView : mWeakImageViews) {
                ImageView imageView = weakImageView.get();
                if (imageView != null) {
                    imageView.setImageResource(DEFAULT_IMAGE_ERROR);
                }
            }

        }

        @Override
        public void onNext(BitmapItem bitmapItem) {
            BitmapModel model = mMapper.transform(bitmapItem);

            for (WeakReference<ImageView> weakImageView : mWeakImageViews) {
                ImageView imageView = weakImageView.get();

                if (imageView != null) {
                    // Check whether the ImageView have been set to other image
                    // if yet, then we will not set the bitmap to the ImageView.
                    if (mUrl != null && mUrl.equals(mImageViews.get(imageView))) {
                        imageView.setImageBitmap(model.getBitmap());
                    }
                }
            }

            mSubscribers.remove(mUrl);
            mUseCases.remove(useCase.get());
            mMemoryCache.put(mUrl, model.getBitmap());
        }

    }
}

