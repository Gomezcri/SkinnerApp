package com.example.skinnerapp.Model;

public class ActualizarUsuarioRequest {
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private Integer id_rol;
    private Integer id_ciudad;

    public ActualizarUsuarioRequest(String nombre, String apellido,String direccion,String telefono, Integer id_ciudad)
    {
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.id_rol = 1;
        this.id_ciudad = id_ciudad;
    }
}
