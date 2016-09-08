package com.mike.feed.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mike.feed.R;
import com.mike.feed.SquarApp;
import com.mike.feed.databinding.FragmentNewFeedBinding;
import com.mike.feed.dependency.injection.AppComponent;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.domain.interactor.NewFeedUseCaseFactory;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.FeedModel;
import com.mike.feed.view.base.BaseFragment;
import com.mike.feed.viewmodel.NewFeedViewModel;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class NewFeedFragment extends BaseFragment implements NewFeedViewModel.DataListener {
    public static NewFeedFragment createInstance() {
        NewFeedFragment fragment = new NewFeedFragment();
        return fragment;
    }

    private static final String[] IMAGE_PATHS = new String[]{
            "https://i.ytimg.com/vi/1LqyOX12_nQ/maxresdefault.jpg"
            , "http://static.boredpanda.com/blog/wp-content/uuuploads/cute-baby-animals-2/cute-baby-animals-2-2.jpg"
            , "http://wallpapercave.com/wp/iyXgxue.jpg"
            , "http://1.bp.blogspot.com/-cCfO2hJ_x9Y/UD3zKqY_ueI/AAAAAAAAFmQ/egiQz9de-zc/s1600/cute-baby-animal-8.jpg"
            , "http://static.boredpanda.com/blog/wp-content/uploads/2016/01/baby-otter-sleeps-mother-belly-monterey-bay-aquarium-fb.png"
    };


    @Inject
    NewFeedViewModel mViewModel;

    private FragmentNewFeedBinding mBinding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater
                , R.layout.fragment_new_feed
                , container
                , false);
        return mBinding.lnContainer;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int imageIndex = (int) (Math.random() * IMAGE_PATHS.length);
        mViewModel.setImage(IMAGE_PATHS[imageIndex]);

        mBinding.setViewModel(mViewModel);



    }


    @Override
    public void close() {
        getActivity().finish();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.destroy();
    }


    /*----------------------------------------------- DJ -------------------------------------------------*/


    @Override
    protected void setUpComponent() {
        AppComponent appComponent = SquarApp.get(getActivity()).getAppComponent();
        NewFeedComponent component = appComponent.plus(new NewFeedModule(this));
        component.inject(this);
    }

    @FragmentScope
    @Subcomponent(
            modules = NewFeedModule.class
    )
    public static interface NewFeedComponent {
        public void inject(NewFeedFragment fragment);
    }

    @Module
    public static class NewFeedModule {

        NewFeedViewModel.DataListener mDataListener;

        public NewFeedModule(NewFeedViewModel.DataListener dataListener) {
            this.mDataListener = dataListener;
        }

        // Provide object that the fragment is needed here.
        @Provides
        public NewFeedViewModel provideNewFeedViewModel(Context context, NewFeedUseCaseFactory newFeedUseCaseFactory, FeedModelMapper mapper) {
            return new NewFeedViewModel(context, newFeedUseCaseFactory, mapper, mDataListener, new FeedModel());
        }
    }
}
