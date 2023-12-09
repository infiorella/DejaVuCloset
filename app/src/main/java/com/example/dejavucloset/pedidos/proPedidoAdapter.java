package com.example.dejavucloset.pedidos;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dejavucloset.R;
import com.example.dejavucloset.AdminSQLiteOpenHelper;
import com.example.dejavucloset.carrito.carrito_modelo;

import java.util.ArrayList;

public class proPedidoAdapter extends RecyclerView.Adapter<proPedidoAdapter.myviewholder>{
    //ALMACENA LOS DATOS DE NUESTRO MODELO
    ArrayList<carrito_modelo> listaCarrito;
    Context context;

    //LO DEFINIMOS EN EL CONSTRUCTOR
    public proPedidoAdapter(ArrayList<carrito_modelo> listaCarrito, Context context){
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
    public void onBindViewHolder(@NonNull proPedidoAdapter.myviewholder holder, int position) {
        holder.ivUrlC.setImageResource(listaCarrito.get(position).getUrl());
        holder.tvTituloC.setText(listaCarrito.get(position).getTitulo());
        holder.tvPrecioC.setText("" + listaCarrito.get(position).getPrecio());
        holder.tvTallaC.setText(listaCarrito.get(position).getTalla());
        holder.tvCantidadC.setText("" + listaCarrito.get(position).getCantidad());

        //Borrar de la lista
        holder.ibQuitarC.setVisibility(View.INVISIBLE);
    };

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
