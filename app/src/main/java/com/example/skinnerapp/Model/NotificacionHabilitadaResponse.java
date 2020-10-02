package com.example.skinnerapp.Model;

public class NotificacionHabilitadaResponse {
    private Boolean recibir_notificaciones;

    public NotificacionHabilitadaResponse(Boolean recibir_notificaciones){
        this.recibir_notificaciones = recibir_notificaciones;
    }

    public Boolean getRecibir_notificaciones() {
        return recibir_notificaciones;
    }
}
