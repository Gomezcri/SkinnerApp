package com.example.skinnerapp.Model;

public class RegistrarUsuarioRequest {
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private Integer id_rol;

    public RegistrarUsuarioRequest(String nombre, String apellido,String direccion,String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.id_rol = 1;
    }

}
