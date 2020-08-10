package com.example.skinnerapp.Model;

public class RegistrarHistoricoRequest {
    private Integer id_lesion;
    private Integer id_doctor;
    private String descripcion;
    private String imagen;
    private String fecha;
    private Integer id_tipo;
    //tipo de lesion (enviar como parametro)

    public RegistrarHistoricoRequest(Integer id_lesion, Integer id_doctor,String descripcion,String imagen,String fecha, Integer id_tipo) {
        this.imagen = imagen;
        this.id_lesion = id_lesion;
        this.id_doctor = id_doctor;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.id_tipo = id_tipo;
    }
}
