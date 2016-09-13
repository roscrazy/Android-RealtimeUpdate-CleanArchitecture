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

public interface CompositeUseCases {


    public void add(final UseCase s);

    public void remove(final UseCase s);

    public boolean contain(UseCase useCase);

    public void clear();

    public boolean hasSubscriptions();
}
