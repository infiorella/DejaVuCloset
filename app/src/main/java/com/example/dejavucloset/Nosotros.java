package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

public class Nosotros extends AppCompatActivity {

    private ImageButton ibLlamar;
    private final int TEL_COD=100;
    private final String etPhone="937027676";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nosotros);


        ibLlamar= (ImageButton) findViewById(R.id.ibLlamar);


        //Bottom Navegation View
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //Con inicio seleeccionado
        navigation.setSelectedItemId(R.id.perfil);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        Intent a = new Intent(Nosotros.this, Inicio.class);
                        startActivity(a);
                        break;
                    case R.id.productos:
                        Intent b = new Intent(Nosotros.this, Categorias.class);
                        startActivity(b);
                        break;
                    case R.id.perfil:
                        Intent c = new Intent(Nosotros.this, Perfil.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });

        ibLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nroFono = etPhone;
                if (nroFono != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                TEL_COD);
                    } else {
                        OlderVersions(nroFono);
                    }
                }
            }

            private void OlderVersions(String fono){
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+fono));
                int result = checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE);

                if ( result == PackageManager.PERMISSION_GRANTED){
                    startActivity(intentCall);}
                else{
                    Toast.makeText(Nosotros.this, "Acceso no autorizado",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this,Perfil.class);
        startActivity(i);
    }

    public void irFormulario(View view){
        String url="https://forms.gle/awUbT9RnwcYShnif9";

        Intent i=new Intent(this, Formulario.class);
        i.putExtra("direccion",url);
        startActivity(i);
    }

    //*****Llamada
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case TEL_COD:
                String permisos = permissions[0];
                int result = grantResults[0];
                if (permisos.equals(Manifest.permission.CALL_PHONE)){
                    if (result == PackageManager.PERMISSION_GRANTED){
                        String phoneNumber = etPhone; Intent intentCall
                                = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
                        if (ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) return;
                        startActivity(intentCall);
                    }
                    else{
                        Toast.makeText(Nosotros.this, "Acceso no autorizado",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private boolean CheckPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    //Mapa
    public void irMapa(View view){
        Intent i=new Intent(this,Mapa.class);
        startActivity(i);
    }

}