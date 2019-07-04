package com.example.moviedirectory.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviedirectory.Model.Movie;
import com.example.moviedirectory.R;
import com.example.moviedirectory.Utils.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetails extends AppCompatActivity {

    private Movie movie;
    private ImageView movieImage;
    private TextView movieTitle;
    private TextView releaseDate;
    private TextView category;
    private TextView rating;
    private TextView runtime;
    private TextView director;
    private TextView actors;
    private TextView writer;
    private TextView plot;
    private TextView boxOffice;

    private RequestQueue queue;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_movie_details);

        queue = Volley.newRequestQueue(this);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        movieId = movie.getImdbId();

        setUp();
        movieDetails(movieId);
    }

    private void movieDetails(String Id) {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL + Id + Constants.KEY, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response.has("Ratings")){
                        JSONArray ratings = response.getJSONArray("Ratings");

                        String source;
                        String value;

                        if(ratings.length() > 0){
                            JSONObject mRatings = ratings.getJSONObject(ratings.length() - 1);

                            source = mRatings.getString("Source");
                            value = mRatings.getString("Value");

                            rating.setText(source + " : " + value);
                        } else {
                            rating.setText("Ratings: N/A");
                        }

                        movieTitle.setText("Title: " + response.getString("Title"));
                        releaseDate.setText("Released: " + response.getString("Year"));
                        category.setText("Category: " + response.getString("Type"));
                        runtime.setText("Runtime: " + response.getString("Runtime"));
                        director.setText("Directors: " + response.getString("Director"));
                        writer.setText("Writers: " + response.getString("Writer"));
                        plot.setText("Plot: " + response.getString("Plot"));
                        actors.setText("Actors: " + response.getString("Actors"));
                        boxOffice.setText("Box Office: " + response.getString("BoxOffice"));

                        Picasso.with(getApplicationContext())
                                .load(response.getString("Poster"))
                                .into(movieImage);

                    }


                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Error", error.getMessage());
            }
        });

        queue.add(objectRequest);
    }

    private void setUp() {
        movieImage = findViewById(R.id.imageViewIDDets);
        movieTitle = findViewById(R.id.movieTitleIDDets);
        releaseDate = findViewById(R.id.movieReleaseIDDets);
        category = findViewById(R.id.movieCatIDDets);
        rating = findViewById(R.id.movieRatingIDDets);
        runtime = findViewById(R.id.movieRuntimeIDDets);
        director = findViewById(R.id.movieDirectorID);
        actors = findViewById(R.id.movieActorsIDDets);
        writer = findViewById(R.id.movieWritersIDDets);
        plot = findViewById(R.id.moviePlotIDDets);
        boxOffice = findViewById(R.id.movieBoxIDDets);
    }


}
