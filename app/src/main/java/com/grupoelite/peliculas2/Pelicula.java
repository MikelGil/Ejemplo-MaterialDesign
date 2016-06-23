package com.grupoelite.peliculas2;

import java.io.Serializable;

/**
 * Created by Mikel Gil on 26/04/2016.
 */
public class Pelicula implements Serializable {
    private String movie;
    private String year;
    private String director;
    private String cast;
    private String poster;
    private String image;
    private String trailer;

    public String getMovie() {
        return movie;
    }
    public void setMovie(String movie) {
        this.movie = movie;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getDirector() { return director; }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getCast() {
        return cast;
    }
    public void setCast(String cast) {
        this.cast = cast;
    }
    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getTrailer() {
        return trailer;
    }
    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
}
