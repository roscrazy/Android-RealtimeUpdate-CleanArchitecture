package com.mike.feed.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mike.feed.R;
import com.mike.feed.model.FeedModel;
import com.mike.feed.util.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_feed, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, final int position) {
        final FeedModel model = mFeedModels.get(position);

        holder.tvBody.setText(model.getBody());
        holder.tvTitle.setText(model.getTitle());

        if(model.getImage() != null && !model.getImage().isEmpty()){
            holder.ivImage.setVisibility(View.VISIBLE);

            /**
             * We can set the default size depend on screen size over "value-swxxxdip" (I personal prefer this option).
             * Or we can use this method to get the image view size
             * holder.ivImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
             * @Override
             * public void onGlobalLayout() {
             *        mImageLoader.displayImage(holder.ivImage, model.getImage(), holder.ivImage.getMeasuredWidth(), 0);
             * }
             * });
             */
            int defaultSize = (int) holder.ivImage.getResources().getDimension(R.dimen.default_image_size);
            mImageLoader.displayImage(holder.ivImage, model.getImage(), defaultSize, defaultSize);
        }else{
            holder.ivImage.setVisibility(View.GONE);
        }

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnDeleteClicked != null)
                    mOnDeleteClicked.onDeleteClicked(model, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFeedModels != null ? mFeedModels.size() : 0;
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvBody)
        TextView tvBody;

        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.btDelete)
        View btDelete;

        public FeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }
}
