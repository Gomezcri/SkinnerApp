package com.example.skinnerapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.RegistrarHistoricoRequest;
import com.example.skinnerapp.Model.RegistrarHistoricoResponse;
import com.example.skinnerapp.Model.RegistrarLesionResponse;
import com.example.skinnerapp.Model.UpdateLesionRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.dismissLoadingDialog;
import static util.Util.getConnection;
import static util.Util.showLoadingDialog;

public class ResponseActivity extends AppCompatActivity {

    private ImageView imagenDeResultado;
    private TextView textoResultado;
    private Integer id_tipo;
    private Integer id_paciente;
    private Integer id_lesion;
    private String mensaje = null;
    private EditText input_palmas;
    private Button btn_update_lesion;
    private Button btn_map;
    public final static int RESULT_ACTIVITY_GPS = 133;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_response);
        int codigo = getIntent().getIntExtra("estado",404);
        mensaje = getIntent().getStringExtra("respuestaServidor");
        id_tipo  = getIntent().getIntExtra("id_tipo",0);
        id_paciente = getIntent().getIntExtra("id_paciente",0);
        id_lesion = getIntent().getIntExtra("id_lesion",0);

        imagenDeResultado = (ImageView) findViewById(R.id.imagenResultado);
        textoResultado= (TextView) findViewById(R.id.textoResultado);
        input_palmas = (EditText) findViewById(R.id.input_palmas);
        btn_update_lesion = (Button) findViewById(R.id.btn_update_lesion);

        btn_map = (Button) findViewById(R.id.btn_map);

        btn_update_lesion.setVisibility(View.GONE);
        input_palmas.setVisibility(View.GONE);
        btn_map.setVisibility(View.GONE);
        if(codigo==200){
            imagenDeResultado.setImageResource(R.drawable.checkverde);

            switch (id_tipo){
                case 1://melanoma
                    textoResultado.setText(getString(R.string.mensaje_melanoma));
                    btn_map.setVisibility(View.VISIBLE);
                    break;
                case 2://vitiligo
                    textoResultado.setText(getString(R.string.mensaje_vitiligo));
                    btn_map.setVisibility(View.VISIBLE);
                    break;
                case 3://psoriasis
                    PsoriasisControler();
                    break;
                case 4://lunar
                    textoResultado.setText(getString(R.string.mensaje_lunar));
                    btn_map.setVisibility(View.VISIBLE);
                    break;
            }

            btn_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent resultIntent = new Intent(ResponseActivity.this, FindDoctorActivity.class);
                    resultIntent.putExtra("id_lesion", id_lesion);  // put data that you want returned to activity A
                    resultIntent.putExtra("id_paciente", id_paciente);  // put data that you want returned to activity A
                    startActivityForResult(resultIntent,RESULT_ACTIVITY_GPS);
                }
            });
        }

        if(codigo==404){
            imagenDeResultado.setImageResource(R.drawable.signopregunta);
            textoResultado.setText("No hay conexion con el servidor, porfavor intenta en unos instantes");
        }
    }

    private void PsoriasisControler() {
        btn_update_lesion.setVisibility(View.VISIBLE);
        input_palmas.setVisibility(View.VISIBLE);
        textoResultado.setText("Para determinar gravedad, ingrese la cantidad de palmas de la distrubución de su lesión.");

        btn_update_lesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_palmas.getText() != null)
                {
                    if(Integer.parseInt(input_palmas.getText().toString()) >= 10)
                        textoResultado.setText(getString(R.string.mensaje_psoriasis_grave));
                    else
                        textoResultado.setText(getString(R.string.mensaje_psoriasis_leve));
                    Retrofit retrofit = getConnection();
                    JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

                    UpdateLesionRequest req = new UpdateLesionRequest(Integer.parseInt(input_palmas.getText().toString()));
                    Call<RegistrarLesionResponse> call= service.putLesion("/lesion/"+id_lesion,req);
                    call.enqueue(new Callback<RegistrarLesionResponse>() {
                        @Override
                        public void onResponse(Call<RegistrarLesionResponse> call, Response<RegistrarLesionResponse> response) {
                            btn_update_lesion.setVisibility(View.GONE);
                            btn_map.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onFailure(Call<RegistrarLesionResponse> call, Throwable t) {

                        }

                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //ACTIVITY RESULT GPS
        super.onActivityResult(requestCode, resultCode, data);

    }
}