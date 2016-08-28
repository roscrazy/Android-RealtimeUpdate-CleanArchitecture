package com.mike.feed.ui.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mike.domain.interactor.UseCase;

/**
 * Base presenter implementation.
 *
 * @param <V> view.
 */
public interface Presenter<V> {


    public void bindView(@NonNull V view);

    @Nullable
    public V view();

    public void unsubscribeOnUnbindView(@NonNull UseCase subscription, @NonNull UseCase... subscriptions);

    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    public void unbindView(@NonNull V view);



}
