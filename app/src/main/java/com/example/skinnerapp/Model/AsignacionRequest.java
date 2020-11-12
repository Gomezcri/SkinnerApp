package com.example.skinnerapp.Model;

public class AsignacionRequest {
    Integer id_lesion;
    Integer id_doctor;
    Integer id_paciente;
    Integer id_lugar;
    String tipo_notificacion;

    public AsignacionRequest(Integer id_lesion, Integer id_doctor, Integer id_paciente, Integer id_lugar, String tipo_notificacion) {
        this.id_lesion = id_lesion;
        this.id_doctor = id_doctor;
        this.id_paciente = id_paciente;
        this.id_lugar = id_lugar;
        this.tipo_notificacion = tipo_notificacion;
    }
}
