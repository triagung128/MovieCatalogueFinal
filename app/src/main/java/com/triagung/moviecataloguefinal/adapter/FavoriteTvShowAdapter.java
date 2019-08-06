package com.triagung.moviecataloguefinal.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
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

public class FavoriteTvShowAdapter extends RecyclerView.Adapter<FavoriteTvShowAdapter.FavoriteTvShowViewHolder> {
    private final ArrayList<TvShow> mListTvShowFavorite = new ArrayList<>();
    private final Activity mActivity;
    private OnItemClickCallback mOnItemClickCallback;

    public FavoriteTvShowAdapter(Activity activity) {
        mActivity = activity;
    }

    public ArrayList<TvShow> getListTvShowFavorite() {
        return mListTvShowFavorite;
    }

    public void setListTvShowFavorite(ArrayList<TvShow> listTvShowFavorite) {
        if (listTvShowFavorite.size() > 0) mListTvShowFavorite.clear();
        mListTvShowFavorite.addAll(listTvShowFavorite);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        mOnItemClickCallback = onItemClickCallback;
    }

    public void removeItem(int position) {
        mListTvShowFavorite.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mListTvShowFavorite.size());
    }

    @NonNull
    @Override
    public FavoriteTvShowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_favorite, viewGroup, false);
        return new FavoriteTvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteTvShowViewHolder favoriteTvShowViewHolder, int i) {
        Glide.with(mActivity)
                .load(mListTvShowFavorite.get(i).getPoster())
                .into(favoriteTvShowViewHolder.imgPoster);

        favoriteTvShowViewHolder.tvTitle.setText(mListTvShowFavorite.get(i).getTitle());
        favoriteTvShowViewHolder.tvReleaseDate.setText(mListTvShowFavorite.get(i).getYear());
        favoriteTvShowViewHolder.tvOverview.setText(mListTvShowFavorite.get(i).getOverview());
        favoriteTvShowViewHolder.rbTvShow.setRating((float) mListTvShowFavorite.get(i).getRating() / 2);

        favoriteTvShowViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickCallback.onItemClicked(
                        mListTvShowFavorite.get(favoriteTvShowViewHolder.getAdapterPosition()),
                        favoriteTvShowViewHolder.getAdapterPosition()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListTvShowFavorite.size();
    }

    class FavoriteTvShowViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgPoster;
        final TextView tvTitle;
        final TextView tvReleaseDate;
        final TextView tvOverview;
        final AppCompatRatingBar rbTvShow;

        FavoriteTvShowViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.img_poster_favorite);
            tvTitle = itemView.findViewById(R.id.tv_title_favorite);
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date_favorite);
            tvOverview = itemView.findViewById(R.id.tv_overview_favorite);
            rbTvShow = itemView.findViewById(R.id.rb_favorite);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow tvShow, int position);
    }
}
