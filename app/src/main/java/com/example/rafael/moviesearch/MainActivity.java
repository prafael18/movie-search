package com.example.rafael.moviesearch;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //

        FetchMoviesTask movieDataBase = new FetchMoviesTask();
        Toast.makeText(this, "We created a movie database withAsynch Task!", Toast.LENGTH_SHORT).show();
        movieDataBase.execute();

        Movies[] movieArray = movieDataBase.getMovieData();


     /*   grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "You clicked at", Toast.LENGTH_SHORT).show();
            }
        });*/

        //
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public class FetchMoviesTask extends AsyncTask<Void, Void, Movies[]> {
        private Movies[] movieData;

        public Movies[] getMovieData () {
            return movieData;
        }

        final String LOG_TAG = MainActivity.FetchMoviesTask.class.getSimpleName();

        private Movies[] getMovieDataFromJson (String movieDataJsonStr) throws JSONException {
            JSONObject movieDataJson = new JSONObject(movieDataJsonStr);
            JSONArray movieArray = movieDataJson.getJSONArray("results");

            movieData = new Movies[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject singleMovie = movieArray.getJSONObject(i);
                movieData[i] = new Movies (singleMovie.getString("id"),
                        singleMovie.getString("title"),
                        singleMovie.getString("poster_path"));
            }

            if (this.movieData == null) {
                Log.e("CustomGrid", "Still have null movie array");
            }

            return movieData;
        }


        @Override
        protected Movies[] doInBackground (Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String topRatedMoviesJsonStr = null;

            try {
                final String TOP_RATED_URL = "https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=28fb7eb8ba62f8aab91e287c94b323d4";
                // final String MOST_POPULAR_URL = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=28fb7eb8ba62f8aab91e287c94b323d4"

                //TODO: Add conditional if statement to choose betweeen links whenever you switch preference

                Uri builtUri = Uri.parse(TOP_RATED_URL);
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }


                if (buffer.length() == 0) {
                    return null;
                }

                topRatedMoviesJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e (LOG_TAG, "Error processing url", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e (LOG_TAG, "Error Closing Stream", e);
                    }

                }

            }



            //TODO: Add conditional statement to choose between methods depending on
            try {
                Movies[] movieArray = getMovieDataFromJson(topRatedMoviesJsonStr);
                return movieArray;
            } catch (JSONException e) {
                Log.e (LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        public void onPostExecute (Movies[] results) {
            CustomGrid mMovieAdapter = new CustomGrid(getApplicationContext(), results);
            GridView grid = (GridView)findViewById (R.id.grid);
            grid.setAdapter(mMovieAdapter);
            mMovieAdapter.notifyDataSetChanged();

        }
    }

}
