package com.mike.feed.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mike.feed.R;
import com.mike.feed.SquarApp;
import com.mike.feed.databinding.FragmentMainBinding;
import com.mike.feed.dependency.injection.AppComponent;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.domain.Feed;
import com.mike.feed.domain.interactor.DeleteFeedUseCaseFactory;
import com.mike.feed.domain.interactor.FeedChangedUseCase;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.FeedModel;
import com.mike.feed.util.ImageLoader;
import com.mike.feed.view.adapter.FeedAdapter;
import com.mike.feed.view.base.BaseFragment;
import com.mike.feed.viewmodel.MainViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

/**
 * Created by MinhNguyen on 8/25/16.
 */
public class MainFragment extends BaseFragment implements MainViewModel.DataListener {

    public static MainFragment createInstance() {
        return new MainFragment();
    }


    @Inject
    ImageLoader mImageLoader;

    @Inject
    MainViewModel mViewModel;

    private FragmentMainBinding mBinding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater
                , R.layout.fragment_main
                , container
                , false);

        return mBinding.frameLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.setViewModel(mViewModel);
        mViewModel.init();
        setUpRecyclerView(mBinding.recyclerView);
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        // Set up Layout Manager, reverse layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        FeedAdapter adapter = new FeedAdapter(mViewModel.getFeeds(), mImageLoader);
        adapter.setOnDeleteClicked(mViewModel);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void removeItem(int index) {
        FeedAdapter adapter =
                (FeedAdapter) mBinding.recyclerView.getAdapter();
        adapter.notifyItemRemoved(index);
    }

    @Override
    public void moveItem(int oldIndex, int newIndex) {
        FeedAdapter adapter =
                (FeedAdapter) mBinding.recyclerView.getAdapter();
        adapter.notifyItemMoved(oldIndex, newIndex);
    }

    @Override
    public void updateItem(FeedModel data, int index) {
        FeedAdapter adapter =
                (FeedAdapter) mBinding.recyclerView.getAdapter();
        adapter.notifyItemChanged(index);
    }

    @Override
    public void addItem(FeedModel data, int index) {
        FeedAdapter adapter =
                (FeedAdapter) mBinding.recyclerView.getAdapter();
        adapter.notifyItemInserted(index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageLoader.unsubscribe();
    }

    @Override
    public void showErrorMsg() {
        Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_LONG).show();
    }

    /*----------------------------------------------- DJ -------------------------------------------------*/
    @Override
    protected void setUpComponent() {
        AppComponent appComponent = SquarApp.get(getActivity()).getAppComponent();
        MainScreenComponent component = appComponent.plus(new MainScreenModule(this));
        component.inject(this);
    }

    @FragmentScope
    @Subcomponent(
            modules = MainScreenModule.class
    )
    public static interface MainScreenComponent {
        public void inject(MainFragment fragment);
    }

    @Module
    public static class MainScreenModule {
        MainViewModel.DataListener mDataListener;

        public MainScreenModule(MainViewModel.DataListener dataListener) {
            this.mDataListener = dataListener;
        }

        @Provides
        public MainViewModel provideMainPresenter(FeedChangedUseCase feedChangedUseCase, FeedModelMapper mapper, DeleteFeedUseCaseFactory deleteFeedUseCaseFactory) {
            return new MainViewModel(mDataListener, new ArrayList<String>(), deleteFeedUseCaseFactory, feedChangedUseCase, mapper, new ArrayList<FeedModel>());
        }
    }
}
