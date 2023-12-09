package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import com.example.dejavucloset.iniciosesion.IniciarSesion;
import com.example.dejavucloset.productos.ProductoClass;
import com.example.dejavucloset.productos.productoAdapter;
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

public class Categorias extends AppCompatActivity {
    private RecyclerView rvBusqueda;
    private ScrollView sv1;
    private CardView cat1,cat2,cat3,cat4,cat5,cat6;
    private EditText txtBusqueda;
    private FirebaseDatabase db;
    private DatabaseReference node;
    private productoAdapter allDataAdapter;
    private List<ProductoClass> listaProductos= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        txtBusqueda= (EditText) findViewById(R.id.txtBusqueda);
        rvBusqueda= (RecyclerView) findViewById(R.id.rvBusqueda);
        cat1=(CardView) findViewById(R.id.cat1);
        cat2=(CardView) findViewById(R.id.cat2);
        cat3=(CardView) findViewById(R.id.cat3);
        cat4=(CardView) findViewById(R.id.cat4);
        cat5=(CardView) findViewById(R.id.cat5);
        cat6=(CardView) findViewById(R.id.cat6);

        //Bottom Navegation View
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //Con inicio seleeccionado
        navigation.setSelectedItemId(R.id.productos);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        Intent a = new Intent(Categorias.this,Inicio.class);
                        startActivity(a);
                        break;
                    case R.id.productos:
                        break;
                    case R.id.perfil:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            Intent intent = new Intent(Categorias.this, Perfil.class);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(Categorias.this, IniciarSesion.class);
                            startActivity(intent);

                        };
                        break;
                }
                return false;
            }
        });
        rvBusqueda.setVisibility(View.INVISIBLE);

        //llamamos a firebase
        db=FirebaseDatabase.getInstance();

        //Tabla pedidoUsuario
        node=db.getReference("productos");


        txtBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cat1.setVisibility(View.INVISIBLE);
                cat2.setVisibility(View.INVISIBLE);
                cat3.setVisibility(View.INVISIBLE);
                cat4.setVisibility(View.INVISIBLE);
                cat5.setVisibility(View.INVISIBLE);
                cat6.setVisibility(View.INVISIBLE);


                rvBusqueda.setVisibility(View.VISIBLE);
                irBuscar();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void irBuscar(){
        listaProductos.clear();
        String busquedas=txtBusqueda.getText().toString().toLowerCase();
        rvBusqueda.setLayoutManager(new GridLayoutManager(this,2));

        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String codigo=dataSnapshot1.child("codigo").getValue().toString();
                    String descripcion=dataSnapshot1.child("descripcion").getValue().toString();
                    String estado=dataSnapshot1.child("estado").getValue().toString();
                    String fecha=dataSnapshot1.child("fecha").getValue().toString();
                    double precio=(Double.parseDouble(dataSnapshot1.child("precio").getValue().toString()));
                    String titulo=dataSnapshot1.child("titulo").getValue().toString();
                    String url=dataSnapshot1.child("url").getValue().toString();

                    String titul=titulo.toLowerCase();
                    //Vemos si coincide
                    if(titul.contains(busquedas)){
                        ProductoClass pe=new ProductoClass(codigo,titulo,descripcion,precio,fecha,url,estado);
                        listaProductos.add(pe);
                    }
                }

                allDataAdapter = new productoAdapter(Categorias.this, listaProductos);
                rvBusqueda.setAdapter(allDataAdapter);
                allDataAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void cardigan(View view){
        String categoria="cardigan";
        Intent i=new Intent(this, Productos.class);
        i.putExtra("categoria",categoria);
        startActivity(i);
    }

    public void vestidos(View view){
        String categoria="vestidos";
        Intent i=new Intent(this, Productos.class);
        i.putExtra("categoria",categoria);
        startActivity(i);

    }

    public void hoodies(View view){
        String categoria="hoodies";
        Intent i=new Intent(this, Productos.class);
        i.putExtra("categoria",categoria);
        startActivity(i);
    }


    public void tops(View view){
        String categoria="tops";
        Intent i=new Intent(this, Productos.class);
        i.putExtra("categoria",categoria);
        startActivity(i);
    }


    public void shorts(View view){
        String categoria="shorts";
        Intent i=new Intent(this, Productos.class);
        i.putExtra("categoria",categoria);
        startActivity(i);
    }

    public void faldas(View view){
        String categoria="faldas";
        Intent i=new Intent(this, Productos.class);
        i.putExtra("categoria",categoria);
        startActivity(i);
    }

    public void irBusqueda(View v){
        irBuscar();
    }
}