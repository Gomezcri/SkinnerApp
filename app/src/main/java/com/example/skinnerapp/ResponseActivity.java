package com.example.skinnerapp;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
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
    private Integer id_historico;
    private String mensaje = null;
    private EditText input_palmas;
    private Button btn_update_lesion;
    private Button btn_map;
    public final static int RESULT_ACTIVITY_GPS = 199;
    private Button btn_ppal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_response);
        int codigo = getIntent().getIntExtra("estado", 404);
        mensaje = getIntent().getStringExtra("respuestaServidor");
        id_tipo = getIntent().getIntExtra("id_tipo", 0);
        id_paciente = getIntent().getIntExtra("id_paciente", 0);
        id_lesion = getIntent().getIntExtra("id_lesion", 0);
        id_historico = getIntent().getIntExtra("id_historial", 0);
        btn_ppal = (Button) findViewById(R.id.btn_ppal);

        imagenDeResultado = (ImageView) findViewById(R.id.imagenResultado);
        textoResultado = (TextView) findViewById(R.id.textoResultado);
        input_palmas = (EditText) findViewById(R.id.input_palmas);
        btn_update_lesion = (Button) findViewById(R.id.btn_update_lesion);
        btn_map = (Button) findViewById(R.id.btn_map);

        btn_update_lesion.setVisibility(View.GONE);
        input_palmas.setVisibility(View.GONE);
        btn_map.setVisibility(View.GONE);
        btn_ppal.setVisibility(View.GONE);
        if (codigo == 200 && id_lesion !=0) {
            imagenDeResultado.setImageResource(R.drawable.checkverde);

            switch (id_tipo) {
                case 1://melanoma
                    LunarMelanomaController(getString(R.string.mensaje_melanoma));
                    btn_map.setVisibility(View.VISIBLE);
                    break;
                case 2://vitiligo
                    textoResultado.setText(getString(R.string.mensaje_vitiligo));
                    btn_ppal.setVisibility(View.VISIBLE);
                    btn_map.setVisibility(View.VISIBLE);
                    break;
                case 3://psoriasis
                    PsoriasisControler();
                    break;
                case 4://lunar
                    LunarMelanomaController(getString(R.string.mensaje_lunar));
                    btn_map.setVisibility(View.VISIBLE);
                    break;
                case 5://nada
                    textoResultado.setText(getString(R.string.mensaje_sin_enfermedad));
                    btn_ppal.setVisibility(View.VISIBLE);
                    break;
            }

            btn_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent resultIntent = new Intent(ResponseActivity.this, FindDoctorActivity.class);
                    resultIntent.putExtra("id_lesion", id_lesion);  // put data that you want returned to activity A
                    resultIntent.putExtra("id_paciente", id_paciente);  // put data that you want returned to activity A
                    startActivityForResult(resultIntent, RESULT_ACTIVITY_GPS);
                }
            });
        }
        else
            if (codigo == 200 && id_lesion ==0)
            {
                btn_ppal.setVisibility(View.VISIBLE);
                imagenDeResultado.setImageResource(R.drawable.checkverde);
                textoResultado.setText(getString(R.string.mensaje_nuevo_historico));
            }

        if (codigo == 404) {
        imagenDeResultado.setImageResource(R.drawable.signopregunta);
        textoResultado.setText("No hay conexion con el servidor, porfavor intenta en unos instantes");
        }

        btn_ppal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });
    }

    private void PsoriasisControler() {
        btn_update_lesion.setVisibility(View.VISIBLE);
        input_palmas.setVisibility(View.VISIBLE);

        textoResultado.setText("Para determinar gravedad, ingrese la cantidad de palmas de la distrubución de su lesión.");

        btn_update_lesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_palmas.getText() != null) {
                    if (Integer.parseInt(input_palmas.getText().toString()) >= 10)
                        textoResultado.setText(getString(R.string.mensaje_psoriasis_grave));
                    else
                        textoResultado.setText(getString(R.string.mensaje_psoriasis_leve));
                    Retrofit retrofit = getConnection();
                    JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
                    btn_ppal.setVisibility(View.VISIBLE);
                    UpdateLesionRequest req = new UpdateLesionRequest("{palmas:"+Integer.parseInt(input_palmas.getText().toString())+"}");
                    Call<RegistrarLesionResponse> call = service.putLesion("/historial/" + id_historico, req);
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

    private void LunarMelanomaController(final String mensaje) {
        btn_update_lesion.setVisibility(View.VISIBLE);
        input_palmas.setVisibility(View.VISIBLE);
        input_palmas.setHint("Ingrese tamaño estimado");
        textoResultado.setText("Por favor, ingresar el tamaño estimado de su lesión en centímetros.");

        btn_update_lesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_palmas.getText() != null) {
                    textoResultado.setText(mensaje);
                    Retrofit retrofit = getConnection();
                    JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
                    btn_ppal.setVisibility(View.VISIBLE);
                    UpdateLesionRequest req = new UpdateLesionRequest("{diametro:"+Integer.parseInt(input_palmas.getText().toString())+"}");
                    Call<RegistrarLesionResponse> call = service.putLesion("/historial/" + id_historico, req);
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
        if (requestCode == RESULT_ACTIVITY_GPS) {
           goToMainMenu();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Aviso")
                .setMessage("¿Desea volver al menú principal?")
                .setNegativeButton("NO", null)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            goToMainMenu();
                    }
                }).create().show();
    }

    private void goToMainMenu() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}