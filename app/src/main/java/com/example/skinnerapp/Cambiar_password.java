package com.example.skinnerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.ActualizarContraseñaRequest;
import com.example.skinnerapp.Model.ActualizarContraseñaResponse;
import com.example.skinnerapp.Model.RegistrarUsuarioRequest;
import com.example.skinnerapp.Model.RegistrarUsuarioResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.getConnection;

public class Cambiar_password extends AppCompatActivity {

    private Integer id_user;
    private Button btn_cambiar_psw;
    private EditText password1;
    private EditText password2;
    private String strusername;
    private String struseremail;
    private TextView tv_gracias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_password);
        id_user = getIntent().getIntExtra("id_usuario",0);
        strusername = getIntent().getStringExtra("username");
        struseremail = getIntent().getStringExtra("useremail");

        btn_cambiar_psw  = (Button) findViewById(R.id.btn_cambiar_psw);
        password1 = (EditText) findViewById(R.id.new_password1);
        password2 = (EditText) findViewById(R.id.new_password2);
        tv_gracias = (TextView) findViewById(R.id.tv_final2);
        tv_gracias.setPaintFlags(tv_gracias.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btn_cambiar_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarContraseña();
            }
        });
    }

    private void cambiarContraseña() {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        if(password2.getText().toString().equals(password1.getText().toString())) {

            ActualizarContraseñaRequest req = new ActualizarContraseñaRequest(password1.getText().toString());
            Call<ActualizarContraseñaResponse> call = service.putUpdatePassword("/actualizar_password/"+id_user,req);
            call.enqueue(new Callback<ActualizarContraseñaResponse>() {
                @Override
                public void onResponse(Call<ActualizarContraseñaResponse> call, Response<ActualizarContraseñaResponse> response) {

                    Toast.makeText(Cambiar_password.this,R.string.actualizar_password_ok, Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("actualizado", true);
                    resultIntent.putExtra("id_usuario", id_user);  // put data that you want returned to activity A
                    resultIntent.putExtra("username", strusername);  // put data that you want returned to activity A
                    resultIntent.putExtra("useremail", struseremail);  // put data that you want returned to activity A

                    setResult(RESULT_OK, resultIntent);
                    finish();
                }

                @Override
                public void onFailure(Call<ActualizarContraseñaResponse> call, Throwable t) {
                    Toast.makeText(Cambiar_password.this, R.string.registroUsuario_error, Toast.LENGTH_SHORT).show();
                }

            });
        }
        else
            Toast.makeText(Cambiar_password.this, R.string.actualizar_password_distinto, Toast.LENGTH_SHORT).show();
    }
}