package com.mike.feed.util;

import com.mike.feed.domain.interactor.UseCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Subscription;
import rx.exceptions.Exceptions;

/**
 * Created by MinhNguyen on 4/4/16.
 */

public class CompositeUseCasesImpl implements CompositeUseCases{

    private Set<UseCase> subscriptions;

    private volatile boolean unsubscribed;

    public CompositeUseCasesImpl() {
    }


    @Override
    public void add(final UseCase s) {
        if (s.isUnsubscribed()) {
            return;
        }
        if (!unsubscribed) {
            synchronized (this) {
                if (!unsubscribed) {
                    if (subscriptions == null) {
                        subscriptions = new HashSet<UseCase>(5);
                    }
                    subscriptions.add(s);
                    return;
                }
            }
        }
        // call after leaving the synchronized block so we're not holding a lock while executing this
        s.unsubscribe();
    }

    @Override
    public void remove(final Subscription s) {
        if (!unsubscribed) {
            boolean unsubscribe = false;
            synchronized (this) {
                if (unsubscribed || subscriptions == null) {
                    return;
                }
                unsubscribe = subscriptions.remove(s);
            }
            if (unsubscribe) {
                // if we removed successfully we then need to call unsubscribe on it (outside of the lock)
                s.unsubscribe();
            }
        }
    }

    @Override
    public boolean contain(UseCase useCase) {
        synchronized (this) {
            if (unsubscribed || subscriptions == null) {
                return false;
            } else {
                return subscriptions.contains(useCase);
            }
        }
    }

    @Override
    public void clear() {
        if (!unsubscribed) {
            Collection<UseCase> unsubscribe = null;
            synchronized (this) {
                if (unsubscribed || subscriptions == null) {
                    return;
                } else {
                    unsubscribe = subscriptions;
                    subscriptions = null;
                }
            }
            unsubscribeFromAll(unsubscribe);

            unsubscribed = true;
        }
    }


    private static void unsubscribeFromAll(Collection<UseCase> subscriptions) {
        if (subscriptions == null) {
            return;
        }
        List<Throwable> es = null;
        for (UseCase s : subscriptions) {
            try {
                s.unsubscribe();
            } catch (Throwable e) {
                if (es == null) {
                    es = new ArrayList<Throwable>();
                }
                es.add(e);
            }
        }

        Exceptions.throwIfAny(es);
    }


    @Override
    public boolean hasSubscriptions() {
        if (!unsubscribed) {
            synchronized (this) {
                return !unsubscribed && subscriptions != null && !subscriptions.isEmpty();
            }
        }
        return false;
    }
}
