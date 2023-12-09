package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dejavucloset.iniciosesion.Usuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditarPerfil extends AppCompatActivity {
    private MediaPlayer mp;

    private EditText etNombre, etDireccion, etCelular, etEmail, etContraseña;

    //Para mostrar datos
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        //Bottom Navegation View
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //Con inicio seleeccionado
        navigation.setSelectedItemId(R.id.perfil);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        Intent a = new Intent(EditarPerfil.this,Inicio.class);
                        startActivity(a);
                        break;
                    case R.id.productos:
                        Intent b = new Intent(EditarPerfil.this,Categorias.class);
                        startActivity(b);
                        break;
                    case R.id.perfil:
                        Intent c = new Intent(EditarPerfil.this,Perfil.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });

        etNombre= (EditText) findViewById(R.id.etNombreP);
        etDireccion= (EditText) findViewById(R.id.etDireccionP);
        etCelular= (EditText) findViewById(R.id.etCelularP);
        etEmail= (EditText) findViewById(R.id.etMailP);
        etContraseña= (EditText) findViewById(R.id.etClaveP);

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
                    String direccion=perfil.direccion;
                    String celular=perfil.celular;
                    String email=perfil.email;
                    String contraseña=perfil.password;

                    etNombre.setText(nombre);
                    etCelular.setText(celular);
                    etDireccion.setText(direccion);
                    etEmail.setText(email);
                    etContraseña.setText(contraseña);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditarPerfil.this, "Existen problemas", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this,Perfil.class);
        startActivity(i);
    }

    public void editar(View view){
        reference.child(userID).child("celular").setValue(etCelular.getText().toString());
        reference.child(userID).child("direccion").setValue(etDireccion.getText().toString());
        reference.child(userID).child("email").setValue(etEmail.getText().toString());
        reference.child(userID).child("nombre").setValue(etNombre.getText().toString());
        reference.child(userID).child("password").setValue(etContraseña.getText().toString());

        mp= MediaPlayer.create(this, R.raw.boton);
        mp.start();
    }
}