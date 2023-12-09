package com.example.dejavucloset.productos;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dejavucloset.AdminSQLiteOpenHelper;
import com.example.dejavucloset.carrito.Carrito;
import com.example.dejavucloset.deseoSQLiteOpenHelper;
import com.example.dejavucloset.DetalleProducto;
import com.example.dejavucloset.R;

import java.util.List;

public class productoAdapter extends RecyclerView.Adapter<productoAdapter.myviewholder>
        implements View.OnClickListener{
    List<ProductoClass> listaProducto;
    Context context;

    private View.OnClickListener listener;

    public productoAdapter(Context context, List<ProductoClass> listaProducto) {
        this.listaProducto = listaProducto;
        this.context = context;
    }

    @NonNull
    @Override
    public productoAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //La vista será la del item_Carrito
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto,null, false);

        return new productoAdapter.myviewholder(view);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull productoAdapter.myviewholder holder, int position) {
        String name = listaProducto.get(position).getUrl();
        int id = context.getResources().getIdentifier(name, "drawable",
                context.getPackageName());

        holder.imgProductoItem.setImageResource(id);
        holder.tvTituloItem.setText(listaProducto.get(position).getTitulo());
        holder.tvPrecioItem.setText(""+listaProducto.get(position).getPrecio());

        holder.producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetalleProducto.class);
                intent.putExtra("codigo",listaProducto.get(position).getCodigo());
                intent.putExtra("titulo",listaProducto.get(position).getTitulo());
                intent.putExtra("precio",""+listaProducto.get(position).getPrecio());
                intent.putExtra("url",id);
                context.startActivity(intent);
            }
        });

        //Vemos si producto existe
        int ver=verSiExiste(listaProducto.get(position).getCodigo());
        if(ver==1){
            holder.corazonItem.setImageResource(R.drawable.heart_pink);
        }


        holder.corazonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ver=verSiExiste(listaProducto.get(position).getCodigo());
                if(ver==1){
                    eliminar( listaProducto.get(position).getCodigo());
                    holder.corazonItem.setImageResource(R.drawable.heart);
                }else{
                    añadir(listaProducto.get(position).getCodigo(),listaProducto.get(position).getTitulo(),listaProducto.get(position).getPrecio(), id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProducto.size();
    }


    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        private ImageView imgProductoItem;
        private TextView tvTituloItem, tvPrecioItem;
        private ImageButton corazonItem;
        private CardView producto;

        public myviewholder(View itemView){
            super(itemView);
            imgProductoItem= (ImageView) itemView.findViewById(R.id.imgProductoItem);
            tvTituloItem= (TextView) itemView.findViewById(R.id.tvTituloItem);
            tvPrecioItem= (TextView) itemView.findViewById(R.id.tvPrecioItem);
            corazonItem= (ImageButton) itemView.findViewById(R.id.corazonItem);
            producto= (CardView)itemView.findViewById(R.id.producto);
        }
    }

    public int verSiExiste(String codigo){
        deseoSQLiteOpenHelper admin = new
                deseoSQLiteOpenHelper(context, "baseDeseos", null, 1);

        SQLiteDatabase bd = admin.getWritableDatabase();
        int i;
        Cursor fila = bd.rawQuery("select codigo,titulo,precio,url from deseos where codigo='" + codigo + "'", null);
        //Existe
        if (fila.moveToFirst()) {
            i=1;
        //No existe
        } else{
            i=0;
        }
        return i;
    }

    public void añadir(String codigo, String titulo, double precio, int url){
        deseoSQLiteOpenHelper admin = new
                deseoSQLiteOpenHelper(context, "baseDeseos", null, 1);

        //Para editar la base de datos
        SQLiteDatabase bd = admin.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("codigo",codigo);
        registro.put("titulo",titulo);
        registro.put("precio", precio);
        registro.put("url", url);

        float res = bd.insert("deseos", null, registro);
        if (res == -1) {
            Toast.makeText(context, "Error para agregar producto", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Añadido a lista de deseos", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
    }

    public void eliminar( String codigo){
        deseoSQLiteOpenHelper admin = new
                deseoSQLiteOpenHelper(context, "baseDeseos", null, 1);
        //Para editar la base de datos
        SQLiteDatabase bd = admin.getWritableDatabase();

        //Borramos
        int n = bd.delete("deseos", "codigo='" + codigo + "'", null);

        if (n == 1) {
            Toast.makeText(context, "Artíclo quitado de lista de deseos", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        } else{
            Toast.makeText(context, "No existe artículo para eliminar", Toast.LENGTH_SHORT).show();
        }
    }

}
