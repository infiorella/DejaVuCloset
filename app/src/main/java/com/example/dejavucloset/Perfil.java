package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejavucloset.iniciosesion.IniciarSesion;
import com.example.dejavucloset.iniciosesion.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.dejavucloset.carrito.Carrito;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Perfil extends AppCompatActivity {
    private TextView nombreP;
    private Button btCerrar;

    //BASE DE DATOS- FIREBASE
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //PARA MOSTRAR
        //Inicializamos
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("user");
        userID=user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario perfil=snapshot.getValue(Usuario.class);

                if(perfil!=null){
                    String nombre=perfil.nombre;

                    nombreP.setText(nombre);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Perfil.this, "Existen problemas", Toast.LENGTH_LONG).show();
            }
        });

        //Bottom Navegation View
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //Con inicio seleeccionado
        navigation.setSelectedItemId(R.id.perfil);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        Intent a = new Intent(Perfil.this,Inicio.class);
                        startActivity(a);
                        break;
                    case R.id.productos:
                        Intent b = new Intent(Perfil.this,Categorias.class);
                        startActivity(b);
                        break;
                    case R.id.perfil:
                        break;
                }
                return false;
            }
        });

        //Incializamos
        btCerrar= (Button) findViewById(R.id.btCerrar);
        nombreP= (TextView) findViewById(R.id.txtNombrePerfil);

        //Si vamos a cerrar sesión
        btCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Llamamos a firebase
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(Perfil.this, IniciarSesion.class);
                startActivity(i);
            }
        });


    }


    //******INTENTS
    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this,Inicio.class);
        startActivity(i);
    }

    //Editar perfil
    public void irEditar(View view){
        Intent i=new Intent(this,EditarPerfil.class);
        startActivity(i);
    }

    //Pedidos
    public void irPedidos(View view){
        Intent i=new Intent(this,Pedidos.class);
        startActivity(i);
    }

    //Ir Nosotros
    public void irNosotros(View view){
        Intent i=new Intent(this,Nosotros.class);
        startActivity(i);
    }

    //Ir deseos
    public void irDeseos(View view){
        Intent i=new Intent(this,ListaDeseos.class);
        startActivity(i);
    }

    //Ir Carrito
    public void irCarrito(View view){
        Intent i=new Intent(this, Carrito.class);
        startActivity(i);
    }
}