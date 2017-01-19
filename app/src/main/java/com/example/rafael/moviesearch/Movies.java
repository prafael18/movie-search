package com.example.rafael.moviesearch;

/**
 * Created by rafael on 18/01/17.
 */

public class Movies {
    private String id, title, posterUrl;

    public String getId () {
        return id;
    }

    public String getTitle () {
        return title;
    }

    public String getPosterUrl () {
        return posterUrl;
    }
    public Movies (String id, String title, String posterUrl) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
    }
}
