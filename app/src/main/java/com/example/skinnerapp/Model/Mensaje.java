package com.example.skinnerapp.Model;

public class Mensaje {
    private Integer id;
    private Integer id_origen_usuario;
    private Integer id_destino_usuario;
    private String nombre_destino;
    private String apellido_destino;
    private String nombre_origen;
    private String apellido_origen;
    private String mensaje;

    public Mensaje() {
    }

    public Mensaje(Integer id, Integer id_origen_usuario, Integer id_destino_usuario, String nombre_destino, String apellido_destino, String mensaje, String nombre_origen, String apellido_origen) {
        this.mensaje = mensaje;
        this.nombre_destino = nombre_destino;
        this.apellido_destino = apellido_destino;
        this.id_origen_usuario = id_origen_usuario;
        this.id_destino_usuario = id_destino_usuario;
        this.nombre_origen = nombre_origen;
        this.apellido_origen = apellido_origen;
        this.id = id;
    }

    public String getNombre_origen() {
        return nombre_origen;
    }

    public String getApellido_origen() {
        return apellido_origen;
    }

    public Integer getId_destino_usuario() {
        return id_destino_usuario;
    }

    public Integer getId_origen_usuario() {
        return id_origen_usuario;
    }

    public String getNombre_destino() {
        return nombre_destino;
    }

    public String getApellido_destino() {
        return apellido_destino;
    }

    public String getMensaje() {
        return mensaje;
    }
}
