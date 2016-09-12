package com.mike.feed.presenter;

import com.mike.feed.domain.interactor.UseCase;
import com.mike.feed.util.CompositeUseCases;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.*;
/**
 * Created by MinhNguyen on 9/12/16.
 */
public class BasePresenterTest {


    private BasePresenter<DummyView> basePresenter;

    @Mock
    CompositeUseCases compositeUseCases;

    @Mock
    DummyView view;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        basePresenter = new BasePresenter<>(compositeUseCases);
    }


    @Test
    public void bindViewShouldRefToView() throws Exception {
        basePresenter.bindView(view);
        assertThat("bind view should have reference to the view", basePresenter.view(), is(view));
    }

    @Test (expected = IllegalStateException.class)
    public void bindViewShouldHandleExistingView(){
        basePresenter.bindView(view);
        DummyView dummyView = Mockito.mock(DummyView.class);
        basePresenter.bindView(dummyView);
    }

    @Test
    public void binViewShouldWorkFineAfterUnbinView(){
        basePresenter.bindView(view);
        basePresenter.unbindView(view);

        DummyView dummyView = Mockito.mock(DummyView.class);
        basePresenter.bindView(dummyView);
        assertThat("bind view should work fine after unbind view", basePresenter.view(), is(dummyView));

    }



    @Test
    public void viewShouldReturnBoundView() throws Exception {
        basePresenter.bindView(view);
        assertThat("view() should return bound view", basePresenter.view(), is(view));
    }

    @Test
    public void unsubscribeOnUnbindViewShouldAddValueToTheList() throws Exception {
        UseCase useCase = Mockito.mock(UseCase.class);
        basePresenter.unsubscribeOnUnbindView(useCase);
        Mockito.verify(compositeUseCases).add(useCase);
        Mockito.verifyNoMoreInteractions(compositeUseCases);
    }


    @Test
    public void unsubscribeOnUnbindViewShouldSupportArray() throws Exception {
        UseCase useCase1 = Mockito.mock(UseCase.class);
        UseCase useCase2 = Mockito.mock(UseCase.class);
        UseCase useCase3 = Mockito.mock(UseCase.class);

        basePresenter.unsubscribeOnUnbindView(useCase1, useCase2, useCase3);
        Mockito.verify(compositeUseCases).add(useCase1);
        Mockito.verify(compositeUseCases).add(useCase2);
        Mockito.verify(compositeUseCases).add(useCase3);
        Mockito.verifyNoMoreInteractions(compositeUseCases);

    }


    @Test
    public void unbindViewShoudClearUseCasesAndSetViewToNull() throws Exception {
        basePresenter.bindView(view);
        basePresenter.unbindView(view);

        assertThat("unbind view should set view to null", basePresenter.view(), is(nullValue()));
        Mockito.verify(compositeUseCases).clear();
    }

    @Test(expected = IllegalStateException.class)
    public void unbindViewShoudHandleDifferentView() throws Exception {
        basePresenter.bindView(view);

        DummyView dummyView = Mockito.mock(DummyView.class);
        basePresenter.unbindView(dummyView);
    }



    public static interface DummyView{

    }
}