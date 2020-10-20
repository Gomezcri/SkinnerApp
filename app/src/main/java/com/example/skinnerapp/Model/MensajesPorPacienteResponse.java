package com.example.skinnerapp.Model;

public class MensajesPorPacienteResponse {
    private Integer id_origen_usuario;
    private Integer id_destino_usuario;
    private Integer id_lesion;
    private String fecha;
    private String nombre_destino;
    private String apellido_destino;
    private String mensaje;
    private String descripcion;
    private String imagen;

   public MensajesPorPacienteResponse(Integer id_origen_usuario, Integer id_lesion, Integer id_destino_usuario,String fecha, String nombre_destino, String apellido_destino, String mensaje,String lesion, String imagen){
       this.id_origen_usuario = id_origen_usuario;
       this.id_destino_usuario = id_destino_usuario;
       this.id_lesion = id_lesion;
       this.fecha = fecha;
       this.nombre_destino = nombre_destino;
       this.apellido_destino = apellido_destino;
       this.mensaje = mensaje;
       this.descripcion=descripcion;
       this.imagen=imagen;
   }

    public Integer getId_destino_usuario() {
        return id_destino_usuario;
    }

    public Integer getId_origen_usuario() {
        return id_origen_usuario;
    }

    public Integer getId_lesion() {
        return id_lesion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getNombre_destino() {
        return nombre_destino;
    }

    public String getApellido_destino() {
        return apellido_destino;
    }
    public String getImagen() {
    return imagen;
    }

    public String getDescripcion(){return descripcion;}
    public String getMensaje() {
        return mensaje;
    }
}
