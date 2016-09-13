package com.mike.feed.presenter;

import android.support.annotation.NonNull;

import com.mike.feed.domain.Feed;
import com.mike.feed.domain.Written;
import com.mike.feed.domain.interactor.DefaultSubscriber;
import com.mike.feed.domain.interactor.NewFeedUseCase;
import com.mike.feed.domain.interactor.NewFeedUseCaseFactory;
import com.mike.feed.domain.interactor.UseCase;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.WrittenModel;
import com.mike.feed.view.NewFeedView;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

/**
 * Created by MinhNguyen on 8/26/16.
 */
@FragmentScope
public class NewFeedPresenter extends BasePresenter<NewFeedView> {

    @NonNull
    NewFeedUseCaseFactory newFeedUseCaseFactory;

    @NonNull
    private FeedModelMapper mapper;


    @Inject
    public NewFeedPresenter(NewFeedUseCaseFactory newFeedUseCaseFactory, FeedModelMapper mapper) {
        this.newFeedUseCaseFactory = newFeedUseCaseFactory;
        this.mapper = mapper;
    }

    public void submit(String title, String body, String img) {
        Feed feed = new Feed();
        feed.setBody(body);
        feed.setTitle(title);
        feed.setImage(img);

        NewFeedUseCase newFeedUseCase = newFeedUseCaseFactory.create(feed);
        newFeedUseCase.execute(new NewFeedSubscriber(newFeedUseCase));
        unsubscribeOnUnbindView(newFeedUseCase);
    }


    
    protected final class NewFeedSubscriber extends DefaultSubscriber<Written> {

        WeakReference<UseCase> mUseCase;

        NewFeedSubscriber(UseCase useCase){
            this.mUseCase = new WeakReference<UseCase>(useCase);
        }

        @Override
        public void onCompleted() {
            removeUnsubscribe(mUseCase.get());
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            e.printStackTrace();
            view().showErrorMsg();

            removeUnsubscribe(mUseCase.get());
        }

        @Override
        public void onNext(Written written) {
            // can do whatever with WrittenModel here
            WrittenModel writtenModel = mapper.transform(written);

            view().close();
        }
    }
}
