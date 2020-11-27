package com.example.skinnerapp;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.LoginUsuarioRequest;
import com.example.skinnerapp.Model.LoginUsuarioResponse;
import com.example.skinnerapp.Model.RecuperarContraseñaRequest;
import com.example.skinnerapp.Model.RecuperarcontraseñaResponse;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.getConnection;

public class Recuperar_contraseña extends AppCompatActivity {

    private TextView tv_email;
    private Button recuperarContraseña;
    private String texto;
    private TextView tv_gracias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recuperar_contrasenia);
        tv_email = (TextView) findViewById(R.id.tv_email_recuperarContraseña);
        recuperarContraseña = (Button) findViewById(R.id.bt_recuperarContraseña);
        tv_gracias = (TextView) findViewById(R.id.tv_final);
        tv_gracias.setPaintFlags(tv_gracias.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        recuperarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecuperarPassword();
            }
        });

    }

    private void RecuperarPassword() {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        if(tv_email.getText()!=null){
            texto=tv_email.getText().toString();
            RecuperarContraseñaRequest req = new RecuperarContraseñaRequest(texto);
            Call<RecuperarcontraseñaResponse> call= service.postRecuperarContraseña(req);
            call.enqueue(new Callback<RecuperarcontraseñaResponse>() {

                @Override
                public void onResponse(Call<RecuperarcontraseñaResponse> call, Response<RecuperarcontraseñaResponse> response) {
                   // Intent resultIntent = new Intent(Recuperar_contraseña.this, LoginActivity.class);
                    Toast.makeText(Recuperar_contraseña.this, "Revise la casilla de mail ingresada y realice un nuevo ingreso", Toast.LENGTH_LONG).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("change", true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                   // startActivity(resultIntent);
                }

                @Override
                public void onFailure(Call<RecuperarcontraseñaResponse> call, Throwable t) {
                    Toast.makeText(Recuperar_contraseña.this, "No se pudo recuperar su contraseña, intente más tarde por favor", Toast.LENGTH_SHORT).show();
                }

            });
        }
    }
}
