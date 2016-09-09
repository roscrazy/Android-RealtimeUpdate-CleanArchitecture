package com.mike.feed.view.fragment;

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
import com.mike.feed.dependency.injection.AppComponent;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.domain.interactor.DeleteFeedUseCaseFactory;
import com.mike.feed.domain.interactor.FeedChangedUseCase;
import com.mike.feed.mapper.FeedModelMapper;
import com.mike.feed.model.FeedModel;
import com.mike.feed.presenter.MainPresenter;
import com.mike.feed.util.ImageLoader;
import com.mike.feed.view.MainView;
import com.mike.feed.view.adapter.FeedAdapter;
import com.mike.feed.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

/**
 * Created by MinhNguyen on 8/25/16.
 */
public class MainFragment extends BaseFragment implements MainView, FeedAdapter.OnDeleteClicked {

    public static MainFragment createInstance(){
        return new MainFragment();
    }

    @Inject
    MainPresenter mPresenter;

    @Inject
    ImageLoader mImageLoader;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    List<FeedModel> mFeedModels;

    FeedAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.bindView(this);
        mPresenter.init();

        // Set up Layout Manager, reverse layout
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mFeedModels = new ArrayList<>();
        mAdapter = new FeedAdapter(mFeedModels, mImageLoader);
        mAdapter.setOnDeleteClicked(this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onDeleteClicked(FeedModel feed, int index) {
        mPresenter.deleteFeed(feed, index);
    }

    @Override
    public void removeItem(int index) {
        if(index > -1 && mFeedModels.size() > index) {
            mFeedModels.remove(index);
            mAdapter.notifyItemRemoved(index);
        }
    }

    @Override
    public void moveItem(int oldIndex, int newIndex) {
        FeedModel item = mFeedModels.remove(oldIndex);
        if(item != null) {
            mFeedModels.add(newIndex, item);
            mAdapter.notifyItemMoved(oldIndex, newIndex);
        }
    }

    @Override
    public void updateItem(FeedModel data, int index) {
        if(index >= 0 && mFeedModels.size() > index){
            mFeedModels.set(index, data);
            mAdapter.notifyItemChanged(index);
        }
    }

    @Override
    public void addItem(FeedModel data, int index) {
        if(index >= 0){
            mFeedModels.add(index, data);
            mAdapter.notifyItemInserted(index);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unbindView(this);
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
        MainScreenComponent component = appComponent.plus(new MainScreenModule());
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
        // provide fragment needed object here
        @Provides
        public MainPresenter provideMainPresenter(FeedChangedUseCase feedChangedUseCase, FeedModelMapper mapper, DeleteFeedUseCaseFactory deleteFeedUseCaseFactory) {
            return new MainPresenter(feedChangedUseCase, mapper, deleteFeedUseCaseFactory, new ArrayList<String>());
        }
    }
}
