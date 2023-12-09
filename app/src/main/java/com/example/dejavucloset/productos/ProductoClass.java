package com.example.dejavucloset.productos;

import java.util.Date;

public class ProductoClass {
    private String codigo;
    private String titulo;
    private String descripcion;
    private double precio;
    private String fecha;
    private  String url;
    private String estado;

    public ProductoClass() {
    }

    public ProductoClass(String codigo, String titulo, String descripcion, double precio, String fecha, String url, String estado) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.fecha = fecha;
        this.url = url;
        this.estado = estado;
    }



    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
