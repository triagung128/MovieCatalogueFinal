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
import com.triagung.moviecataloguefinal.model.Movie;

import java.util.ArrayList;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder> {
    private final ArrayList<Movie> mListMovieFavorite = new ArrayList<>();
    private final Activity mActivity;
    private OnItemClickCallback mOnItemClickCallback;

    public FavoriteMovieAdapter(Activity activity) {
        mActivity = activity;
    }

    public ArrayList<Movie> getListMovieFavorite() {
        return mListMovieFavorite;
    }

    public void setListMovieFavorite(ArrayList<Movie> listMovieFavorite) {
        if (listMovieFavorite.size() > 0) mListMovieFavorite.clear();
        mListMovieFavorite.addAll(listMovieFavorite);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        mOnItemClickCallback = onItemClickCallback;
    }

    public void removeItem(int position) {
        mListMovieFavorite.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mListMovieFavorite.size());
    }

    @NonNull
    @Override
    public FavoriteMovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_favorite, viewGroup, false);
        return new FavoriteMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteMovieViewHolder favoriteMovieViewHolder, int i) {
        Glide.with(mActivity)
                .load(mListMovieFavorite.get(i).getPoster())
                .into(favoriteMovieViewHolder.imgPoster);

        favoriteMovieViewHolder.tvTitle.setText(mListMovieFavorite.get(i).getTitle());
        favoriteMovieViewHolder.tvReleaseDate.setText(mListMovieFavorite.get(i).getYear());
        favoriteMovieViewHolder.tvOverview.setText(mListMovieFavorite.get(i).getOverview());
        favoriteMovieViewHolder.rbMovie.setRating((float) mListMovieFavorite.get(i).getRating() / 2);

        favoriteMovieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickCallback.onItemClicked(
                        mListMovieFavorite.get(favoriteMovieViewHolder.getAdapterPosition()),
                        favoriteMovieViewHolder.getAdapterPosition()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListMovieFavorite.size();
    }

    class FavoriteMovieViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgPoster;
        final TextView tvTitle;
        final TextView tvReleaseDate;
        final TextView tvOverview;
        final AppCompatRatingBar rbMovie;

        FavoriteMovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.img_poster_favorite);
            tvTitle = itemView.findViewById(R.id.tv_title_favorite);
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date_favorite);
            tvOverview = itemView.findViewById(R.id.tv_overview_favorite);
            rbMovie = itemView.findViewById(R.id.rb_favorite);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie movie, int position);
    }
}
