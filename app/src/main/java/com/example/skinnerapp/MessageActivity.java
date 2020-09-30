package com.example.skinnerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.MensajeResponse;
import com.example.skinnerapp.Model.SendMessageRequest;
import com.example.skinnerapp.Model.SendMessageResponse;
import com.example.skinnerapp.ui.message.AdapterMensajes;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.getConnection;

public class MessageActivity extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;
    private AdapterMensajes adapter;
    private ArrayList<MensajeResponse> datos;
    private Integer id_lesion;
    private Integer id_paciente;
    private Context contexto;
    private Integer id_doctor;
    private String nombre_doctor;

    private static final int PHOTO_SEND = 1;
    private static final int PHOTO_PERFIL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        contexto = this;
        //fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        nombre = (TextView) findViewById(R.id.nombre_medico);

        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        //todo agregar los ids dinamicos desde grilla de lesiones y el id del paciente logueado.
        id_lesion = getIntent().getIntExtra("id_lesion",0);
        id_paciente = getIntent().getIntExtra("id_paciente",0);
        id_doctor = getIntent().getIntExtra("id_doctor",0);
        nombre.setText(getIntent().getStringExtra("nombre_doctor"));
        adapter = new AdapterMensajes(this);

        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(txtMensaje.getText().toString());
                txtMensaje.setText("");
            }
        });

        datos = obtenerMensajes();

    }

    private void sendMessage(String mensaje) {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        SendMessageRequest req = new SendMessageRequest(id_paciente,id_doctor,mensaje,id_lesion);
        Call<SendMessageResponse> call= service.postSendMessage(req);
        call.enqueue(new Callback<SendMessageResponse>() {
            @Override
            public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                datos = obtenerMensajes();
            }
            @Override
            public void onFailure(Call<SendMessageResponse> call, Throwable t) {
                Toast.makeText(contexto, "Error al obtener lesiones, el servicio no se encuentra disponible. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<MensajeResponse> obtenerMensajes() {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

        Call<ArrayList<MensajeResponse>> call= service.getMensajesByLesionId("/mensajes/"+id_lesion);
        call.enqueue(new Callback<ArrayList<MensajeResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MensajeResponse>> call, Response<ArrayList<MensajeResponse>> response) {
                datos = response.body();
                adapter.addMensaje(datos);
            }
            @Override
            public void onFailure(Call<ArrayList<MensajeResponse>> call, Throwable t) {
                Toast.makeText(contexto, "Error al obtener los mensajes para la lesion. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return datos;
    }

    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}