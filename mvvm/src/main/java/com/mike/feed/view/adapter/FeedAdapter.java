package com.mike.feed.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mike.feed.R;
import com.mike.feed.databinding.ViewItemFeedBinding;
import com.mike.feed.model.FeedModel;
import com.mike.feed.util.ImageLoader;
import com.mike.feed.viewmodel.FeedItemViewModel;

import java.util.List;

/**
 * Created by MinhNguyen on 8/27/16.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    public static interface OnDeleteClicked{
        public void onDeleteClicked(FeedModel feed, int index);
    }

    private List<FeedModel> mFeedModels;

    private ImageLoader mImageLoader;

    private OnDeleteClicked mOnDeleteClicked;

    public FeedAdapter(List<FeedModel> feedModels, ImageLoader imageLoader) {
        this.mFeedModels = feedModels;
        this.mImageLoader = imageLoader;
    }


    public void setOnDeleteClicked(OnDeleteClicked mOnDeleteClicked) {
        this.mOnDeleteClicked = mOnDeleteClicked;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewItemFeedBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.view_item_feed
                , parent
                , false);

        return new FeedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, final int position) {
        final FeedModel model = mFeedModels.get(position);
        holder.bindFeed(model, mOnDeleteClicked, position, mImageLoader);
    }


    public List<FeedModel> getFeedModels(){
        return mFeedModels;
    }

    @Override
    public int getItemCount() {
        return mFeedModels != null ? mFeedModels.size() : 0;
    }



    public static class FeedViewHolder extends RecyclerView.ViewHolder{

        ViewItemFeedBinding mBinding;

        public FeedViewHolder(ViewItemFeedBinding binding) {
            super(binding.cardView);
            this.mBinding = binding;
        }

        public void bindFeed(FeedModel feedModel,
                             FeedAdapter.OnDeleteClicked onDeleteClicked, int position, ImageLoader imageLoader){

            if(this.mBinding.getViewModel() == null){
                this.mBinding.setViewModel(new FeedItemViewModel(feedModel, onDeleteClicked, position));
            }else{
                this.mBinding.getViewModel().setFeedModel(feedModel);
            }


            int defaultSize = (int) this.mBinding.ivImage.getResources().getDimension(R.dimen.default_image_size);

            /**
             * Follow MVVM, We should download the image and put to the ViewModel -> notify the FeedViewHolder that image have changed -> imageview will be updated
             * But in my point of view, we should use this imageloader as an exception for a better performance.
             */
            imageLoader.displayImage(mBinding.ivImage, feedModel.getImage(), defaultSize, defaultSize);

        }


    }
}
