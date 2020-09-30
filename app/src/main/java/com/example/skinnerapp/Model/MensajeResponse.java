package com.example.skinnerapp.Model;

public class MensajeResponse extends Mensaje {

    private String fecha;

    public MensajeResponse() {
    }

    public MensajeResponse(String hora) {
        this.fecha = hora;
    }

    public MensajeResponse(Integer id, Integer id_origen_usuario, Integer id_destino_usuario, String nombre_destino, String apellido_destino, String fecha, String mensaje, String nombre_origen, String apellido_origen) {
        super(id, id_origen_usuario, id_destino_usuario, nombre_destino, apellido_destino,mensaje,nombre_origen,apellido_origen);
        this.fecha = fecha;
    }

    public String getHora() {
        return fecha;
    }

    public void setHora(String hora) {
        this.fecha = hora;
    }
}
