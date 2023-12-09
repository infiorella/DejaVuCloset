package com.example.dejavucloset.carrito;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dejavucloset.AdminSQLiteOpenHelper;
import com.example.dejavucloset.R;

import java.util.ArrayList;

public class carritoAdapter extends RecyclerView.Adapter<carritoAdapter.myviewholder>{

    //ALMACENA LOS DATOS DE NUESTRO MODELO
    ArrayList<carrito_modelo> listaCarrito;
    Context context;
    private Button btnCheckout;

    //LO DEFINIMOS EN EL CONSTRUCTOR
    public carritoAdapter(ArrayList<carrito_modelo> listaCarrito, Context context){
        this.listaCarrito=listaCarrito;
        this.context = context;
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //La vista será la del item_Carrito
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito,null, false);
        return new myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull carritoAdapter.myviewholder holder, int position) {
        holder.ivUrlC.setImageResource(listaCarrito.get(position).getUrl());
        holder.tvTituloC.setText(listaCarrito.get(position).getTitulo());
        holder.tvPrecioC.setText(""+listaCarrito.get(position).getPrecio());
        holder.tvTallaC.setText(listaCarrito.get(position).getTalla());
        holder.tvCantidadC.setText(""+listaCarrito.get(position).getCantidad());

        //Borrar de la lista
        holder.ibQuitarC.setOnClickListener((View view)-> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            //Mnesaje de confirmacion
            builder.setTitle("Confirmacion");
            builder.setMessage("¿Deseas eliminar " + listaCarrito.get(position).getTitulo() + "?");
            builder.setCancelable(false);
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {

                //Al hacerle click en SÍ
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AdminSQLiteOpenHelper admin = new
                            AdminSQLiteOpenHelper(context, "administracion", null, 1);
                    //Para editar la base de datos
                    SQLiteDatabase bd = admin.getWritableDatabase();

                    //Obtenemos el titulo del articulo
                    String titulo = listaCarrito.get(position).getTitulo();

                    //Borramos
                    int n = bd.delete("carrito", "titulo='" + titulo + "'", null);

                    if (n == 1) {
                        Toast.makeText(context, "Se eliminó artículo", Toast.LENGTH_SHORT).show();
                        listaCarrito.remove(listaCarrito.get(position));

                        notifyDataSetChanged();
                        ((Carrito) context).calcularmonto();
                    } else{
                        Toast.makeText(context, "No existe artículo para eliminar", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("No", null);
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        private ImageView ivUrlC;
        private TextView tvTituloC, tvPrecioC, tvTallaC, tvCantidadC;
        private ImageButton ibQuitarC;

        public myviewholder(View itemView){
            super(itemView);

            ivUrlC= (ImageView) itemView.findViewById(R.id.ivUrlC);
            tvTituloC= (TextView) itemView.findViewById(R.id.tvTituloC);
            tvPrecioC= (TextView) itemView.findViewById(R.id.tvPrecioC);
            tvTallaC= (TextView) itemView.findViewById(R.id.tvTallaC);
            tvCantidadC= (TextView) itemView.findViewById(R.id.tvCantidadC);
            ibQuitarC= (ImageButton) itemView.findViewById(R.id.ibQuitarC);

        }
    }



}