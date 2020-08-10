package com.example.skinnerapp.Model;

public class RegistrarLesionRequest {
    private String imagen;
    private Integer id_paciente;
    private String ubicacion;
    private String seccion;
    private String descripcion;
    private String fecha_creacion;

    public RegistrarLesionRequest(String imagen, String ubicacion, String seccion, Integer id_paciente, String descripcion, String fecha_creacion) {
        this.imagen = imagen;
        this.ubicacion = ubicacion;
        this.id_paciente = id_paciente;
        this.seccion = seccion;
        this.descripcion = descripcion;
        this.fecha_creacion = fecha_creacion;
    }
}

