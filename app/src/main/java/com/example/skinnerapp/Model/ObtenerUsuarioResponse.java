package com.example.skinnerapp.Model;

public class ObtenerUsuarioResponse {
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private Integer id;
    private Boolean activo;
    private Integer id_rol;
    private String email;

    public ObtenerUsuarioResponse(String nombre, String apellido, String direccion, String telefono, Integer id, Boolean activo, Integer id_rol, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.id = id;
        this.activo = activo;
        this.id_rol = id_rol;
        this.email = email;
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

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
}
