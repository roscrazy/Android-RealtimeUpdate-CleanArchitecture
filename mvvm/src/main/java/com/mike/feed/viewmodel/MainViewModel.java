package com.mike.feed.viewmodel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mike.feed.model.FeedModel;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import uk.ivanc.archimvvm.ArchiApplication;
import uk.ivanc.archimvvm.R;
import uk.ivanc.archimvvm.model.GithubService;
import uk.ivanc.archimvvm.model.Repository;

/**
 * View model for the MainFragment
 */
public class MainViewModel extends BaseViewModel implements ViewModel {

    private static final String TAG = "MainViewModel";

    public ObservableInt recyclerViewVisibility;

    private Context context;

    private List<FeedModel> feedModels;

    private DataListener dataListener;
    private String editTextUsernameValue;

    public MainViewModel(Context context, DataListener dataListener) {
        this.context = context;
        this.dataListener = dataListener;
        recyclerViewVisibility = new ObservableInt(View.INVISIBLE);
    }



    @Override
    public void destroy() {
        super.destroy();

        context = null;
        dataListener = null;
    }


    public void onClickSearch(View view) {

    }



    public void registerDataChange(String username) {

    }


    public interface DataListener {

        void removeItem(int index);

        void moveItem(int oldIndex, int newIndex);

        void updateItem(FeedModel data, int index);

        void addItem(FeedModel data, int index);

        void showErrorMsg();
    }
}
