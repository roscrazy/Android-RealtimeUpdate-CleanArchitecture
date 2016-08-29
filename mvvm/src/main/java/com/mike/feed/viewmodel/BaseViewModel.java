package com.mike.feed.viewmodel;

import android.support.annotation.NonNull;

import com.mike.feed.domain.interactor.UseCase;
import com.mike.feed.util.CompositeUseCases;

/**
 * Base ViewModel implementation.
 *
 */
public class BaseViewModel implements ViewModel {

    @NonNull
    private final CompositeUseCases useCasesToUnsubscribeOnUnbindView = new CompositeUseCases();


    @Override
    public final void unsubscribeOnUnbindView(@NonNull UseCase subscription, @NonNull UseCase... subscriptions) {
        useCasesToUnsubscribeOnUnbindView.add(subscription);

        for (UseCase s : subscriptions) {
            useCasesToUnsubscribeOnUnbindView.add(s);
        }

    }

    public boolean haveSubscription(){
        if(useCasesToUnsubscribeOnUnbindView != null)
            return useCasesToUnsubscribeOnUnbindView.hasSubscriptions();
        return false;
    }

    @Override
    public void destroy() {
        useCasesToUnsubscribeOnUnbindView.clear();
    }
}
