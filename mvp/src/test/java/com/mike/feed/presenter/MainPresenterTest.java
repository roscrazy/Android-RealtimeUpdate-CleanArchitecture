package com.mike.feed.presenter;

import com.mike.feed.domain.Deleted;
import com.mike.feed.domain.interactor.DeleteFeedUseCase;
import com.mike.feed.domain.interactor.DeleteFeedUseCaseFactory;
import com.mike.feed.domain.interactor.FeedChangedUseCase;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.FeedChangedInfoModel;
import com.mike.feed.model.FeedModel;
import com.mike.feed.util.CompositeUseCases;
import com.mike.feed.view.MainView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by MinhNguyen on 8/25/16.
 */
public class MainPresenterTest {

    private static String FAKE_KEY = "key";
    private static String FAKE_PREVIOUS_KEY = "PREVIOUS_KEY";

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

    @Mock
    DeleteFeedUseCase deleteFeedUseCase;

    @Mock
    DeleteFeedUseCaseFactory deleteFeedUseCaseFactory;


    MainPresenter mainPresenter;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        mainPresenter = new MainPresenter(feedChangedUseCase, mapper, deleteFeedUseCaseFactory, keyStore);
        mainPresenter.useCasesToUnsubscribeOnUnbindView = Mockito.mock(CompositeUseCases.class);
        mainPresenter.bindView(view);
    }

    @Test
    public void initShouldExecuteTheUseCase(){
        mainPresenter.init();
        Mockito.verify(feedChangedUseCase).execute(Matchers.any(Subscriber.class));
        Mockito.verifyNoMoreInteractions(view);
        Mockito.verifyNoMoreInteractions(mapper);
    }

    @Test
    public void handleErrorShouldShowError(){
        mainPresenter.handleError(new Exception());
        Mockito.verify(view).showErrorMsg();
        Mockito.verifyNoMoreInteractions(view);
    }

    @Test
    public void addNewFeedShouldBeWellHandledWithZeroItem(){

        FeedChangedInfoModel infoModel = createDummyFeedChangedInfoModel(FeedChangedInfoModel.EventType.Added);
        infoModel.setPreviousChildKey(null);

        mainPresenter.handleFeedChanged(infoModel);

        Mockito.verify(view).addItem(feedModel, 0);
        Mockito.verify(keyStore).add(0, FAKE_KEY);

        Mockito.verifyNoMoreInteractions(view);
        Mockito.verifyNoMoreInteractions(keyStore);
    }


    @Test(expected = IllegalArgumentException.class)
    public void addNewShouldThrowExceptionIfThereIsNoItem(){
        Mockito.when(keyStore.indexOf(FAKE_PREVIOUS_KEY)).thenReturn(-1);
        mainPresenter.handleFeedChanged(createDummyFeedChangedInfoModel(FeedChangedInfoModel.EventType.Added));
    }

    @Test
    public void addNewFeedShouldBeWellHandled(){
        FeedChangedInfoModel infoModel = createDummyFeedChangedInfoModel(FeedChangedInfoModel.EventType.Added);

        Mockito.when(keyStore.indexOf(FAKE_PREVIOUS_KEY)).thenReturn(2);
        mainPresenter.handleFeedChanged(infoModel);

        Mockito.verify(view).addItem(feedModel, 3);
        Mockito.verify(keyStore).add(3, FAKE_KEY);
        Mockito.verifyNoMoreInteractions(view);
    }

    @Test
    public void removeFeedShouldBeWellHandled(){

        Mockito.when(keyStore.indexOf(FAKE_KEY)).thenReturn(2);
        mainPresenter.handleFeedChanged(createDummyFeedChangedInfoModel(FeedChangedInfoModel.EventType.Removed));

        Mockito.verify(view).removeItem(2);
        Mockito.verify(keyStore).remove(2);

        Mockito.verifyNoMoreInteractions(view);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeFeedShouldThrowExceptionIfThereIsNoItem(){
        Mockito.when(keyStore.indexOf(FAKE_KEY)).thenReturn(-1);
        mainPresenter.handleFeedChanged(createDummyFeedChangedInfoModel(FeedChangedInfoModel.EventType.Removed));
    }




    @Test
    public void movedShouldBeWelHandle(){
        FeedChangedInfoModel infoModel = createDummyFeedChangedInfoModel(FeedChangedInfoModel.EventType.Moved);
        Mockito.when(keyStore.indexOf(FAKE_PREVIOUS_KEY)).thenReturn(2);
        Mockito.when(keyStore.indexOf(FAKE_KEY)).thenReturn(5);

        mainPresenter.handleFeedChanged(infoModel);

        Mockito.verify(view).moveItem(5, 3);
        Mockito.verify(keyStore).remove(5);
        Mockito.verify(keyStore).add(3, FAKE_KEY);
    }


    @Test(expected = IllegalArgumentException.class)
    public void movedFeedShouldThrowExceptionIfThereIsNoItem(){
        Mockito.when(keyStore.indexOf(FAKE_KEY)).thenReturn(-1);
        mainPresenter.handleFeedChanged(createDummyFeedChangedInfoModel(FeedChangedInfoModel.EventType.Moved));
    }


    @Test(expected = IllegalArgumentException.class)
    public void changedShouldThrowExceptionIfThereIsNoItem(){
        Mockito.when(keyStore.indexOf(FAKE_KEY)).thenReturn(-1);
        mainPresenter.handleFeedChanged(createDummyFeedChangedInfoModel(FeedChangedInfoModel.EventType.Changed));
    }


    @Test
    public void changedShouldBeWellHandle(){
        FeedChangedInfoModel infoModel = createDummyFeedChangedInfoModel(FeedChangedInfoModel.EventType.Changed);
        Mockito.when(keyStore.indexOf(FAKE_KEY)).thenReturn(5);
        mainPresenter.handleFeedChanged(infoModel);
        Mockito.verify(view).updateItem(feedModel, 5);
        Mockito.verifyNoMoreInteractions(view);
    }


    @Test
    public void deleteFeedShouldExecuteTheUseCase(){
        Mockito.when(deleteFeedUseCaseFactory.create(FAKE_KEY)).thenReturn(deleteFeedUseCase);
        Mockito.when(keyStore.get(1)).thenReturn(FAKE_KEY);
        Mockito.when(keyStore.size()).thenReturn(10);

        mainPresenter.deleteFeed(feedModel, 1);
        Mockito.verify(deleteFeedUseCase).execute(Matchers.any(MainPresenter.DeleteFeedSubscriber.class));
    }


    @Test(expected = IllegalArgumentException.class)
    public void deleteFeedShouldHandleNegativeIndex(){
        mainPresenter.deleteFeed(feedModel, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteFeedShouldHandleOutOfBoundIndex(){
        Mockito.when(keyStore.size()).thenReturn(1);
        mainPresenter.deleteFeed(feedModel, 2);
    }


    @Test
    public void deleteFeedSubscriberShouldRemoveUnsubribe(){

        Observable observable = Observable.just(new Deleted());

        observable.subscribe(mainPresenter.new DeleteFeedSubscriber(deleteFeedUseCase));
        Mockito.verify(mainPresenter.useCasesToUnsubscribeOnUnbindView).remove(deleteFeedUseCase);


        Mockito.reset(mainPresenter.useCasesToUnsubscribeOnUnbindView);

        observable = Observable.error(new RuntimeException());
        observable.subscribe(mainPresenter.new DeleteFeedSubscriber(deleteFeedUseCase));
        Mockito.verify(mainPresenter.useCasesToUnsubscribeOnUnbindView).remove(deleteFeedUseCase);

    }


    private FeedChangedInfoModel createDummyFeedChangedInfoModel(FeedChangedInfoModel.EventType type){
        FeedChangedInfoModel infoModel = new FeedChangedInfoModel();
        infoModel.setFeed(feedModel);
        infoModel.setType(type);
        infoModel.setKey(FAKE_KEY);
        infoModel.setPreviousChildKey(FAKE_PREVIOUS_KEY);
        return infoModel;
    }
}