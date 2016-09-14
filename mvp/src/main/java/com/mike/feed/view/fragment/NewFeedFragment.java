package com.mike.feed.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mike.feed.R;
import com.mike.feed.FeedApp;
import com.mike.feed.dependency.injection.AppComponent;
import com.mike.feed.dependency.injection.scope.FragmentScope;
import com.mike.feed.presenter.NewFeedPresenter;
import com.mike.feed.view.NewFeedView;
import com.mike.feed.view.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.Module;
import dagger.Subcomponent;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class NewFeedFragment extends BaseFragment implements NewFeedView{
    public static NewFeedFragment createInstance(){
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

    @BindView(R.id.etTitle)
    EditText etTitle;

    @BindView(R.id.etBody)
    EditText etBody;

    @BindView(R.id.etImage)
    EditText etImage;

    @Inject
    NewFeedPresenter presenter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_feed, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.bindView(this);

        int imageIndex = (int)(Math.random() * IMAGE_PATHS.length);

        etImage.setText(IMAGE_PATHS[imageIndex]);
    }

    @OnClick(R.id.btSubmit)
    public void onSubmitClicked(){
        String title = etTitle.getText().toString();
        String body = etBody.getText().toString();
        String image = etImage.getText().toString();

        presenter.submit(title, body, image);
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    @Override
    public void showErrorMsg() {
        Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unbindView(this);
    }


    /*----------------------------------------------- DJ -------------------------------------------------*/


    @Override
    protected void setUpComponent() {
        AppComponent appComponent = FeedApp.get(getActivity()).getAppComponent();
        NewFeedComponent component = appComponent.plus(new NewFeedModule());
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
        // Provide object that the fragment is needed here.
    }
}
