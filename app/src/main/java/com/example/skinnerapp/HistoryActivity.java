package com.example.skinnerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.HistoricoResponse;
import com.example.skinnerapp.Model.NotificacionHabilitadaResponse;
import com.example.skinnerapp.Model.ObtenerUsuarioResponse;
import com.example.skinnerapp.Model.TratamientoResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.skinnerapp.ResponseActivity.RESULT_ACTIVITY_GPS;
import static util.Util.getConnection;

public class HistoryActivity extends AppCompatActivity {
    private ListView lista;
    private ArrayList<HistoricoResponse> datos = null;
    private Button btn_add_historico;
    private Integer id_lesion;
    private Integer id_doctor;
    private Integer id_tipo;
    private Context contexto;
    private Button btn_recomendaciones;
    private ImageView btn_msj;
    private Integer id_paciente;
    private String nombre_doctor;
    private Button btn_map;

    public final static int RESULT_ACTIVITY_RECOMENDACION = 144;
    public final static int RESULT_ACTIVITY_LESION = 141;
    public final static int RESULT_ACTIVITY_MESSAGE = 150;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        contexto = this;
        lista = (ListView) findViewById(R.id.lista_historico);
        btn_add_historico = (Button) findViewById(R.id.button_add_historico);
        btn_recomendaciones= (Button) findViewById(R.id.button_tratamientos);
        btn_map = (Button) findViewById(R.id.button_gps);
        btn_recomendaciones.setVisibility(View.GONE);
        btn_msj = (ImageView)findViewById(R.id.img_msj);
        id_lesion = getIntent().getIntExtra("id_lesion",0);
        obtenerRecomendaciones();
        id_doctor = getIntent().getIntExtra("id_doctor",0);
        id_paciente = getIntent().getIntExtra("id_paciente",0);
        if(id_doctor != 0)
        {
            habilitarMensajeria(id_doctor);
            getNombreApellidoDoctor(id_doctor);
        }

        id_tipo = getIntent().getIntExtra("id_tipo",0);
        datos = obtenerHistorico(id_lesion);
        btn_msj.setVisibility(View.GONE);
        btn_msj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(HistoryActivity.this.getApplicationContext(), R.anim.alpha);
                btn_msj.startAnimation(animation);

                Intent resultIntent = new Intent(contexto, MessageActivity.class);
                resultIntent.putExtra("id_lesion", id_lesion);  // put data that you want returned to activity A
                resultIntent.putExtra("id_doctor", id_doctor);  // put data that you want returned to activity A
                resultIntent.putExtra("id_paciente", id_paciente);  // put data that you want returned to activity A
                resultIntent.putExtra("nombre_doctor", nombre_doctor);  // put data that you want returned to activity A
                startActivityForResult(resultIntent,RESULT_ACTIVITY_MESSAGE);
            }
        });
        btn_add_historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddLesionActivity();
            }
        });
        btn_recomendaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecomendacionActivity();
            }
        });

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(HistoryActivity.this, FindDoctorActivity.class);
                resultIntent.putExtra("id_lesion", id_lesion);  // put data that you want returned to activity A
                resultIntent.putExtra("id_paciente", id_paciente);  // put data that you want returned to activity A
                startActivityForResult(resultIntent, RESULT_ACTIVITY_GPS);
            }
        });
    }

    private void getNombreApellidoDoctor(Integer id_doctor) {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

        Call<ObtenerUsuarioResponse> call= service.getUserById("/usuarios/"+id_doctor);
        call.enqueue(new Callback<ObtenerUsuarioResponse>() {
            @Override
            public void onResponse(Call<ObtenerUsuarioResponse> call, Response<ObtenerUsuarioResponse> response) {
                nombre_doctor = response.body().getNombre() +" "+ response.body().getApellido();

            }
            @Override
            public void onFailure(Call<ObtenerUsuarioResponse> call, Throwable t) {
                Toast.makeText(contexto, "Error al obtener datos del doctor, el servicio no se encuentra disponible. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerRecomendaciones() {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

        Call<ArrayList<TratamientoResponse>> call = service.getRecomendacionesById("/lesion_tratamientos/" + id_lesion);
        call.enqueue(new Callback<ArrayList<TratamientoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<TratamientoResponse>> call, Response<ArrayList<TratamientoResponse>> response) {
                if (response.body().size() > 0)
                    btn_recomendaciones.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<ArrayList<TratamientoResponse>> call, Throwable t) {
            }

        });
    }

    private void openRecomendacionActivity() {
        Intent resultIntent = new Intent(this, TratamientoActivity.class);
        resultIntent.putExtra("id_lesion", id_lesion);  // put data that you want returned to activity A
        startActivityForResult(resultIntent,RESULT_ACTIVITY_RECOMENDACION);
    }


    private ArrayList<HistoricoResponse> obtenerHistorico(Integer id_lesion) {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

        Call<ArrayList<HistoricoResponse>> call= service.getHistoricoByLesionId("/historial/lesion/"+id_lesion);
        call.enqueue(new Callback<ArrayList<HistoricoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<HistoricoResponse>> call, Response<ArrayList<HistoricoResponse>> response) {
                datos = response.body();
                if(datos != null)
                    lista.setAdapter(new AdaptadorHistorico(contexto,datos));
            }
            @Override
            public void onFailure(Call<ArrayList<HistoricoResponse>> call, Throwable t) {
                Toast.makeText(contexto, "Error al obtener lesiones, el servicio no se encuentra disponible. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return datos;
    }

    private void habilitarMensajeria(Integer id_doctor) {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

        Call<NotificacionHabilitadaResponse> call= service.getNotificacionHabilitada("/notificacion_habilitada/"+id_doctor);
        call.enqueue(new Callback<NotificacionHabilitadaResponse>() {
            @Override
            public void onResponse(Call<NotificacionHabilitadaResponse> call, Response<NotificacionHabilitadaResponse> response) {
                NotificacionHabilitadaResponse notif = response.body();
                if(notif != null) {
                    if (response.body().getRecibir_notificaciones())
                        btn_msj.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<NotificacionHabilitadaResponse> call, Throwable t) {
                Toast.makeText(contexto, "Error al obtener lesiones, el servicio no se encuentra disponible. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAddLesionActivity(){
        Intent resultIntent = new Intent(this, AddLesionActivity.class);
        resultIntent.putExtra("id_lesion", id_lesion);  // put data that you want returned to activity A
        resultIntent.putExtra("id_doctor", id_doctor);  // put data that you want returned to activity A
        startActivityForResult(resultIntent,RESULT_ACTIVITY_LESION);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //ACTIVITY RESULT TAKE PICTURE
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTIVITY_LESION) {
            finish();
        }

    }

}