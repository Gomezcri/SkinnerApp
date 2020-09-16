package com.example.skinnerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.TratamientoResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.getConnection;

public class TratamientoActivity extends AppCompatActivity {

    private Integer id_lesion;
    private ArrayList<TratamientoResponse> datos;
    private ListView lista;
    private Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tratamiento);
        lista = (ListView) findViewById(R.id.lista_recomendaciones);
        contexto = this;
        id_lesion = getIntent().getIntExtra("id_lesion", 0);
        if(id_lesion != 0)
            datos = obtenerRecomendaciones();
    }

    private ArrayList<TratamientoResponse> obtenerRecomendaciones() {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        Date currentTime = Calendar.getInstance().getTime();

        Call<ArrayList<TratamientoResponse>> call = service.getRecomendacionesById("/lesion_tratamientos/" + id_lesion);
        call.enqueue(new Callback<ArrayList<TratamientoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<TratamientoResponse>> call, Response<ArrayList<TratamientoResponse>> response) {
                datos = response.body();
                if (datos != null)
                    lista.setAdapter(new AdaptadorRecomendacion(contexto, datos));

            }
            @Override
            public void onFailure(Call<ArrayList<TratamientoResponse>> call, Throwable t) {
            }

        });

        return datos;
    }
}