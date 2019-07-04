package com.example.moviedirectory.Data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviedirectory.Activities.MovieDetails;
import com.example.moviedirectory.Model.Movie;
import com.example.moviedirectory.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movieList;

    public MyAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_row, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i) {

        Movie movie = movieList.get(i);
        String posterLink = movie.getPoster();

        viewHolder.title.setText(movie.getTitle());
        Picasso.with(context)
                .load(posterLink)
                .placeholder(android.R.drawable.btn_star)
                .into(viewHolder.poster);
        viewHolder.year.setText(movie.getYear());
        viewHolder.type.setText(movie.getMovieType());

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView poster;
        TextView year;
        TextView type;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.movieTitleID);
            poster = itemView.findViewById(R.id.imageViewID);
            year = itemView.findViewById(R.id.movieReleaseID);
            type = itemView.findViewById(R.id.movieCatID);
        }

        @Override
        public void onClick(View v) {
            Movie movie = movieList.get(getAdapterPosition());

            Intent intent = new Intent(context, MovieDetails.class);
            intent.putExtra("movie", movie);

            context.startActivity(intent);
        }
    }
}
