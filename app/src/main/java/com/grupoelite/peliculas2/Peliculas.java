package com.grupoelite.peliculas2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikel Gil on 26/04/2016.
 */
public class Peliculas{

    private List<Pelicula> data = new ArrayList<Pelicula>();
    public List<Pelicula> getData() {
        return data;
    }

    public void setData(List<Pelicula> data) {
        this.data = data;
    }

    public Pelicula getPelicula (int position){
        return data.get(position);
    }

}
