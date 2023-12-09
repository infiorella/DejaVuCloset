package com.example.dejavucloset.pedidos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.dejavucloset.AdminSQLiteOpenHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PedidoClass {
    private String fecha;
    private String estado;
    private String nomUsuario;
    private String repartidor;
    private double monto;
    private String codigo;

    public PedidoClass(String fecha, String estado, String nomUsuario, String repartidor, double monto, String codigo) {
        this.fecha = fecha;
        this.estado = estado;
        this.nomUsuario = nomUsuario;
        this.repartidor = repartidor;
        this.monto = monto;
        this.codigo = codigo;
    }

    public PedidoClass() {
    }

    public String obtenerRepartidor(String distrito){
        String repartidor;
        if(distrito.equals("Ate") || distrito.equals("Barranco")|| distrito.equals("Breña") || distrito.equals("Carabayllo")
            ||distrito.equals("Chaclacayo") || distrito.equals("Chorrillos")|| distrito.equals("Cieneguilla") || distrito.equals("Comas")){
            repartidor="José";
        }  else if(distrito.equals("El Agustino") || distrito.equals("Independencia")|| distrito.equals("Jesús María") || distrito.equals("La Molina")
                ||distrito.equals("La Victoria") || distrito.equals("Lince")|| distrito.equals("Los Olivos") || distrito.equals("Lurigancho")){
            repartidor="Martin";
        }  else if(distrito.equals("Lurín") || distrito.equals("Magdalena del Mar")|| distrito.equals("Miraflores") || distrito.equals("Pachacamac")
                ||distrito.equals("Pucusuna") || distrito.equals("Pueblo Libre")|| distrito.equals("Puente Piedra") || distrito.equals("Punta Hermosa")) {
            repartidor = "Louis";
        }else if(distrito.equals("Punta Negra") || distrito.equals("Rímac")|| distrito.equals("San Bartolo") || distrito.equals("San Borja")
                ||distrito.equals("San Isidro") || distrito.equals("San Juan de Lurigancho")|| distrito.equals("San Juan de Miraflores") || distrito.equals("San Luis")) {
            repartidor = "Louis";
        }else if(distrito.equals("San Martin de Porres") || distrito.equals("San Miguel")|| distrito.equals("Santa Anita") || distrito.equals("Santa María del Mar")
                ||distrito.equals("Santa Rosa") || distrito.equals("Santiago de Surco")|| distrito.equals("Surquillo") || distrito.equals("Villa El Salvador")) {
            repartidor = "Louis";
        } else{
            repartidor="Stevan";
        }
        return repartidor;
    }



    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getCodigo() { return codigo;}

    public void setCodigo(String codigo) {this.codigo = codigo;}

}
