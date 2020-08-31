package com.example.skinnerapp.Interface;

import com.example.skinnerapp.Model.ActualizarUsuarioResponse;
import com.example.skinnerapp.Model.LoginUsuarioRequest;
import com.example.skinnerapp.Model.LoginUsuarioResponse;
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

    @POST("/usuarios/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<RegistrarUsuarioResponse> postRegistrarUsuario(@Body RegistrarUsuarioRequest request);

    @GET()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArrayList<ObtenerUsuarioResponse>> getUserById(@Url() String url);

    @PUT()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ActualizarUsuarioResponse> putUserById(@Url() String url,@Body RegistrarUsuarioRequest request);

    @GET()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArrayList<LesionesResponse>> getLesionesById(@Url() String url);

    @GET()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArrayList<HistoricoResponse>> getHistoricoByLesionId(@Url() String url);

    @POST("/historial/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<RegistrarHistoricoResponse> postRegistrarHistorico(@Body RegistrarHistoricoRequest request);

    @POST("/login/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<LoginUsuarioResponse> getUsuarioLogin(@Body LoginUsuarioRequest request);

    @PUT()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<RegistrarLesionResponse> putLesion(@Url() String url,@Body UpdateLesionRequest req);
}