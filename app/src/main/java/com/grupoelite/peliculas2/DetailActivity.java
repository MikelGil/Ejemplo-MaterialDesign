package com.grupoelite.peliculas2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mikel Gil on 26/04/2016.
 */

public class DetailActivity extends Activity implements View.OnClickListener{

    //Variables
    public static final String EXTRA_PARAM_PELICULA = "asdf";
    private ImageView mImageView;
    private TextView mTitle;
    private TextView mDirector;
    private TextView mCast;
    private VideoView mTrailer;
    private ImageButton mAddButton;
    private Pelicula pelicula;

    private boolean isEditTextVisible;
    private LinearLayout mRevealView;
    private EditText mEditTextTodo;
    private InputMethodManager mInputManager;

    private Animatable mAnimatable;

    private LinearLayout mTitleHolder;
    private Palette mPalette;
    int defaultColorForRipple;

    private ListView mList;
    private ArrayList<String> mTodoList;
    private ArrayAdapter mToDoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Obtiene la pelicula desde el activity main
        pelicula = (Pelicula) getIntent().getSerializableExtra(EXTRA_PARAM_PELICULA);

        mImageView = (ImageView) findViewById(R.id.movieImage);
        mTitle = (TextView) findViewById(R.id.textView);
        mDirector = (TextView) findViewById(R.id.director);
        mCast = (TextView) findViewById(R.id.cast);
        mTrailer = (VideoView)findViewById(R.id.trailer);
        mAddButton = (ImageButton) findViewById(R.id.btn_add);
        mAddButton.setImageResource(R.drawable.icn_morph_reverse);

        mAddButton.setOnClickListener(this);
        mRevealView = (LinearLayout)findViewById(R.id.llEditTextHolder);
        mEditTextTodo = (EditText) findViewById(R.id.etTodo);
        mInputManager = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        isEditTextVisible = false;

        mTitleHolder = (LinearLayout)findViewById(R.id.movieNameHolder);
        defaultColorForRipple =getResources().getColor(R.color.primary_dark);

        mList = (ListView) findViewById(R.id.list);
        mTodoList = new ArrayList<>();
        mToDoAdapter = new ArrayAdapter(this, R.layout.row_todo, mTodoList);
        mList.setAdapter(mToDoAdapter);


        loadMovie();
        getPhoto();
    }

    //Metodo que carga los datos de la pelicula
    private void loadMovie() {
        mTitle.setText(pelicula.getMovie());
        mDirector.setText("DIRECTOR: " + pelicula.getDirector());
        mCast.setText("CAST: " + pelicula.getCast());
        try {
            String link= pelicula.getTrailer();
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(mTrailer);
            Uri video = Uri.parse(link);
            mTrailer.setMediaController(mediaController);
            mTrailer.setVideoURI(video);
            //mTrailer.start();
        } catch (Exception e) {
            Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }

        Picasso.with(DetailActivity.this).load(pelicula.getPoster()).into(mImageView);
    }

    //Metodo que muestra un editText oculto al darle al + de la pantalla
    private void revealEditText(LinearLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        isEditTextVisible = true;
        anim.start();
    }

    //Metodo que oculta un editText al darle al - de la pantalla
    private void hideEditText(final LinearLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int initialRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        isEditTextVisible = false;
        anim.start();
    }

    //Metodo que controla el editText para añadir nuevos datos
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (!isEditTextVisible) {
                    revealEditText(mRevealView);
                    mEditTextTodo.requestFocus();
                    mInputManager.showSoftInput(mEditTextTodo, InputMethodManager.SHOW_IMPLICIT);
                    mAddButton.setImageResource(R.drawable.icn_morp);

                    applyRippleColor(getResources().getColor(R.color.light_green), getResources().getColor(R.color.dark_green));

                    mAnimatable = (Animatable) (mAddButton).getDrawable();
                    mAnimatable.start();
                } else {
                    hideEditText(mRevealView);
                    mAddButton.setImageResource(R.drawable.icn_morph_reverse);

                    applyRippleColor(mPalette.getVibrantColor(defaultColorForRipple), mPalette.getDarkVibrantColor(defaultColorForRipple));

                    addToDo(mEditTextTodo.getText().toString());
                    mToDoAdapter.notifyDataSetChanged();
                    mInputManager.hideSoftInputFromWindow(mEditTextTodo.getWindowToken(), 0);

                    mAnimatable = (Animatable) (mAddButton).getDrawable();
                    mAnimatable.start();
                }
        }
    }

    //Metodo que convierte el poster de la pelicula en un BitmapDrawable para mostrarlo en pantalla
    private void getPhoto( ) {
        Picasso.with(DetailActivity.this)
            .load(pelicula.getPoster())
            .into(mImageView, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
                    // do your processing here....

                    colorize(bitmap);
                }

                @Override
                public void onError() {
                    // reset your views to default colors, etc.
                }
            });
    }
    private void colorize(Bitmap photo) {
        mPalette = Palette.generate(photo);
        applyPalette();
    }
    private void applyPalette() {
        getWindow().setBackgroundDrawable(new ColorDrawable(mPalette.getDarkMutedColor(defaultColorForRipple)));
        mTitleHolder.setBackgroundColor(mPalette.getMutedColor(defaultColorForRipple));
        applyRippleColor(mPalette.getVibrantColor(defaultColorForRipple),mPalette.getDarkVibrantColor(defaultColorForRipple));
        mRevealView.setBackgroundColor(mPalette.getLightVibrantColor(defaultColorForRipple));
    }
    private void applyRippleColor(int bgColor, int tintColor) {
        colorRipple(mAddButton, bgColor, tintColor);
    }
    private void colorRipple(ImageButton id, int bgColor, int tintColor) {
        View buttonView = id;
        RippleDrawable ripple = (RippleDrawable) buttonView.getBackground();
        GradientDrawable rippleBackground = (GradientDrawable) ripple.getDrawable(0);
        rippleBackground.setColor(bgColor);
        ripple.setColor(ColorStateList.valueOf(tintColor));
    }

    private void addToDo(String todo) {
        mTodoList.add(todo);
    }

    public void sendText(View v){
        Uri imageUri = Uri.parse(pelicula.getPoster());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, mEditTextTodo.getText().toString());

        switch (v.getId()){
            case R.id.btnTwitter:
                intent.setClassName("com.twitter.android", "com.twitter.android.composer.ComposerActivity");
                break;
            case R.id.btnFacebook:
                intent.setClassName("com.facebook.katana","com.facebook.katana.ShareLinkActivity");
                break;
            case R.id.btnWhatsapp:
                intent.setPackage("com.whatsapp");
                break;
        }
        startActivity(intent);
    }
}

