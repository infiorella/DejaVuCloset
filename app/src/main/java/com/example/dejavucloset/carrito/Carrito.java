package com.example.dejavucloset.carrito;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejavucloset.AdminSQLiteOpenHelper;
import com.example.dejavucloset.Categorias;
import com.example.dejavucloset.DetallesPedido;
import com.example.dejavucloset.Pedidos;
import com.example.dejavucloset.R;
import com.example.dejavucloset.iniciosesion.IniciarSesion;
import com.example.dejavucloset.iniciosesion.Usuario;
import com.example.dejavucloset.pedidos.PedidoClass;
import com.example.dejavucloset.productos.ProductoClass;
import com.example.dejavucloset.productos.productoAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Carrito extends AppCompatActivity {
    private MediaPlayer mp;
    private double monto;
    private TextView txtSubtotal, infoCarrito, idPedido;
    private LinearLayout carritoVacio;
    RecyclerView recyclerView;
    ArrayList<carrito_modelo> listaCarrito;
    private ProgressBar pgCarrito;
    private String key1, userID, strDate, nombre,distrito, repartidor, montototal;

    //Para mostrar datos
    private FirebaseUser user;
    private DatabaseReference reference;

    //Cuando se muestre el layout se muestre la lista del com.example.dejavucloset.carrito
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        carritoVacio= (LinearLayout) findViewById(R.id.carritoVacio);
        txtSubtotal= (TextView) findViewById(R.id.txtSubtotal);
        infoCarrito= (TextView) findViewById(R.id.infoCarrito);
        pgCarrito= (ProgressBar) findViewById(R.id.pgCarrito);
        listaCarrito= new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.rvCarrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pgCarrito.setVisibility(View.INVISIBLE);

        //Carrito vacio UI
        carritoVacio.setVisibility(View.INVISIBLE);

        //Consultamos los elementos de la lista
        consultarListaCarrito();

        //Llamos al carritoAdapter para el RecyclerView
        carritoAdapter adapter=new carritoAdapter(listaCarrito, this);
        recyclerView.setAdapter(adapter);


        //Inicializamos USUARIO
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("user");
        userID=user.getUid();

        //Snapshot
        //DATABASE pedido-usuario
        DatabaseReference mDatabasePlayers = FirebaseDatabase.getInstance().getReference().child("pedido");

        //Creamos un key para pedido-usuario autoincrementado
        Query mDatabaseHighestPlayer = mDatabasePlayers.orderByKey().limitToLast(1);
        mDatabaseHighestPlayer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        //Creamos una llave
                        int key = (Integer.parseInt(childSnapshot.getKey()));
                        int key2=key+1;

                        key1=""+key2;

                    }
                } else{
                    key1="100001";
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // don't swallow errors
            }
        } );

    }

    //Retornar
    public void irAtras(View view){
        Intent i=new Intent(this, Categorias.class);
        startActivity(i);
    }


    //Mostramos la lista
    public void consultarListaCarrito(){
        carrito_modelo cm=null;

        monto=0;

        Cursor cursor=obtenerDatos();

        if(siDataExiste()){
            while(cursor.moveToNext()){
                cm=new carrito_modelo();
                cm.setCodigo(cursor.getString(0));
                cm.setTitulo(cursor.getString(1));
                cm.setPrecio(cursor.getDouble(2));
                cm.setCantidad(cursor.getInt(3));
                cm.setUrl(cursor.getInt(4));
                cm.setTalla(cursor.getString(5));

                monto=monto+(cursor.getDouble(2)*cursor.getInt(3));

                listaCarrito.add(cm);

                txtSubtotal.setText(String.format("%.2f", monto) );
            }
        } else{
            infoCarrito.setVisibility(View.INVISIBLE);
            carritoVacio.setVisibility(View.VISIBLE);
        }
    }

    //Mostramos la lista
    public void calcularmonto(){
        monto=0;
        Cursor cursor=obtenerDatos();

        if(siDataExiste()){
            while(cursor.moveToNext()){
                monto=monto+(cursor.getDouble(2)*cursor.getInt(3));
                txtSubtotal.setText(String.format("%.2f", monto) );
            }
        } else{
            txtSubtotal.setText("0.00");
            infoCarrito.setVisibility(View.INVISIBLE);
            carritoVacio.setVisibility(View.VISIBLE);
        }
    }


    public void datosPedidos(View view){
        pgCarrito.setVisibility(View.VISIBLE);

        PedidoClass pedido=new PedidoClass();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario perfil=snapshot.getValue(Usuario.class);

                if(perfil!=null){
                    //Creamos un date para que se pueda insertar en
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    strDate = dateFormat.format(date).toString();

                    nombre=perfil.nombre;
                    distrito=perfil.distrito;
                    repartidor=pedido.obtenerRepartidor(distrito);
                    double montototal=(Double.parseDouble(txtSubtotal.getText().toString())+10);

                    //Objeto Pedido
                    PedidoClass pec=new PedidoClass(strDate,"Procesando",nombre,repartidor,montototal, key1);

                    //Enviamos al firebase
                    pedido();

                    pedidoUsuario(pec);

                    quitarCantidad();

                    pgCarrito.setVisibility(View.INVISIBLE);

                    Toast.makeText(Carrito.this, "Pedido Realizado", Toast.LENGTH_LONG).show();
                    mp= MediaPlayer.create(Carrito.this, R.raw.exitoso);
                    mp.start();

                    eliminar();

                    Intent i=new Intent(Carrito.this, Pedidos.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Carrito.this, "Existen problemas", Toast.LENGTH_LONG).show();
            }
        });
    }

    //
    public boolean siDataExiste(){
        Cursor cursor=obtenerDatos();
        if (cursor.moveToFirst()){
            return true;
        } else{
            return false;
        }
    }

    public Cursor obtenerDatos(){
        AdminSQLiteOpenHelper admin = new
                AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor cursor=bd.rawQuery("SELECT * FROM carrito", null);
        return cursor;
    }


    //Elimnamos la lista
    public void eliminar(){
        AdminSQLiteOpenHelper admin = new
                AdminSQLiteOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        bd.delete("carrito", null, null);
    }


    public void pedidoUsuario(PedidoClass pec){

        //llamamos a firebase
        FirebaseDatabase db=FirebaseDatabase.getInstance();

        //Tabla pedidoUsuario
        DatabaseReference node=db.getReference("pedidoUsuario");
        DatabaseReference node1=node.child(userID);

        //Agreamos a la tabla pedidoUsuario la nueva key
        node1.child(key1).setValue(pec);
    }


    public void pedido(){
        Cursor cursor=obtenerDatos();
        carrito_modelo pc;

        //llamamos a firebase
        FirebaseDatabase db=FirebaseDatabase.getInstance();

        //Tabla Pedido
        DatabaseReference node=db.getReference("pedido");
        DatabaseReference node22=node.child(key1);

        //Llamamos a lista
        while(cursor.moveToNext()){
            pc=new carrito_modelo(cursor.getString(0),cursor.getString(1)
                    ,cursor.getString(5),cursor.getInt(3),cursor.getInt(4),cursor.getDouble(2));

            String codigo=cursor.getString(0);
            node22.child(codigo).setValue(pc);
        }
    }

    public void quitarCantidad(){
        Cursor cursor=obtenerDatos();

        //llamamos a firebase
        FirebaseDatabase db=FirebaseDatabase.getInstance();

        DatabaseReference node=db.getReference("productos");
        //Llamamos a lista
        while(cursor.moveToNext()){
            String codigo=cursor.getString(0);
            String talla=cursor.getString(5);
            int cantidad=cursor.getInt(3);

            DatabaseReference node2=node.child(codigo).child("talla").child(talla).child("stock");
            node2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int stock=Integer.parseInt(dataSnapshot.getValue().toString());
                    int nuevaCantidad=stock-cantidad;

                    añadir(codigo,talla,nuevaCantidad);
                    node2.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void añadir(String codigo, String talla, int cant){
        FirebaseDatabase db=FirebaseDatabase.getInstance();

        DatabaseReference node=db.getReference("productos");
        DatabaseReference node2=node.child(codigo).child("talla").child(talla).child("stock");
        node2.setValue(cant);
    }

}