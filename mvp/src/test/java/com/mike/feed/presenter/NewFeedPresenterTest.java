package com.mike.feed.presenter;

import com.mike.feed.domain.Feed;
import com.mike.feed.domain.Written;
import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.interactor.NewFeedUseCase;
import com.mike.feed.domain.interactor.NewFeedUseCaseFactory;
import com.mike.feed.domain.repository.FeedRepository;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.view.NewFeedView;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class NewFeedPresenterTest {

    @Mock
    private NewFeedUseCaseFactory mNewFeedUseCaseFactory;

    @Mock
    private NewFeedUseCase mUseCase;

    @Mock
    private FeedModelMapper mMapper;

    @Mock
    private NewFeedView mView;


    private NewFeedPresenter mPresenter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mPresenter = new NewFeedPresenter(mNewFeedUseCaseFactory, mMapper);
        mPresenter.bindView(mView);
    }

    @Test
    public void newFeedScreenShouldCloseIfAddNewSuccessful(){
        Observable observable = Observable.create(new Observable.OnSubscribe<Written>() {
            @Override
            public void call(Subscriber<? super Written> subscriber) {
                Written written = new Written();
                subscriber.onNext(written);
                subscriber.onCompleted();
            }
        });

        observable.subscribe(mPresenter.new NewFeedSubscriber());
        Mockito.verify(mView).close();
        Mockito.verifyNoMoreInteractions(mView);

    }

    @Test
    public void newFeedScreenShouldShowErrorIfAddNewSuccessful(){
        Observable observable = Observable.create(new Observable.OnSubscribe<Written>() {
            @Override
            public void call(Subscriber<? super Written> subscriber) {
                subscriber.onError(new RuntimeException("There is error occur"));
            }
        });

        observable.subscribe(mPresenter.new NewFeedSubscriber());
        Mockito.verify(mView).showErrorMsg();
        Mockito.verifyNoMoreInteractions(mView);
    }

    @Test
    public void submitShouldExecuteUseCase(){
        Mockito.when(mNewFeedUseCaseFactory.create(Matchers.any(Feed.class))).thenReturn(mUseCase);
        mPresenter.submit("title", "body", "image");

        Mockito.verify(mUseCase).execute(Matchers.any(Subscriber.class));
        Mockito.verifyNoMoreInteractions(mView);
    }
}