package com.example.skinnerapp.Model;

public class AsignacionRequest {
    Integer id_lesion;
    Integer id_doctor;
    Integer id_paciente;

    public AsignacionRequest(Integer id_lesion, Integer id_doctor, Integer id_paciente) {
        this.id_lesion = id_lesion;
        this.id_doctor = id_doctor;
        this.id_paciente = id_paciente;
    }
}