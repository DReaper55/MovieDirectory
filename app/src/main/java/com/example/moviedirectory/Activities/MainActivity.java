package com.example.moviedirectory.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviedirectory.Data.MyAdapter;
import com.example.moviedirectory.Model.Movie;
import com.example.moviedirectory.R;
import com.example.moviedirectory.Utils.Constants;
import com.example.moviedirectory.Utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private EditText hintSearch;
    private Button submitSearch;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialogSetUp(view);

            }
        });

        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();

        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getString();
//        getMovie(search);

        movieList = getMovie(search);
        myAdapter = new MyAdapter(this, movieList);
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    // Get Movies

    public List<Movie> getMovie(String searchItem){
        movieList.clear();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL_LEFT + searchItem + Constants.URL_RIGHT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONArray moviesArray = response.getJSONArray("Search");

                    for(int i = 0; i < moviesArray.length(); i++){

                        JSONObject movieObj = moviesArray.getJSONObject(i);

                        Movie movie = new Movie();
                        movie.setTitle(movieObj.getString("Title"));
                        movie.setMovieType("Type: " + movieObj.getString("Type"));
                        movie.setYear("Release Date: " + movieObj.getString("Year"));
                        movie.setPoster(movieObj.getString("Poster"));
                        movie.setImdbId(movieObj.getString("imdbID"));


//                        Log.d("Movies: ", movie.getTitle());

                        movieList.add(movie);
                    }

                    myAdapter.notifyDataSetChanged();

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(objectRequest);

        return movieList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {

            View view = new View(this);
            alertDialogSetUp(view);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void alertDialogSetUp(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        view = getLayoutInflater().inflate(R.layout.movie_search, null);

        hintSearch = view.findViewById(R.id.hintSearchID);
        submitSearch = view.findViewById(R.id.submitSearchID);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        submitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs = new Prefs(MainActivity.this);

                if(!hintSearch.getText().toString().isEmpty()){

                    String hint = hintSearch.getText().toString();
                    prefs.setString(hint);
                    movieList.clear();

                    getMovie(hint);

                }
                dialog.dismiss();
//                recreate();
            }
        });

    }
}
