package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.dejavucloset.carrito.carrito_modelo;
import com.example.dejavucloset.productos.ProductoClass;
import com.example.dejavucloset.productos.productoAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ListaDeseos extends AppCompatActivity {
    private RecyclerView rvDeseos;
    ArrayList<ProductoClass> listaDeseos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_deseos);


        rvDeseos= (RecyclerView)findViewById(R.id.rvDeseos);

        //Bottom Navegation View
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //Con inicio seleeccionado
        navigation.setSelectedItemId(R.id.perfil);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        Intent a = new Intent(ListaDeseos.this,Inicio.class);
                        startActivity(a);
                        break;
                    case R.id.productos:
                        Intent b = new Intent(ListaDeseos.this,Categorias.class);
                        startActivity(b);
                        break;
                    case R.id.perfil:
                        Intent c = new Intent(ListaDeseos.this,Perfil.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });

        listaDeseos= new ArrayList<>();
        rvDeseos.setLayoutManager(new GridLayoutManager(this, 2));

        consultarLista();

        productoAdapter adapter=new productoAdapter(this, listaDeseos);
        rvDeseos.setAdapter(adapter);
    }

    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this,Perfil.class);
        startActivity(i);
    }

    public void consultarLista(){
        deseoSQLiteOpenHelper admin = new
                deseoSQLiteOpenHelper(this, "baseDeseos", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        ProductoClass cm;
        Cursor cursor=bd.rawQuery("SELECT * FROM deseos", null);

        while(cursor.moveToNext()){
            cm=new ProductoClass();
            cm.setCodigo(cursor.getString(0));
            cm.setTitulo(cursor.getString(1));
            cm.setPrecio(cursor.getDouble(2));
            cm.setUrl(cursor.getString(3));

            listaDeseos.add(cm);
        }
    }
}