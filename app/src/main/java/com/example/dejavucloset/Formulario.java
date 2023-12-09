package com.example.dejavucloset;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Formulario extends AppCompatActivity {
    private WebView wvInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        wvInternet=(WebView) findViewById(R.id.wvInternet);

        Bundle bundle=getIntent().getExtras();

        String url=bundle.getString("direccion");
        wvInternet.loadUrl(url);
    }
}