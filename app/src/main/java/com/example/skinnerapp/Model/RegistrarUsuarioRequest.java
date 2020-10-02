package com.example.skinnerapp.Model;

public class RegistrarUsuarioRequest {
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private Integer id_ciudad;
    private Integer id_rol;

    public RegistrarUsuarioRequest(String email, String password, String nombre, String apellido,String direccion,String telefono,Integer id_city) {
        this.email=email;
        this.password=password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.id_ciudad=id_city;
        this.id_rol = 2;
    }
}
