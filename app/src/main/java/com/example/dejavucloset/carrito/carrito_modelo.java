package com.example.dejavucloset.carrito;


public class carrito_modelo {
    String codigo, titulo, talla;
    int cantidad, url;
    double precio;

    public carrito_modelo() {
    }

    public carrito_modelo(String codigo, String titulo, String talla, int cantidad, int url, double precio) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.talla = talla;
        this.cantidad = cantidad;
        this.url = url;
        this.precio = precio;
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

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }


}
