package com.example.skinnerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.LoginUsuarioRequest;
import com.example.skinnerapp.Model.LoginUsuarioResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.getConnection;

public class LoginActivity extends AppCompatActivity {

    private TextView us;
    private TextView pass;
    private Button ingresar;
    private String textoUsuario;
    private String textoContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        us = (TextView) findViewById(R.id.tv_us);
        pass = (TextView) findViewById(R.id.tv_pass);
        ingresar = (Button) findViewById(R.id.bt_ingresar);

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoguearUsuario();
            }
        });

    }

    private void LoguearUsuario() {
        Retrofit retrofit = getConnection();
        final String[] textoRespuesta = {""};
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        if(us.getText()!= null && pass.getText() != null)
        {
            textoUsuario = us.getText().toString();
            textoContrasenia = pass.getText().toString();
            LoginUsuarioRequest req = new LoginUsuarioRequest(textoUsuario,textoContrasenia);
            Call<LoginUsuarioResponse> call= service.getUsuarioLogin(req);
            call.enqueue(new Callback<LoginUsuarioResponse>() {
                @Override
                public void onResponse(Call<LoginUsuarioResponse> call, Response<LoginUsuarioResponse> response) {

                    Intent resultIntent = new Intent(LoginActivity.this, MainActivity2.class);
                    if(response.body().getId()!=null){
                        startActivity(resultIntent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta, intente de nuevo.", Toast.LENGTH_SHORT).show();
                    }

                    //Toast.makeText(MainActivity.this, "Se envío correctamente la petición. Falta retorno del servidor."+ response.toString(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<LoginUsuarioResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "No se pudo conectar con el servidor, intenta mas tarde cuerno", Toast.LENGTH_SHORT).show();
                }

            });

        }



    }

}