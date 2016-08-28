package com.mike.feed.ui;

import com.mike.feed.domain.Written;
import com.mike.feed.domain.executor.PostExecutionThread;
import com.mike.feed.domain.executor.ThreadExecutor;
import com.mike.feed.domain.interactor.NewFeedUseCase;
import com.mike.feed.domain.repository.FeedRepository;
import com.mike.feed.mapper.FeedModelMapper;

import org.junit.Before;
import org.junit.Test;
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
    FeedRepository repository;

    @Mock
    ThreadExecutor threadExecutor;

    @Mock
    PostExecutionThread postExecutionThread;

    @Mock
    NewFeedUseCase useCase;

    @Mock
    FeedModelMapper mapper;

    @Mock
    NewFeedView view;

    NewFeedPresenter presenter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        presenter = new NewFeedPresenter(repository, threadExecutor, postExecutionThread, mapper);
        presenter.bindView(view);
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

        observable.subscribe(presenter.new NewFeedSubscriber());
        Mockito.verify(view).close();
        Mockito.verifyNoMoreInteractions(view);

    }

    @Test
    public void newFeedScreenShouldShowErrorIfAddNewSuccessful(){
        Observable observable = Observable.create(new Observable.OnSubscribe<Written>() {
            @Override
            public void call(Subscriber<? super Written> subscriber) {
                subscriber.onError(new RuntimeException("There is error occur"));
            }
        });

        observable.subscribe(presenter.new NewFeedSubscriber());
        Mockito.verify(view).showErrorMsg();
        Mockito.verifyNoMoreInteractions(view);

    }
}