package com.triagung.moviecataloguefinal.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triagung.moviecataloguefinal.R;
import com.triagung.moviecataloguefinal.model.TvShow;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {
    private final Context mContext;
    private final ArrayList<TvShow> mData = new ArrayList<>();
    private OnItemClickCallback mOnItemClickCallback;

    public TvShowAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<TvShow> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        mOnItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_grid, viewGroup, false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TvShowViewHolder tvShowViewHolder, int i) {
        tvShowViewHolder.bind(mData.get(i));
        tvShowViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickCallback.onItemClicked(mData.get(tvShowViewHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPoster;
        private final TextView tvTitle;
        private final TextView tvYear;
        private final TextView tvRating;

        TvShowViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.img_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvYear = itemView.findViewById(R.id.tv_release_year);
            tvRating = itemView.findViewById(R.id.tv_rating);
        }

        void bind(TvShow itemTvShow) {
            Glide.with(mContext)
                    .load(itemTvShow.getPoster())
                    .into(imgPoster);
            tvTitle.setText(itemTvShow.getTitle());
            tvYear.setText(itemTvShow.getYear());
            tvRating.setText(String.valueOf(itemTvShow.getRating()));
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow tvShow);
    }
}
