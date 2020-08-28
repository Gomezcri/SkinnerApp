package com.example.skinnerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.LesionesResponse;
import com.example.skinnerapp.Model.LoginUsuarioRequest;
import com.example.skinnerapp.Model.LoginUsuarioResponse;
import com.example.skinnerapp.Model.RegistrarHistoricoRequest;
import com.example.skinnerapp.Model.RegistrarHistoricoResponse;
import com.example.skinnerapp.Model.RegistrarLesionRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.dismissLoadingDialog;
import static util.Util.getConnection;
import static util.Util.obtenerUserIdApp;
import static util.Util.showLoadingDialog;

public class LOGINARDO extends AppCompatActivity {

    private TextView us;
    private TextView pass;
    private Button ingresar;
    private String textoUsuario;
    private String textoContrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesion);

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

                    Intent resultIntent = new Intent(LOGINARDO.this, MainActivity2.class);
                    if(response.isSuccessful()){
                        startActivity(resultIntent);
                    }

                    //Toast.makeText(MainActivity.this, "Se envío correctamente la petición. Falta retorno del servidor."+ response.toString(), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<LoginUsuarioResponse> call, Throwable t) {
                    Toast.makeText(LOGINARDO.this, "No se pudo conectar con el servidor, intenta mas tarde cuerno", Toast.LENGTH_SHORT).show();
                }

            });

        }



    }

}