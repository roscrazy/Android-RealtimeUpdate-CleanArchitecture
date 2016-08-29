package com.mike.feed.viewmodel;

import android.support.annotation.NonNull;

import com.mike.feed.domain.interactor.UseCase;

/**
 * Interface that every ViewModel must implement
 */
public interface ViewModel {

    void destroy();

    void unsubscribeOnUnbindView(@NonNull UseCase subscription, @NonNull UseCase... subscriptions);
}
