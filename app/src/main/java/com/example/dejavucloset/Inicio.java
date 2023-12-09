package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.example.dejavucloset.iniciosesion.IniciarSesion;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.dejavucloset.carrito.Carrito;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Inicio extends AppCompatActivity {
    //Para video
    private VideoView videoView;
    private ImageButton btnPlay, btnStop, btnPause, btnUno, btnDos, btnTres, ibLlamar;
    private ImageView imgInicio, imgVideo;
    private int posicion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        //Bottom Navegation View
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //Con inicio seleeccionado
        navigation.setSelectedItemId(R.id.inicio);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        break;
                    case R.id.productos:
                        Intent a = new Intent(Inicio.this,Categorias.class);
                        startActivity(a);
                        break;
                    case R.id.perfil:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            Intent intent = new Intent(Inicio.this, Perfil.class);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(Inicio.this, IniciarSesion.class);
                            startActivity(intent);

                        };
                        break;
                }
                return false;
            }
        });


        //****Para el video
        videoView= (VideoView) findViewById(R.id.videoinicio);
        String path= "android.resource://"+getPackageName()+ "/"+R.raw.vidini;
        videoView.setVideoURI(Uri.parse(path));
        btnPlay= (ImageButton) findViewById(R.id.btnPlay);
        btnPause= (ImageButton) findViewById(R.id.btnPausar);
        btnStop= (ImageButton) findViewById(R.id.btnDetener);
        btnUno=(ImageButton) findViewById(R.id.btnUno);
        btnDos=(ImageButton) findViewById(R.id.btnDos);
        btnTres=(ImageButton) findViewById(R.id.btnTres);
        imgInicio=(ImageView) findViewById(R.id.imgInicio);
        imgVideo= (ImageView)findViewById(R.id.imgVideo);

        videoView.setVisibility(View.INVISIBLE);
    }

    //Carrito
    public void irCarrito(View view){
        Intent i=new Intent(this, Carrito.class);
        startActivity(i);
    }

    //******Metodo del Video
    public void play(View view) {
        imgVideo.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.VISIBLE);
        if(posicion==0){
            videoView.seekTo(0);
            videoView.start();
        } else{
            int length=videoView.getCurrentPosition();
            videoView.seekTo(length);
            videoView.start();
        }

        btnPause.setImageResource(R.drawable.pause);
        btnPlay.setImageResource(R.drawable.play_pink);
    }

    public void pause(View view) {
        posicion=videoView.getCurrentPosition();
        videoView.pause();

        btnPlay.setImageResource(R.drawable.play);
        btnPause.setImageResource(R.drawable.pause_pink);
    }

    public void stop(View view){
        btnPause.setImageResource(R.drawable.pause);
        btnPlay.setImageResource(R.drawable.play);
        videoView.suspend(); btnStop.setImageResource(R.drawable.stop_pink);
    }

    public void uno(View view){
        imgInicio.setImageResource(R.drawable.inicio2);
        btnUno.setImageResource(R.drawable.circulo_pink);
        btnDos.setImageResource(R.drawable.circulo);
        btnTres.setImageResource(R.drawable.circulo);
    }

    public void dos(View view){
        imgInicio.setImageResource(R.drawable.inicio1);
        btnUno.setImageResource(R.drawable.circulo);
        btnDos.setImageResource(R.drawable.circulo_pink);
        btnTres.setImageResource(R.drawable.circulo);
    }

    public void tres(View view){
        imgInicio.setImageResource(R.drawable.inicio3);
        btnUno.setImageResource(R.drawable.circulo);
        btnDos.setImageResource(R.drawable.circulo);
        btnTres.setImageResource(R.drawable.circulo_pink);
    }
}