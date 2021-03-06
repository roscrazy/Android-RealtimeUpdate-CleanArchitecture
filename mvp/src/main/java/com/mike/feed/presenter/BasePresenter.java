package com.mike.feed.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mike.feed.domain.interactor.UseCase;
import com.mike.feed.util.CompositeUseCases;
import com.mike.feed.util.CompositeUseCasesImpl;

/**
 * Base presenter implementation.
 *
 * @param <V> view.
 */
public class BasePresenter<V> implements Presenter<V> {

    @NonNull
    protected CompositeUseCases useCasesToUnsubscribeOnUnbindView;

    @Nullable
    private volatile V view;


    public BasePresenter() {
        this(new CompositeUseCasesImpl());
    }

    BasePresenter(CompositeUseCases compositeUseCases) {
        this.useCasesToUnsubscribeOnUnbindView = compositeUseCases;
    }

    @Override
    @CallSuper
    public void bindView(@NonNull V view) {

        final V previousView = this.view;

        if (previousView != null) {
            throw new IllegalStateException("Previous view is not unbounded! previousView = " + previousView);
        }

        this.view = view;
    }

    @NonNull
    @Override
    public V view() {
        return view;
    }

    @Override
    public final void unsubscribeOnUnbindView(@NonNull UseCase subscription, @NonNull UseCase... subscriptions) {
        useCasesToUnsubscribeOnUnbindView.add(subscription);

        for (UseCase s : subscriptions) {
            useCasesToUnsubscribeOnUnbindView.add(s);
        }

    }

    public void removeUnsubscribe(UseCase useCase){
        if(useCase != null)
            useCasesToUnsubscribeOnUnbindView.remove(useCase);
    }

    public boolean haveSubscription(){
        if(useCasesToUnsubscribeOnUnbindView != null)
            return useCasesToUnsubscribeOnUnbindView.hasSubscriptions();
        return false;
    }



    @Override
    @CallSuper
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    public void unbindView(@NonNull V view) {
        // Unsubscribe all subscriptions that need to be unsubscribed in this lifecycle state.
        useCasesToUnsubscribeOnUnbindView.clear();
        final V previousView = this.view;
        if (previousView == view) {
            this.view = null;
        } else {
            throw new IllegalStateException("Unexpected view! previousView = " + previousView + ", view to unbind = " + view);
        }
    }
}
