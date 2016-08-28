package com.mike.feed.ui;

import com.mike.feed.domain.interactor.FeedChangedUseCase;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.FeedChangedInfoModel;
import com.mike.feed.model.FeedModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Subscriber;

/**
 * Created by MinhNguyen on 8/25/16.
 */
public class MainPresenterTest {

    @Mock
    FeedChangedUseCase feedChangedUseCase;

    @Mock
    FeedModelMapper mapper;

    @Mock
    MainView view;

    @Mock
    FeedModel feedModel;

    @Mock
    List<String> keyStore;

    MainPresenter mainPresenter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mainPresenter = new MainPresenter(feedChangedUseCase, mapper);
        mainPresenter.bindView(view);
        mainPresenter.mKeyStore = keyStore;
    }

    @Test
    public void initShouldExecuteTheUseCase(){
        mainPresenter.init();
        Mockito.verify(feedChangedUseCase).execute(Matchers.any(Subscriber.class));
        Mockito.verifyNoMoreInteractions(view);
        Mockito.verifyNoMoreInteractions(mapper);
    }

    @Test
    public void addNewFeedShouldBeWellHandleWithZeroItem(){

        FeedChangedInfoModel infoModel = new FeedChangedInfoModel();
        infoModel.setFeed(feedModel);
        infoModel.setType(FeedChangedInfoModel.EventType.Added);
        infoModel.setPreviousChildKey(null);
        final String key = "key";
        infoModel.setKey(key);

        mainPresenter.handleFeedChanged(infoModel);

        Mockito.verify(view).addItem(feedModel, 0);
        Mockito.verify(keyStore).add(0, key);

        Mockito.verifyNoMoreInteractions(view);
        Mockito.verifyNoMoreInteractions(keyStore);
    }

    @Test
    public void addNewFeedShouldBeWellHandle(){

        final String key = "key";
        final String previousKey = "previous";

        FeedChangedInfoModel infoModel = new FeedChangedInfoModel();
        infoModel.setFeed(feedModel);
        infoModel.setType(FeedChangedInfoModel.EventType.Added);
        infoModel.setPreviousChildKey(previousKey);
        infoModel.setKey(key);
        Mockito.when(keyStore.indexOf(previousKey)).thenReturn(2);

        mainPresenter.handleFeedChanged(infoModel);

        Mockito.verify(view).addItem(feedModel, 3);
        Mockito.verify(keyStore).add(3, key);
        Mockito.verify(keyStore).indexOf(previousKey);

        Mockito.verifyNoMoreInteractions(view);
        Mockito.verifyNoMoreInteractions(keyStore);
    }

    @Test
    public void removeFeedShouldBeWellHandle(){
        final String key = "key";

        FeedChangedInfoModel infoModel = new FeedChangedInfoModel();
        infoModel.setFeed(feedModel);
        infoModel.setType(FeedChangedInfoModel.EventType.Removed);
        infoModel.setKey(key);
        Mockito.when(keyStore.indexOf(key)).thenReturn(2);

        mainPresenter.handleFeedChanged(infoModel);

        Mockito.verify(view).removeItem(2);
        Mockito.verify(keyStore).indexOf(key);
        Mockito.verify(keyStore).remove(2);

        Mockito.verifyNoMoreInteractions(view);
        Mockito.verifyNoMoreInteractions(keyStore);
    }

    @Test
    public void movedShouldBeWelHandle(){
        final String key = "key";
        final String previousKey = "previous";

        FeedChangedInfoModel infoModel = new FeedChangedInfoModel();
        infoModel.setFeed(feedModel);
        infoModel.setType(FeedChangedInfoModel.EventType.Moved);
        infoModel.setPreviousChildKey(previousKey);
        infoModel.setKey(key);

        Mockito.when(keyStore.indexOf(previousKey)).thenReturn(2);
        Mockito.when(keyStore.indexOf(key)).thenReturn(5);

        mainPresenter.handleFeedChanged(infoModel);

        Mockito.verify(view).moveItem(5, 3);
        Mockito.verify(keyStore).remove(5);
        Mockito.verify(keyStore).add(3, key);
    }
}