package com.example.skinnerapp.Model;

public class SendMessageRequest {
    private Integer id_origen_usuario;
    private Integer id_destino_usuario;
    private String mensaje;
    private Integer id_lesion;

    public SendMessageRequest(Integer id_origen_usuario, Integer id_destino_usuario, String mensaje, Integer id_lesion) {
        this.id_origen_usuario = id_origen_usuario;
        this.id_destino_usuario = id_destino_usuario;
        this.mensaje = mensaje;
        this.id_lesion = id_lesion;
    }

}
