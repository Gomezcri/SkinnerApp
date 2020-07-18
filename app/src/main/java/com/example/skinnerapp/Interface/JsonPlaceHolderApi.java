package com.example.skinnerapp.Interface;

import com.example.skinnerapp.Model.AnalizarImagenRequest;
import com.example.skinnerapp.Model.AnalizarImagenResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @POST("/AnalizarImagen/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<AnalizarImagenResponse> savePost(@Body AnalizarImagenRequest body);

    @GET("/Test")
    Call<String> callTest();
}