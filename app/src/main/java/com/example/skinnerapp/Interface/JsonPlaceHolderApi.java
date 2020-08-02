package com.example.skinnerapp.Interface;

import com.example.skinnerapp.Model.ActualizarUsuarioResponse;
import com.example.skinnerapp.Model.AnalizarImagenRequest;
import com.example.skinnerapp.Model.AnalizarImagenResponse;
import com.example.skinnerapp.Model.ObtenerUsuarioResponse;
import com.example.skinnerapp.Model.RegistrarUsuarioRequest;
import com.example.skinnerapp.Model.RegistrarUsuarioResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @POST("/AnalizarImagen/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<AnalizarImagenResponse> getAnalisisImagen(@Body AnalizarImagenRequest body);

    @POST("/usuarios/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArrayList<RegistrarUsuarioResponse>> postRegistrarUsuario(@Body RegistrarUsuarioRequest request);

    @GET()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArrayList<ObtenerUsuarioResponse>> getUserById(@Url() String url);

    @PUT()
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ActualizarUsuarioResponse> putUserById(@Url() String url,@Body RegistrarUsuarioRequest request);

}