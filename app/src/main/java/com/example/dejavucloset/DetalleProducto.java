package com.example.dejavucloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dejavucloset.iniciosesion.*;
import com.example.dejavucloset.carrito.Carrito;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetalleProducto extends AppCompatActivity {
    //Variables
    private MediaPlayer mp;
    private ImageButton btnQuitar, btnAumentar;
    private RadioGroup talla;
    private Button btnAñadir;
    private TextView txtTitulo,txtCantidad, txtPrecio;
    private int num=1, url;
    private String codigo;
    String radioText;
    private ImageView imgProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        Bundle bundle=getIntent().getExtras();
        String titulo=bundle.getString("titulo");
        String precio=bundle.getString("precio");
        codigo=bundle.getString("codigo");
        url=bundle.getInt("url");

        //Declarar variables
        btnQuitar = (ImageButton) findViewById(R.id.btnQuitar);
        btnAumentar = (ImageButton) findViewById(R.id.btnAumentar);
        txtCantidad = (TextView) findViewById(R.id.txtCantidad);
        txtTitulo=(TextView) findViewById(R.id.txtTitulo);
        txtPrecio=(TextView) findViewById(R.id.txtPrecio);
        imgProducto= (ImageView) findViewById(R.id.imgProducto);
        talla= (RadioGroup) findViewById(R.id.talla);
        txtTitulo.setText(titulo);
        txtPrecio.setText(precio);
        imgProducto.setImageResource(url);
        btnAñadir=(Button) findViewById(R.id.btnAñadir);
        talla.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id=group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                radioText=rb.getText().toString();

                verStock();
            }
        });

        btnAumentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidad= (Integer.parseInt(txtCantidad.getText().toString()));
                    cantidad++;

                txtCantidad.setText(""+cantidad);
            }
        });

        btnQuitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cantidad= (Integer.parseInt(txtCantidad.getText().toString()));
                if(cantidad<=1){
                    cantidad=1;
                } else{
                    cantidad--;
                }
                txtCantidad.setText(""+cantidad);

            }
        });


    }


    //*****INTENTS
    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this,Categorias.class);
        startActivity(i);
    }


    //Verificar si ha iniciado sesión OJOOO
    public void irCarrito(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(DetalleProducto.this, Carrito.class);
            startActivity(intent);
        } else{
            Intent intent = new Intent(DetalleProducto.this, IniciarSesion.class);
            startActivity(intent);

        };
    }


    //Añadir com.example.dejavucloset.carrito
    public void añadir(View view) {
        AdminSQLiteOpenHelper admin = new
                AdminSQLiteOpenHelper(this, "administracion", null, 1);

        //Cree y / o abra una base de datos que se utilizará para leer y escribir.
        SQLiteDatabase bd = admin.getWritableDatabase();

        //Obtener los datos
        String titulo = txtTitulo.getText().toString();
        String pre = txtPrecio.getText().toString();
        String cant = txtCantidad.getText().toString();
        String size= radioText;

        //Registro
        ContentValues registro = new ContentValues();
        registro.put("codigo", codigo);
        registro.put("titulo", titulo);
        registro.put("precio", pre);
        registro.put("cantidad", cant);
        registro.put("url", url);
        registro.put("talla", size);

        //Verificamos si marcó el radio button
        if(talla.getCheckedRadioButtonId()==-1){
            Toast.makeText(this, "Selecciona una talla", Toast.LENGTH_SHORT).show();
            return;
        } else {

            ///Verificamos si existe
            Cursor fila = bd.rawQuery("select codigo,titulo,precio,cantidad, url, talla from carrito where titulo='" + titulo + "' and talla='"+size+"'", null);

            //Cursor fila = bd.rawQuery("select codigo,titulo,precio,cantidad, url, talla from carrito where titulo='" + titulo + "'", null);

            mp = MediaPlayer.create(this, R.raw.exitoso);
            mp.start();

            // permite probar si la consulta devolvió un conjunto vacío  y mueve el cursor al primer resultado
            if (fila.moveToFirst()) {
                int cantidad1 = Integer.parseInt(fila.getString(3));
                int cantidad2 = Integer.parseInt(txtCantidad.getText().toString());

                int suma = cantidad1 + cantidad2;
                ContentValues nuevo = new ContentValues();
                nuevo.put("cantidad", suma);
                //QUERY
                bd.update("carrito", nuevo, "codigo='" + codigo + "'", null);
                bd.close();

            } else {
                //Insertar
                bd.insert("carrito", null, registro);

                //Cerrar
                bd.close();

            }

            //Mostrar mensaje de ingreso de datos
            Toast.makeText(this, "Producto agregado a Carrito", Toast.LENGTH_SHORT).show();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(this, Carrito.class);
                startActivity(intent);
            } else{
                Intent intent = new Intent(this, IniciarSesion.class);
                startActivity(intent);

            };

        }
    }

    public void verStock(){
        //llamamos a firebase
        FirebaseDatabase db=FirebaseDatabase.getInstance();

        DatabaseReference node=db.getReference("productos");

        String talla=radioText;
        int cant = (Integer.parseInt(txtCantidad.getText().toString()));

        DatabaseReference node2=node.child(codigo).child("talla").child(talla).child("stock");
        node2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                btnAñadir.setEnabled(true);
                int stock=Integer.parseInt(snapshot.getValue().toString());
                if(cant>stock){
                    Toast.makeText(DetalleProducto.this, "No existe stock actualmente", Toast.LENGTH_SHORT).show();
                    btnAñadir.setEnabled(false);
                }
                if(stock<3){
                    Toast.makeText(DetalleProducto.this, "Pocas unidades disponibles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}