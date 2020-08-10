package com.example.skinnerapp.Model;

public class HistoricoResponse {
    private Integer id;
    private Integer id_lesion;
    private Integer id_doctor;
    private String descripcion;
    private String imagen;
    private String fecha;
    private Boolean activo;

    public Integer getId_lesion() {
        return id_lesion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public Integer getId() {
        return id;
    }

    public Integer getId_doctor() {
        return id_doctor;
    }

    public Boolean getActivo() {
        return activo;
    }
}
