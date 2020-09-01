package com.example.skinnerapp.Model;

public class LoginUsuarioResponse {

    private Integer id;
    private String email;
    private String nombre;
    private String apellido;

    public LoginUsuarioResponse(Integer id, String email, String nombre, String apellido) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Integer getId() {
        return id;
    }
}
