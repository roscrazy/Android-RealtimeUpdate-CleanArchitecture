package com.mike.feed.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.mike.feed.domain.BitmapItem;
import com.mike.feed.domain.interactor.BitmapUseCase;
import com.mike.feed.domain.interactor.BitmapUseCaseFactory;
import com.mike.feed.domain.interactor.UseCase;
import com.mike.feed.mapper.BitmapModelMapper;
import com.mike.feed.model.BitmapModel;
import com.mike.utility.cache.MemoryCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by MinhNguyen on 9/13/16.
 */

public class ImageLoaderTest {

    private static final Bitmap DUMMY_BITMAP = Mockito.mock(Bitmap.class);
    private static final String DUMMY_URL = "http://img.f29.vnecdn.net/2016/09/14/cam-photo-front-chettra-9214-1473862876.jpg";
    private static final BitmapUseCase DUMMY_BITMAP_USECASE = mock(BitmapUseCase.class);
    private static final int REQUIRED_SIZE = 100;

    @Mock
    private MemoryCache mMemoryCache;

    @Mock
    Map<String, ImageLoader.BitmapSubscriber> subscriberMap;

    @Mock
    private BitmapUseCaseFactory bitmapUseCaseFactory;

    @Mock
    private BitmapModelMapper mapper;

    @Mock
    private ImageView imageView;


    @Mock
    private List<UseCase> useCases;

    @Mock
    private Map<ImageView, String> imageViews;


    ImageLoader imageLoader;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        imageLoader = new ImageLoader(mMemoryCache, bitmapUseCaseFactory, mapper, imageViews, useCases, subscriberMap);
    }

    @Test
    public void displayImageShouldNotAddToQueueIfAlreadyExist(){
        ImageLoader.BitmapSubscriber subscriber = mock(ImageLoader.BitmapSubscriber.class);

        when(subscriberMap.get(DUMMY_URL)).thenReturn(subscriber);
        when(imageViews.get(imageView)).thenReturn(null);
        when(mMemoryCache.get(DUMMY_URL)).thenReturn(null);

        imageLoader.displayImage(imageView, DUMMY_URL, REQUIRED_SIZE, REQUIRED_SIZE);
        verify(imageViews).put(imageView, DUMMY_URL);
        verify(subscriber).addImageView(imageView);

        verifyNoMoreInteractions(bitmapUseCaseFactory);
        verifyNoMoreInteractions(mapper);
        verifyNoMoreInteractions(imageView);
    }

    @Test
    public void displayImageShouldNotAddToQueueIfAlreadyExistWithSameUrl() throws Exception {
        when(imageViews.get(imageView)).thenReturn(DUMMY_URL);

        imageLoader.displayImage(imageView, DUMMY_URL, REQUIRED_SIZE, REQUIRED_SIZE);

        verify(imageViews, never()).put(imageView, DUMMY_URL);

        verifyNoMoreInteractions(mMemoryCache);
        verifyNoMoreInteractions(bitmapUseCaseFactory);
        verifyNoMoreInteractions(mapper);
        verifyNoMoreInteractions(imageView);
    }


    @Test
    public void displayImageShouldShowImageIfAlreadyInMemory() throws Exception {
        when(mMemoryCache.get(DUMMY_URL)).thenReturn(DUMMY_BITMAP);

        imageLoader.displayImage(imageView, DUMMY_URL, REQUIRED_SIZE, REQUIRED_SIZE);

        verify(imageViews).put(imageView, DUMMY_URL);
        verify(imageView).setImageBitmap(DUMMY_BITMAP);

        verifyNoMoreInteractions(bitmapUseCaseFactory);
        verifyNoMoreInteractions(mapper);
        verifyNoMoreInteractions(imageView);
    }


    @Test
    public void displayImageShouldExecuteUseCaseIfNoMemoryCache() throws Exception {
        BitmapUseCase bitmapUseCase = mock(BitmapUseCase.class);

        when(bitmapUseCaseFactory.create(DUMMY_URL, REQUIRED_SIZE, REQUIRED_SIZE)).thenReturn(bitmapUseCase);
        imageLoader.displayImage(imageView, DUMMY_URL, REQUIRED_SIZE, REQUIRED_SIZE);


        verify(imageViews).put(imageView, DUMMY_URL);
        verify(imageView).setImageResource(ImageLoader.DEFAULT_IMAGE_ID);

        verify(bitmapUseCase).execute(any(Subscriber.class));
        verify(useCases).add(bitmapUseCase);

        verifyNoMoreInteractions(mapper);
        verifyNoMoreInteractions(imageView);
    }


    @Test
    public void unsubscribeShouldHandleZeroUseCases() throws Exception {

        Iterator iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(false);

        when(useCases.iterator()).thenReturn(iterator);

        imageLoader.unsubscribe();

    }


    @Test
    public void unsubscribeShouldUnsubcribeAllUseCases() throws Exception {
        UseCase useCase1 = mock(UseCase.class);
        UseCase useCase2 = mock(UseCase.class);
        UseCase useCase3 = mock(UseCase.class);

        Iterator iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, true, true, false);
        when(iterator.next()).thenReturn(useCase1)
                .thenReturn(useCase2).thenReturn(useCase3);

        when(useCases.iterator()).thenReturn(iterator);

        imageLoader.unsubscribe();

        verify(useCase1).unsubscribe();
        verify(useCase2).unsubscribe();
        verify(useCase3).unsubscribe();
    }


    @Test
    public void bitmapSubscriberShouldSetBitmapToImageView() {
        BitmapModel bitmapModel = new BitmapModel();
        bitmapModel.setBitmap(DUMMY_BITMAP);
        BitmapItem bitmapItem = mock(BitmapItem.class);
        when(mapper.transform(bitmapItem)).thenReturn(bitmapModel);

        when(imageViews.get(imageView)).thenReturn(DUMMY_URL);

        Observable<BitmapItem> observable = Observable.just(bitmapItem);

        observable.subscribe(imageLoader.new BitmapSubscriber(imageView, DUMMY_URL, DUMMY_BITMAP_USECASE));

        verify(imageView).setImageBitmap(DUMMY_BITMAP);
        verify(useCases).remove(DUMMY_BITMAP_USECASE);
        verify(mMemoryCache).put(DUMMY_URL, DUMMY_BITMAP);

    }

    @Test
    public void bitmapSubscriberShouldNotSetBitmapIfTheImageViewHaveSetToOtherUrl() {
        BitmapModel bitmapModel = new BitmapModel();
        bitmapModel.setBitmap(DUMMY_BITMAP);
        BitmapItem bitmapItem = mock(BitmapItem.class);
        when(mapper.transform(bitmapItem)).thenReturn(bitmapModel);

        when(imageViews.get(imageView)).thenReturn("new url");

        Observable<BitmapItem> observable = Observable.just(bitmapItem);

        observable.subscribe(imageLoader.new BitmapSubscriber(imageView, DUMMY_URL, DUMMY_BITMAP_USECASE));

        verify(mMemoryCache).put(DUMMY_URL, DUMMY_BITMAP);
        verify(useCases).remove(DUMMY_BITMAP_USECASE);

        verify(imageView, never()).setImageBitmap(DUMMY_BITMAP);

    }

    @Test
    public void bitmapSubscriberShouldHandleError() {
        BitmapModel bitmapModel = new BitmapModel();
        bitmapModel.setBitmap(DUMMY_BITMAP);
        BitmapItem bitmapItem = mock(BitmapItem.class);
        when(mapper.transform(bitmapItem)).thenReturn(bitmapModel);

        when(imageViews.get(imageView)).thenReturn(DUMMY_URL);

        Observable<BitmapItem> observable = Observable.error(new RuntimeException("dummy exception"));

        observable.subscribe(imageLoader.new BitmapSubscriber(imageView, DUMMY_URL, DUMMY_BITMAP_USECASE));

        verify(imageView).setImageResource(ImageLoader.DEFAULT_IMAGE_ERROR);

        verify(mMemoryCache, never()).put(DUMMY_URL, DUMMY_BITMAP);
        verify(imageView, never()).setImageBitmap(DUMMY_BITMAP);
        verify(useCases).remove(DUMMY_BITMAP_USECASE);

    }
}