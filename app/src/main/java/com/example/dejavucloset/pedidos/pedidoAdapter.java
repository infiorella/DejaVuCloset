package com.example.dejavucloset.pedidos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dejavucloset.DetallesPedido;
import com.example.dejavucloset.R;
import com.example.dejavucloset.carrito.Carrito;

import java.util.ArrayList;
import java.util.List;


public class pedidoAdapter extends RecyclerView.Adapter<pedidoAdapter.myviewholder>
        implements View.OnClickListener{
    private List<PedidoClass> listaPedido=new ArrayList<>();
    private Context mContext;
    private String id;

    private View.OnClickListener listener;
    //Constructor
    public pedidoAdapter(Context mContext, List<PedidoClass> listaPedido) {
        this.mContext=mContext;
        this.listaPedido = listaPedido;
    }

    @NonNull
    @Override
    public pedidoAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido,null, false);

        view.setOnClickListener(this);

        return new pedidoAdapter.myviewholder(view);
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull pedidoAdapter.myviewholder holder, int position) {
        PedidoClass pedido=listaPedido.get(position);
        String estado=pedido.getEstado();
        switch (estado){
            case "Procesando":
                holder.tvEstado.setTextColor(Color.rgb(70,130,180));
                break;

            case "Enviado":
                holder.tvEstado.setTextColor(Color.rgb(255,0,0));
                break;

            case "Entregado":
                holder.tvEstado.setTextColor(Color.rgb(76,145,65));
                break;
        }

        holder.tvEstado.setText(pedido.getEstado());
        holder.tvMontoPedido.setText(""+pedido.getMonto());
        holder.imgPedido.setImageResource(R.drawable.shop);
        holder.tvFecha.setText(pedido.getFecha());

        holder.tvIdPedido.setText(pedido.getCodigo());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetallesPedido.class);
                intent.putExtra("id",pedido.getCodigo());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPedido.size();
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        private ImageView imgPedido;
        private CardView cv;
        private TextView tvIdPedido, tvMontoPedido, tvEstado, tvFecha;

        public myviewholder(View itemView){
            super(itemView);
            imgPedido= (ImageView) itemView.findViewById(R.id.imgPedido);
            tvIdPedido= (TextView) itemView.findViewById(R.id.tvIdPedido);
            tvMontoPedido= (TextView) itemView.findViewById(R.id.tvMontoPedido);
            tvEstado= (TextView) itemView.findViewById(R.id.tvEstado);
            tvFecha= (TextView) itemView.findViewById(R.id.tvFecha);
            cv= (CardView) itemView.findViewById(R.id.pedido1);
        }
    }
}
