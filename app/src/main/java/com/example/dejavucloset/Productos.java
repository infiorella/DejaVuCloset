package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.dejavucloset.carrito.Carrito;
import com.example.dejavucloset.iniciosesion.IniciarSesion;

import com.example.dejavucloset.productos.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Productos extends AppCompatActivity {
    private ImageView imgProductos;
    private ProgressBar pgProductos;
    private RecyclerView rvProductos;
    private productoAdapter allDataAdapter;
    private String cat;
    private List<ProductoClass> listaProductos= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        Bundle bundle=getIntent().getExtras();
        cat=bundle.getString("categoria");
        pgProductos=(ProgressBar)findViewById(R.id.pgProductos);
        rvProductos= (RecyclerView)findViewById(R.id.rvProductos);

        //Bottom Navegation View
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        pgProductos.setVisibility(View.VISIBLE);

        //Con inicio seleeccionado
        navigation.setSelectedItemId(R.id.productos);

        imgProductos= (ImageView) findViewById(R.id.imgProductos);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        Intent a = new Intent(Productos.this,Inicio.class);
                        startActivity(a);
                        break;
                    case R.id.productos:
                        Intent b = new Intent(Productos.this,Categorias.class);
                        startActivity(b);
                        break;
                    case R.id.perfil:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            Intent intent = new Intent(Productos.this, Perfil.class);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(Productos.this, IniciarSesion.class);
                            startActivity(intent);

                        };
                        break;
                }
                return false;
            }
        });

        cambiarImagen();
        initView();

    }

    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this,Categorias.class);
        startActivity(i);
    }

    private void cambiarImagen(){
        switch(cat){
            case "cardigan":
                imgProductos.setImageResource(R.drawable.portada_cardigan);
                break;
            case "faldas":
                imgProductos.setImageResource(R.drawable.portada_falda);
                break;
            case "hoodies":
                imgProductos.setImageResource(R.drawable.portada_hoodie);
                break;
            case "shorts":
                imgProductos.setImageResource(R.drawable.portada_short);
                break;
            case "tops":
                imgProductos.setImageResource(R.drawable.portada_tops);
                break;
            case "vestidos":
                imgProductos.setImageResource(R.drawable.portada_vestidos);
                break;
        }
    }

    private void initView() {
        //llamamos a firebase
        FirebaseDatabase db=FirebaseDatabase.getInstance();

        //Tabla pedidoUsuario
        DatabaseReference node=db.getReference("productos");

        rvProductos.setLayoutManager(new GridLayoutManager(this,2));
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaProductos.clear();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String codigo = dataSnapshot1.child("codigo").getValue().toString();
                        String descripcion = dataSnapshot1.child("descripcion").getValue().toString();
                        String estado = dataSnapshot1.child("estado").getValue().toString();
                        String fecha = dataSnapshot1.child("fecha").getValue().toString();
                        double precio = (Double.parseDouble(dataSnapshot1.child("precio").getValue().toString()));
                        String titulo = dataSnapshot1.child("titulo").getValue().toString();
                        String url = dataSnapshot1.child("url").getValue().toString();
                        String cate = dataSnapshot1.child("cat").getValue().toString();

                        if (cate.equals(cat)) {
                            ProductoClass pe = new ProductoClass(codigo, titulo, descripcion, precio, fecha, url, estado);
                            listaProductos.add(pe);
                        }

                        pgProductos.setVisibility(View.INVISIBLE);
                        allDataAdapter = new productoAdapter(Productos.this, listaProductos);
                        rvProductos.setAdapter(allDataAdapter);
                        allDataAdapter.notifyDataSetChanged();
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //Ir Carrito
    public void irCarrito(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(Productos.this, Carrito.class);
            startActivity(intent);
        } else{
            Intent intent = new Intent(Productos.this, IniciarSesion.class);
            startActivity(intent);

        };
    }
}