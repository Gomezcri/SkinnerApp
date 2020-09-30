package com.example.skinnerapp.Interface;

import com.example.skinnerapp.Model.ActualizarContraseñaRequest;
import com.example.skinnerapp.Model.ActualizarContraseñaResponse;
import com.example.skinnerapp.Model.ActualizarUsuarioRequest;
import com.example.skinnerapp.Model.ActualizarUsuarioResponse;
import com.example.skinnerapp.Model.AsignacionRequest;
import com.example.skinnerapp.Model.AsignacionResponse;
import com.example.skinnerapp.Model.LoginUsuarioRequest;
import com.example.skinnerapp.Model.LoginUsuarioResponse;
import com.example.skinnerapp.Model.MensajeResponse;
import com.example.skinnerapp.Model.MensajesPorPacienteResponse;
import com.example.skinnerapp.Model.RecuperarContraseñaRequest;
import com.example.skinnerapp.Model.RecuperarcontraseñaResponse;
import com.example.skinnerapp.Model.SendMessageRequest;
import com.example.skinnerapp.Model.SendMessageResponse;
import com.example.skinnerapp.Model.TratamientoResponse;
import com.example.skinnerapp.Model.RegistrarLesionRequest;
import com.example.skinnerapp.Model.RegistrarLesionResponse;
import com.example.skinnerapp.Model.HistoricoResponse;
import com.example.skinnerapp.Model.LesionesResponse;
import com.example.skinnerapp.Model.ObtenerUsuarioResponse;
import com.example.skinnerapp.Model.RegistrarHistoricoRequest;
import com.example.skinnerapp.Model.RegistrarHistoricoResponse;
import com.example.skinnerapp.Model.RegistrarUsuarioRequest;
import com.example.skinnerapp.Model.RegistrarUsuarioResponse;
import com.example.skinnerapp.Model.UpdateLesionRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @POST("/lesiones/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<LesionesResponse> getAnalisisImagen(@Body RegistrarLesionRequest body);

    @GET()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ObtenerUsuarioResponse> getUserById(@Url() String url);

    @PUT()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ActualizarUsuarioResponse> putUserById(@Url() String url,@Body ActualizarUsuarioRequest request);

    @PUT()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ActualizarContraseñaResponse> putUpdatePassword(@Url() String url, @Body ActualizarContraseñaRequest request);

    @GET()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArrayList<LesionesResponse>> getLesionesById(@Url() String url);

    @GET()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArrayList<HistoricoResponse>> getHistoricoByLesionId(@Url() String url);

    @GET()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArrayList<TratamientoResponse>> getRecomendacionesById(@Url() String url);

    @POST("/historial/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<RegistrarHistoricoResponse> postRegistrarHistorico(@Body RegistrarHistoricoRequest request);

    @POST("/login/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<LoginUsuarioResponse> getUsuarioLogin(@Body LoginUsuarioRequest request);

    @POST("/asignaciones/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<AsignacionResponse> postRegistrarAsignacion(@Body AsignacionRequest request);

    @PUT()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<RegistrarLesionResponse> putLesion(@Url() String url,@Body UpdateLesionRequest req);

    @POST("/usuarios/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<RegistrarUsuarioResponse> postRegistrarUsuario(@Body RegistrarUsuarioRequest request);

    @POST("/recuperar_password/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<RecuperarcontraseñaResponse> postRecuperarContraseña(@Body RecuperarContraseñaRequest request);

    @PUT()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<Void> postCerrarSesion(@Url() String url);

    @GET()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArrayList<MensajeResponse>> getMensajesByLesionId(@Url() String url);

    @POST("/mensajes/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<SendMessageResponse> postSendMessage(@Body SendMessageRequest request);

    @GET()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArrayList<MensajesPorPacienteResponse>> getMensajesByPacienteId(@Url() String url);


}