package com.grupoelite.peliculas2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Mikel Gil on 26/04/2016.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    Context mContext;
    Peliculas mPeliculas;

    OnItemClickListener mItemClickListener;

    public MovieListAdapter(Context context, Peliculas peliculas) {
        this.mContext = context;
        this.mPeliculas = peliculas;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public LinearLayout movieHolder;
        public LinearLayout movieNameHolder;
        public TextView movieName;
        public ImageView movieImage;

        public ViewHolder(View itemView) {
            super(itemView);
            movieHolder = (LinearLayout)itemView.findViewById(R.id.mainHolder);
            movieName = (TextView)itemView.findViewById(R.id.movieName);
            movieNameHolder = (LinearLayout)itemView.findViewById(R.id.movieNameHolder);
            movieImage = (ImageView)itemView.findViewById(R.id.movieImage);

            movieHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mPeliculas.getData().size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Pelicula pelicula = mPeliculas.getData().get(position);
        holder.movieName.setText(pelicula.getMovie());

        Picasso.with(mContext)
            .load(pelicula.getPoster())
            .into(holder.movieImage, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) holder.movieImage.getDrawable()).getBitmap();
                    // do your processing here....

                    Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {
                            int mutedLight = palette.getMutedColor(mContext.getResources().getColor(android.R.color.black));
                            holder.movieNameHolder.setBackgroundColor(mutedLight);
                        }
                    });
                }
                @Override
                public void onError() {
                    // reset your views to default colors, etc.
                }
            });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


}

