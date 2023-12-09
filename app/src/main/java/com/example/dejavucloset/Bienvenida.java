package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Bienvenida extends AppCompatActivity {
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);


    }

    //Inicio
    public void irInicio(View view){
        mp= MediaPlayer.create(this, R.raw.boton);
        mp.start();
        Intent i=new Intent(this,Inicio.class);
        startActivity(i);
    }

}