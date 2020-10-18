package com.example.skinnerapp.Model;

public class LesionesResponse {
    private Integer id;
    private Integer id_paciente;
    private Integer id_doctor;
    private Integer id_lugar;
    private String descripcion;
    private Integer id_tipo;
    private String ubicacion;
    private String fecha_creacion;
    private String imagen;
    private Boolean activo;
    private Integer id_historial;

    public Integer getId_lugar() {
        return id_lugar;
    }

    public Integer getId_historial() {
        return id_historial;
    }

    public Integer getId_paciente() {
        return id_paciente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getId_tipo() {
        return id_tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public String getImage() {
        return imagen;
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