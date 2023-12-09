package com.example.dejavucloset.iniciosesion;

public class Usuario {
    public String nombre, distrito, direccion, celular, email, password;

    public Usuario(){
    }

    public Usuario(String nombre, String distrito, String direccion, String celular, String email, String password) {
        this.nombre = nombre;
        this.distrito = distrito;
        this.direccion = direccion;
        this.celular = celular;
        this.email = email;
        this.password = password;
    }
}
