package com.example.dejavucloset.iniciosesion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejavucloset.Perfil;
import com.example.dejavucloset.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {
    //Variables
    private MediaPlayer mp;
    private ImageButton btnVer;
    private static int status=0;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USER= "user";
    private static final String TAG="Registro";
    private Usuario user;

    //Variables
    private EditText txtNombreCompleto, txtDireccion, txtCelular, txtMail, textPassword;
    private Spinner spDistrito;
    private Button btnRegistrarse;
    private ProgressBar pbRegistro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //Firebase
        database= FirebaseDatabase.getInstance();
        mDatabase= database.getReference(USER);
        mAuth=FirebaseAuth.getInstance();

        //LAYOUT
        txtNombreCompleto=(EditText) findViewById(R.id.txtNombreCompleto);
        txtDireccion= (EditText) findViewById(R.id.txtDireccion);
        txtCelular= (EditText) findViewById(R.id.txtCelular);
        txtMail= (EditText) findViewById(R.id.txtMail);
        textPassword= (EditText) findViewById(R.id.textPassword);
        btnRegistrarse= (Button) findViewById(R.id.btnRegistrarse);
        pbRegistro= (ProgressBar) findViewById(R.id.pbRegistro);

        //Para que no se muestre el progress bar
        pbRegistro.setVisibility(View.GONE);

        //Spinner Registro
        //Declarar variables
        spDistrito=(Spinner) findViewById(R.id.spDistrito);
        btnVer= (ImageButton) findViewById(R.id.btnVer);

        //Declarar matriz
        String distritos[]={"Distrito","Ancón", "Ate", "Barranco", "Breña", "Carabayllo",
                "Chaclacayo", "Chorrillos", "Cieneguilla", "Comas", "El Agustino", "Independencia",
                "Jesús María", "La Molina", "La Victoria", "Lince", "Los Olivos", "Lurigancho", "Lurín",
                "Magdalena del Mar", "Miraflores", "Pachacamac", "Pucusuna", "Pueblo Libre","Puente Piedra",
                "Punta Hermosa", "Punta Negra", "Rímac", "San Bartolo", "San Borja", "San Isidro",
                "San Juan de Lurigancho", "San Juan de Miraflores", "San Luis", "San Martin de Porres",
                "San Miguel", "Santa Anita", "Santa María del Mar", "Santa Rosa", "Santiago de Surco", "Surquillo",
                "Villa El Salvador", "Villa María del Triunfo"};

        //En base de datos se pone simple list item
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,distritos);

        //Asiganamos adapatador a spinner
        spDistrito.setAdapter(adapter);

        //Para seleccionar, onItemSelected
        //OJO---> En el primero solo se copia setOnItemS.............. En el segundo se escribe "new Adaptaer." para que aparezca todo
        spDistrito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String distrito = spDistrito.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    //Para que muestre clave
    public void mostrarClave(View v) {
        int cursor=textPassword.getSelectionEnd();

        if(status==0){
            textPassword.setInputType(InputType. TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            status=1;
        } else{
            textPassword.setInputType(InputType. TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            status=0;
        }
        textPassword.setSelection(cursor);
    }

    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this,IniciarSesion.class);
        startActivity(i);
    }


    //REGISTRO DE USUARIO
    public void registrarUsuario(View v){
        String nombre=txtNombreCompleto.getText().toString().trim();
        String distrito=spDistrito.getSelectedItem().toString();
        String direccion=txtDireccion.getText().toString().trim();
        String celular=txtCelular.getText().toString().trim();
        String email=txtMail.getText().toString().trim();
        String password=textPassword.getText().toString();

        //Validamos
        if(nombre.isEmpty()){
            txtNombreCompleto.setError("Campo nombre obligatorio");
            txtNombreCompleto.requestFocus();
            return;
        }

        if(distrito=="Distrito"){
            ((TextView)spDistrito.getSelectedView()).setError("Escoge un distrito");
            return;
        }

        if(direccion.isEmpty()){
            txtDireccion.setError("Campo dirección obligatorio");
            txtDireccion.requestFocus();
            return;
        }

        if(celular.isEmpty()){
            txtCelular.setError("Campo celular obligatorio");
            txtCelular.requestFocus();
            return;
        }

        if(email.isEmpty()){
            txtMail.setError("Campo email obligatorio");
            txtMail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            textPassword.setError("Campo contraseña obligatorio");
            textPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            textPassword.setError("Se necesita mínimo 6 caracteres");
            textPassword.requestFocus();
            return;
        }


        pbRegistro.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user=new Usuario(nombre, distrito, direccion, celular, email, password);
                            //oBTENEMOS EL UI DE AUthroization
                            FirebaseDatabase.getInstance().getReference("user").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mp= MediaPlayer.create(Registro.this, R.raw.exitoso);
                                        mp.start();
                                        Toast.makeText(Registro.this, "Usuario registrado", Toast.LENGTH_LONG).show();
                                        Intent i=new Intent(Registro.this, IniciarSesion.class);
                                        startActivity(i);
                                    } else{

                                        mp= MediaPlayer.create(Registro.this, R.raw.exitoso);
                                        mp.start();
                                        Toast.makeText(Registro.this, "Problemas en el registro", Toast.LENGTH_LONG).show();
                                        pbRegistro.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            pbRegistro.setVisibility(View.GONE);
                            Toast.makeText(Registro.this, "Falló registro",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}