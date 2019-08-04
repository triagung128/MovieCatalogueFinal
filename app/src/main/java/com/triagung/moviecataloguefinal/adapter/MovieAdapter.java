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
import com.triagung.moviecataloguefinal.model.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ListItemViewHolder> {
    private final Context mContext;
    private final ArrayList<Movie> mData = new ArrayList<>();
    private OnItemClickCallback mOnItemClickCallback;

    public MovieAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<Movie> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        mOnItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_grid, viewGroup, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListItemViewHolder listItemViewHolder, int i) {
        listItemViewHolder.bind(mData.get(i));
        listItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickCallback.onItemClicked(mData.get(listItemViewHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPoster;
        private final TextView tvTitle;
        private final TextView tvYear;
        private final TextView tvRating;

        ListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.img_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvYear = itemView.findViewById(R.id.tv_release_year);
            tvRating = itemView.findViewById(R.id.tv_rating);
        }

        void bind(Movie itemMovie) {
            Glide.with(mContext)
                    .load(itemMovie.getPoster())
                    .into(imgPoster);
            tvTitle.setText(itemMovie.getTitle());
            tvYear.setText(itemMovie.getYear());
            tvRating.setText(String.valueOf(itemMovie.getRating()));
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie movie);
    }
}
