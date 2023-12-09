package com.example.dejavucloset.iniciosesion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dejavucloset.Categorias;
import com.example.dejavucloset.Inicio;
import com.example.dejavucloset.Perfil;
import com.example.dejavucloset.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class IniciarSesion extends AppCompatActivity {
    //ACCEDER
    private MediaPlayer mp;
    private Button btnIniciarSesion;
    private EditText etMail, etClave;
    private ImageButton btnVerIniciar;
    private ProgressBar pbIniciar;
    private static int status=0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        //Bottom Navegation View
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //Con inicio seleeccionado
        navigation.setSelectedItemId(R.id.perfil);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio:
                        Intent a = new Intent(IniciarSesion.this, Inicio.class);
                        startActivity(a);
                        break;
                    case R.id.productos:
                        Intent b = new Intent(IniciarSesion.this, Categorias.class);
                        startActivity(b);
                        break;
                    case R.id.perfil:
                        break;
                }
                return false;
            }
        });

        btnIniciarSesion= (Button) findViewById(R.id.btnIniciarSesion);
        etMail= (EditText) findViewById(R.id.etMail);
        etClave= (EditText) findViewById(R.id.etClave);
        btnVerIniciar= (ImageButton) findViewById(R.id.btnVerIniciar);
        pbIniciar= (ProgressBar) findViewById(R.id.pbIniciar);

        //Para conectar con firebase
        mAuth= FirebaseAuth.getInstance();

        //Progress Bar
        pbIniciar.setVisibility(View.GONE);
    }

    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this,Inicio.class);
        startActivity(i);
    }

    //Ir Registro
    public void irRegistro(View view){
        Intent i=new Intent(this, Registro.class);
        startActivity(i);
    }

    //Ir Perfil
    public void irPerfil(View view){
        String email=etMail.getText().toString();
        String password=etClave.getText().toString();

        if(email.isEmpty()){
            etMail.setError("Campo email obligatorio");
            etMail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            etClave.setError("Campo contraseña obligatorio");
            etClave.requestFocus();
            return;
        }

        if(password.length()<6){
            etClave.setError("Se necesita mínimo 6 caracteres");
            etClave.requestFocus();
            return;
        }

        pbIniciar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mp= MediaPlayer.create(IniciarSesion.this, R.raw.exitoso);
                    mp.start();
                    Intent i=new Intent(IniciarSesion.this, Perfil.class);
                    startActivity(i);
                } else{
                    mp= MediaPlayer.create(IniciarSesion.this, R.raw.boton);
                    mp.start();
                    Toast.makeText(IniciarSesion.this, "Problemas con la autenticación", Toast.LENGTH_LONG).show();

                }
                pbIniciar.setVisibility(View.GONE);
            }
        });
    }

    //Para que muestre clave
    public void mostrarClave(View v) {
        int cursor=etClave.getSelectionEnd();

        if(status==0){
            etClave.setInputType(InputType. TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            status=1;
            btnVerIniciar.setColorFilter(Color.rgb(246,166,164));
        } else{
            etClave.setInputType(InputType. TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            status=0;
            btnVerIniciar.setColorFilter(Color.rgb(154,149,149));
        }
        etClave.setSelection(cursor);
    }
}