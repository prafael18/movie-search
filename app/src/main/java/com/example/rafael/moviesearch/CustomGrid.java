package com.example.rafael.moviesearch;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomGrid extends BaseAdapter {
    private Context context;
    private Movies[] movieData = null;
    private int size = 0;

    public CustomGrid(Context context, Movies[] movieData ) {
        this.context = context;
        this.movieData = new Movies[movieData.length];
        this.movieData = movieData;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub

        return movieData.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View gridView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
                gridView = new View(context);
                gridView = inflater.inflate(R.layout.grid_single, null);
                TextView textView = (TextView) gridView.findViewById(R.id.movie_title);
                ImageView imageView = (ImageView) gridView.findViewById(R.id.movie_poster);
                textView.setText(movieData[position].getTitle());
                String movieTitle = movieData[position].getTitle();
                String url = "https://image.tmdb.org/t/p/w500" + movieData[position].getPosterUrl();
                Picasso.with(context).load(url).into(imageView);


        } else {
            gridView = convertView;
        }

        return gridView;
    }
}
