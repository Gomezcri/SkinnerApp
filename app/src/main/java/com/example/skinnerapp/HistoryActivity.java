package com.example.skinnerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.HistoricoResponse;
import com.example.skinnerapp.ui.home.AdaptadorLesion;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.getConnection;

public class HistoryActivity extends AppCompatActivity {
    private ListView lista;
    private ArrayList<HistoricoResponse> datos = null;
    private Button btn_add_historico;
    private Integer id_lesion;
    private Integer id_doctor;
    private Integer id_tipo;
    private Context contexto;

    public final static int RESULT_ACTIVITY_LESION = 122;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        contexto = this;
        lista = (ListView) findViewById(R.id.lista_historico);
        btn_add_historico = (Button) findViewById(R.id.button_add_historico);
        id_lesion = getIntent().getIntExtra("id_lesion",0);
        id_doctor = getIntent().getIntExtra("id_doctor",0);
        id_tipo = getIntent().getIntExtra("id_tipo",0);
        datos = obtenerHistorico(id_lesion);
        btn_add_historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddLesionActivity();
            }
        });
    }

    private ArrayList<HistoricoResponse> obtenerHistorico(Integer id_lesion) {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

        Call<ArrayList<HistoricoResponse>> call= service.getHistoricoByLesionId("/historial/lesion/"+id_lesion);
        call.enqueue(new Callback<ArrayList<HistoricoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<HistoricoResponse>> call, Response<ArrayList<HistoricoResponse>> response) {
                datos = response.body();
                Toast.makeText(contexto, "Se actualizó correctamente los datos de su usuario", Toast.LENGTH_SHORT).show();
                if(datos != null)
                    lista.setAdapter(new AdaptadorHistorico(contexto,datos));
            }
            @Override
            public void onFailure(Call<ArrayList<HistoricoResponse>> call, Throwable t) {
                Toast.makeText(contexto, "Error al registrar usuario. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return datos;
    }

    private void openAddLesionActivity(){
        Intent resultIntent = new Intent(this, AddLesionActivity.class);
        resultIntent.putExtra("id_lesion", id_lesion);  // put data that you want returned to activity A
        resultIntent.putExtra("id_doctor", id_doctor);  // put data that you want returned to activity A
        startActivityForResult(resultIntent,RESULT_ACTIVITY_LESION);
    }
}