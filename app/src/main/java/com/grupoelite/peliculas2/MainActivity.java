package com.grupoelite.peliculas2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.google.gson.Gson;

/**
 * Created by Mikel Gil on 26/04/2016.
 */

public class MainActivity extends Activity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    private MovieListAdapter mAdapter;
    private boolean isListView;
    private Menu menu;
    Peliculas peliculas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpActionBar();
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        Consulta();
    }

    MovieListAdapter.OnItemClickListener onItemClickListener = new MovieListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Pelicula miPelicula = peliculas.getData().get(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_PARAM_PELICULA,miPelicula);

            ImageView movieImage = (ImageView)v.findViewById(R.id.movieImage);
            LinearLayout movieNameHolder = (LinearLayout) v.findViewById(R.id.movieNameHolder);
            Pair<View, String> imagePair = Pair.create((View) movieImage,"tImage");
            Pair<View, String> holderPair = Pair.create((View) movieNameHolder, "tNameHolder");
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imagePair, holderPair);

            ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
        }
    };

    //Metodo que hace la consulta del JSON
    public void Consulta(){
        ProcessJSON consulta = new ProcessJSON();
        consulta.execute("https://github.com/MikelGil/Ejemplo-MaterialDesign/master/cine.JSON");
    }

    //Metodo para tratar la información del archivo JSON
    private class ProcessJSON extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];
            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);
            // Return the data from specified url
            return stream;
        }
        protected void onPostExecute(String stream){
            //..........Process JSON DATA................
            if(stream !=null){
                //Se crean las peliculas desde el archivo JSON usando la libreria de terceros Gson
                final Gson gson = new Gson();
                peliculas = gson.fromJson(stream, Peliculas.class);

                mRecyclerView.setHasFixedSize(true);
                //Se crea el adaptador MovieListAdapter usando la lista de peliculas.
                mAdapter = new MovieListAdapter(MainActivity.this, peliculas);

                //Se crea la vista usando el adaptador
                mRecyclerView.setAdapter(mAdapter);

                //Se añaden "escuchadores" a todas las filas para llamar a un metodo al hacer click en ella
                mAdapter.setOnItemClickListener(onItemClickListener);
                isListView = true;
            }
        }
    }

    //Metodo para añadir un action bar
    private void setUpActionBar() {
        if (toolbar != null) {
            setActionBar(toolbar);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(true);
            getActionBar().setElevation(7);
        }
    }

    //Metodo para crear el menu de 3 filas que aparecera en el action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_toggle) {
            toggle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Metodo que controla que la vista se vea tipo lista o tipo grid
    private void toggle() {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (isListView) {
            mStaggeredLayoutManager.setSpanCount(2);
            item.setIcon(R.drawable.ic_action_list);
            item.setTitle("Show as list");
            isListView = false;
        } else {
            mStaggeredLayoutManager.setSpanCount(1);
            item.setIcon(R.drawable.ic_action_grid);
            item.setTitle("Show as grid");
            isListView = true;
        }
    }

}
