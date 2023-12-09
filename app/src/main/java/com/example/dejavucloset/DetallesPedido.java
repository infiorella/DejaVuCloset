package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejavucloset.iniciosesion.Usuario;
import com.example.dejavucloset.carrito.*;
import com.example.dejavucloset.pedidos.*;
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

public class DetallesPedido extends AppCompatActivity {
    private TextView numPedido, tvNombreP, tvDireccionP, txtEstadoPedido, txtRepartidor, tvMonto, tvFechaP;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private DatabaseReference db, db2;
    private RecyclerView rvProductoP;

    private proPedidoAdapter adapter;
    private String llave;

    private ArrayList<carrito_modelo> listaProductos= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);
        Bundle bundle=getIntent().getExtras();

        numPedido= (TextView) findViewById(R.id.numPedido);
        tvNombreP=(TextView) findViewById(R.id.tvNombreP);
        tvDireccionP= (TextView) findViewById(R.id.tvDireccionP);
        txtEstadoPedido= (TextView) findViewById(R.id.txtEstadoPedido);
        txtRepartidor= (TextView) findViewById(R.id.txtRepartidor);
        tvMonto= (TextView) findViewById(R.id.tvMonto);
        tvFechaP= (TextView) findViewById(R.id.tvFechaP);
        rvProductoP= (RecyclerView) findViewById(R.id.rvProductosP);

        if(bundle!=null){
            llave=bundle.getString("id");
            numPedido.setText(llave);
        }

        //Bottom Navegation View
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //Con inicio seleeccionado
        navigation.setSelectedItemId(R.id.perfil);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        Intent a = new Intent(DetallesPedido.this,Inicio.class);
                        startActivity(a);
                        break;
                    case R.id.productos:
                        Intent b = new Intent(DetallesPedido.this,Categorias.class);
                        startActivity(b);
                        break;
                    case R.id.perfil:
                        Intent c = new Intent(DetallesPedido.this,Perfil.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });


        //Inicializamos USUARIO
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("user");
        userID=user.getUid();

        //Snapshot
        //DATABASE pedido-usuario
        db = FirebaseDatabase.getInstance().getReference().child("pedidoUsuario").child(userID).child(llave);

        db2=FirebaseDatabase.getInstance().getReference().child("pedido").child(llave);

        obtenerDireccion();
        obtenerDatoPedido();
        obtenerProductos();
    }

    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this,Pedidos.class);
        startActivity(i);
    }

    public void obtenerDatoPedido(){
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fecha=snapshot.child("fecha").getValue().toString();
                String estado=snapshot.child("estado").getValue().toString();
                String nombre=snapshot.child("nomUsuario").getValue().toString();
                String repartidor=snapshot.child("repartidor").getValue().toString();
                String codigo=snapshot.child("codigo").getValue().toString();
                double monto=(Double.parseDouble( snapshot.child("monto").getValue().toString()));

                tvNombreP.setText(nombre);
                txtEstadoPedido.setText(estado);
                txtRepartidor.setText(repartidor);
                tvMonto.setText(""+monto);
                tvFechaP.setText(fecha);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(DetallesPedido.this, "Error al conectar con base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void obtenerDireccion(){
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("user");
        userID=user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario perfil=snapshot.getValue(Usuario.class);

                if(perfil!=null){
                    String direccion=perfil.direccion;

                    tvDireccionP.setText(direccion);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void obtenerProductos(){
        //llamamos a firebase
        FirebaseDatabase db=FirebaseDatabase.getInstance();

        String llave=numPedido.getText().toString();

        //Tabla pedidoUsuario
        DatabaseReference node=db.getReference("pedido");
        DatabaseReference node1=node.child(llave);


        rvProductoP.setLayoutManager(new LinearLayoutManager(this));

        node1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaProductos.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    int cantidad=Integer.parseInt(dataSnapshot1.child("cantidad").getValue().toString());
                    String codigo=dataSnapshot1.child("codigo").getValue().toString();
                    double precio=Double.parseDouble(dataSnapshot1.child("precio").getValue().toString());
                    String talla=dataSnapshot1.child("talla").getValue().toString();
                    String titulo=dataSnapshot1.child("titulo").getValue().toString();
                    int url=Integer.parseInt(dataSnapshot1.child("url").getValue().toString());

                    carrito_modelo pe=new carrito_modelo(codigo,titulo, talla,cantidad,url,precio);
                    listaProductos.add(pe);
                }

                adapter = new proPedidoAdapter( listaProductos,DetallesPedido.this);
                rvProductoP.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}