package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import com.example.dejavucloset.iniciosesion.IniciarSesion;
import com.example.dejavucloset.pedidos.PedidoClass;
import com.example.dejavucloset.pedidos.pedidoAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class Pedidos extends AppCompatActivity {
    //Para mostrar datos
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private ConstraintLayout clPedidoVacio;
    private RecyclerView rvPedido;
    private pedidoAdapter allDataAdapter;

    private List<PedidoClass> listaPedido= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        rvPedido=(RecyclerView)findViewById(R.id.rvPedidos);
        clPedidoVacio=(ConstraintLayout) findViewById(R.id.clPedidoVacio);

        clPedidoVacio.setVisibility(View.INVISIBLE);
        //Bottom Navegation View
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //Con perfil seleccionado
        navigation.setSelectedItemId(R.id.perfil);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        Intent a = new Intent(Pedidos.this,Inicio.class);
                        startActivity(a);
                        break;
                    case R.id.productos:
                        Intent b = new Intent(Pedidos.this,Categorias.class);
                        startActivity(b);
                        break;
                    case R.id.perfil:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            Intent intent = new Intent(Pedidos.this, Perfil.class);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(Pedidos.this, IniciarSesion.class);
                            startActivity(intent);

                        };
                        break;
                }
                return false;
            }
        });

        initView();
    }

    private void initView() {
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("user");
        userID=user.getUid();

        //llamamos a firebase
        FirebaseDatabase db=FirebaseDatabase.getInstance();

        //Tabla pedidoUsuario
        DatabaseReference node=db.getReference("pedidoUsuario");
        DatabaseReference node1=node.child(userID);


       rvPedido.setLayoutManager(new LinearLayoutManager(this));
       node1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaPedido.clear();
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String fecha = dataSnapshot1.child("fecha").getValue().toString();
                        String estado = dataSnapshot1.child("estado").getValue().toString();
                        String nomUsuario = dataSnapshot1.child("nomUsuario").getValue().toString();
                        String repartidor = dataSnapshot1.child("repartidor").getValue().toString();
                        String codigo = dataSnapshot1.child("codigo").getValue().toString();
                        double monto = (Double.parseDouble(dataSnapshot1.child("monto").getValue().toString()));

                        PedidoClass pe = new PedidoClass(fecha, estado, nomUsuario, repartidor, monto, codigo);
                        listaPedido.add(pe);
                    }
                    clPedidoVacio.setVisibility(View.INVISIBLE);
                    allDataAdapter = new pedidoAdapter(Pedidos.this, listaPedido);
                    rvPedido.setAdapter(allDataAdapter);
                    allDataAdapter.notifyDataSetChanged();

                } else{
                    clPedidoVacio.setVisibility(View.VISIBLE);
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this,Perfil.class);
        startActivity(i);
    }

}