package com.example.skinnerapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.AsignacionRequest;
import com.example.skinnerapp.Model.AsignacionResponse;
import com.example.skinnerapp.Model.LesionesResponse;
import com.example.skinnerapp.Model.RegistrarLesionRequest;
import com.example.skinnerapp.Model.RegistrarHistoricoRequest;
import com.example.skinnerapp.Model.RegistrarHistoricoResponse;
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

import androidx.appcompat.app.AppCompatActivity;

public class AddLesionActivity extends AppCompatActivity {

    private ImageView takePictureButton;
    private ImageView imageView;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int OPEN_GALERY = 10;
    private String currentPhotoPath;
    private ImageView btnAnalizar;
    private TextView txtMsj2;
    private TextView textView;
    private File f;
    private String bodyPart;
    private String section;
    private String encodedImage;
    private Button btngaleria;
    private Integer id_lesion;
    private Integer id_doctor;
    private EditText text_descripcion;
    private Integer id_tipo;
    private Integer id_paciente;
    private Context contexto;
    private TextView tv_presioneCamara;
    private static ProgressDialog progress;
    public static Activity activity = null;
    public ProgressDialog progressDialog_new;
    public ProgressDialog progressDialog_history;


    public final static int REQUEST_ACTIVITY_BODY = 100;
    public final static int RESULT_ACTIVITY_BODY = 101;
    public final static int RESULT_ACTIVITY_RESPONSE = 104;

    private Integer id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesion);
        progressDialog_new = new ProgressDialog(this);
        progressDialog_history = new ProgressDialog(this);
        contexto = this;
        takePictureButton = (ImageView) findViewById(R.id.button_image);
        imageView = (ImageView) findViewById(R.id.imageview);
        //textView = (TextView) findViewById(R.id.text_call);
        text_descripcion = (EditText)findViewById(R.id.text_from);
        btngaleria =(Button)findViewById(R.id.btngaleria);
        btnAnalizar = (ImageView) findViewById(R.id.button_analizar);
        txtMsj2 = (TextView) findViewById(R.id.textView9);
        tv_presioneCamara = (TextView) findViewById(R.id.textView11);
        id_lesion = getIntent().getIntExtra("id_lesion",0);

        id_doctor = getIntent().getIntExtra("id_doctor",0);
        id_tipo = getIntent().getIntExtra("id_tipo",0);
        id_user = getIntent().getIntExtra("id_user",0);
        id_paciente = getIntent().getIntExtra("id_paciente",0);
        if(!id_lesion.equals(0))
            txtMsj2.setText("Presione el paciente para agregar la foto a su historial");
        tv_presioneCamara.setPaintFlags(tv_presioneCamara.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtMsj2.setPaintFlags(txtMsj2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnAnalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(AddLesionActivity.this.getApplicationContext(), R.anim.alpha);
                btnAnalizar.startAnimation(animation);
                if(id_lesion == 0)
                    addNewLesion();
                else
                    addHistory();
            }
        });
        btnAnalizar.setVisibility(View.GONE);
        txtMsj2.setVisibility(View.GONE);

        final Intent activityBody = new Intent(this, BodyActivity.class);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_lesion == 0)
                {
                    Animation animation = AnimationUtils.loadAnimation(AddLesionActivity.this.getApplicationContext(), R.anim.alpha);
                    takePictureButton.startAnimation(animation);
                    startActivityForResult(activityBody, REQUEST_ACTIVITY_BODY);
                }
                else
                    askCameraPermissions();

                //askCameraPermissions();
            }
        });

        btngaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  textView.setText("");
                openGalery();
            }
        });

        btngaleria.setVisibility(View.GONE);
    }

    private void addHistory() {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        Date currentTime = Calendar.getInstance().getTime();
        String descripcion = null;
        //showLoadingDialog(contexto,"Analizando","Skinner está agregando su nueva imagen.");
        mostrarCartel_history();
        if(text_descripcion.getText()!= null)
            descripcion = text_descripcion.getText().toString();
        RegistrarHistoricoRequest req = new RegistrarHistoricoRequest(id_lesion,id_doctor, text_descripcion.getText().toString(),encodedImage,currentTime.toString(),id_tipo);
        Log.d("ID_TIPO_ENADDLESION",id_tipo.toString());
        Call<RegistrarHistoricoResponse> call= service.postRegistrarHistorico(req);
        call.enqueue(new Callback<RegistrarHistoricoResponse>() {
            @Override
            public void onResponse(Call<RegistrarHistoricoResponse> call, Response<RegistrarHistoricoResponse> response) {

                //dismissLoadingDialog();
                cerrarCartel_history();
                Intent resultIntent = new Intent(AddLesionActivity.this, ResponseActivity.class);
                resultIntent.putExtra("id_tipo", response.body().getId_tipo());
                resultIntent.putExtra("id_lesion", response.body().getId_lesion());
                resultIntent.putExtra("id_historial", response.body().getId());
                resultIntent.putExtra("respuestaServidor", response.body().getId().toString());  // put data that you want returned to activity A
                resultIntent.putExtra("estado", response.code());  // put data that you want returned to activity A
                resultIntent.putExtra("pantalla_historial", 1);

                startActivityForResult(resultIntent,RESULT_ACTIVITY_RESPONSE);
                if(id_doctor != 0)
                    addAsignacion();
            }


            @Override
            public void onFailure(Call<RegistrarHistoricoResponse> call, Throwable t) {
              //  textView.setText(t.getMessage());
                //dismissLoadingDialog();
                cerrarCartel_history();
                Intent resultIntent = new Intent(AddLesionActivity.this,ResponseActivity.class);
                resultIntent.putExtra("respuestaServidor", t.getMessage());  // put data that you want returned to activity A
                resultIntent.putExtra("estado", 404);  // put data that you want returned to activity A
                startActivityForResult(resultIntent,RESULT_ACTIVITY_RESPONSE);
            }

        });
    }

    private void addAsignacion() {
            Retrofit retrofit = getConnection();
            JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

            AsignacionRequest req = new AsignacionRequest(id_lesion,id_doctor,id_paciente,null,"notificacion");
            Call<AsignacionResponse> call= service.postRegistrarAsignacion(req);

            call.enqueue(new Callback<AsignacionResponse>() {
                @Override
                public void onResponse(Call<AsignacionResponse> call, Response<AsignacionResponse> response) {
                   // Toast.makeText(FindDoctorActivity.this, "Su solicitud ha sido enviada, volviendo al menu principal.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                @Override
                public void onFailure(Call<AsignacionResponse> call, Throwable t) {
                }

            });
        }


    private void openGalery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , OPEN_GALERY);
    }

    private void addNewLesion() {
        Retrofit retrofit = getConnection();
        String descripcion = null;
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        //showLoadingDialog(contexto,"Analizando","Skinner está analizando su imagen, aguarde un momento.");
        mostrarCartel_new();
        if(text_descripcion.getText()!= null)
            descripcion = text_descripcion.getText().toString();
        RegistrarLesionRequest req = new RegistrarLesionRequest(encodedImage,bodyPart,section,id_user,descripcion,Calendar.getInstance().getTime().toString());
        Call<LesionesResponse> call= service.getAnalisisImagen(req);
        call.enqueue(new Callback<LesionesResponse>() {
            @Override
            public void onResponse(Call<LesionesResponse> call, Response<LesionesResponse> response) {
                //dismissLoadingDialog();
                cerrarCartel_new();
                Intent resultIntent = new Intent(AddLesionActivity.this, ResponseActivity.class);
                resultIntent.putExtra("id_tipo", response.body().getId_tipo());  // put data that you want returned to activity A
                resultIntent.putExtra("id_paciente", response.body().getId_paciente());  // put data that you want returned to activity A
                resultIntent.putExtra("id_lesion", response.body().getId());  // put data that you want returned to activity A
                resultIntent.putExtra("id_historial", response.body().getId_historial());  // put data that you want returned to activity A
                resultIntent.putExtra("estado", response.code());  // put data that you want returned to activity A
                startActivityForResult(resultIntent,RESULT_ACTIVITY_RESPONSE);
                //Toast.makeText(MainActivity.this, "Se envío correctamente la petición. Falta retorno del servidor."+ response.toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<LesionesResponse> call, Throwable t) {
                //textView.setText(t.getMessage());
                //dismissLoadingDialog();
                cerrarCartel_new();
                Intent resultIntent = new Intent(AddLesionActivity.this,ResponseActivity.class);
                resultIntent.putExtra("respuestaServidor", t.getMessage());  // put data that you want returned to activity A
                resultIntent.putExtra("estado", 404);  // put data that you want returned to activity A
                startActivityForResult(resultIntent,RESULT_ACTIVITY_RESPONSE);
            }

        });
    }

    private String convertImgString(){

        int size = (int) f.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(f));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Base64.encodeToString(bytes,Base64.NO_WRAP);
    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(this,"Se requieren permisos para utilizar la cámara",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.skinnerapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                String[] animals = {"1- Limpie la lente","2- Use luz natural siempre que sea posible","3- Evite contraluces","4- Evite usar el flash"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder/*.setMessage(R.string.recomendacion_foto_mensaje)*/
                        .setItems(animals,null)
                        .setTitle(R.string.recomendacion_foto_titulo)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //ACTIVITY RESULT TAKE PICTURE

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                Toast.makeText(this, "Recorte la imagen en donde se encuentra su lesión.", Toast.LENGTH_SHORT).show();
                CropImage.activity(contentUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .start(this);

            }
        }
        //ACTIVITY RESULT OPEN OTHERS ACTIVITIES
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //  if (resultCode == RESULT_OK) {
            Uri contentUri = result.getUri();
            f = new File(contentUri.getPath());
            //imageView.setImageURI(contentUri);
            imageView.setImageURI(Uri.fromFile(f));
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            btnAnalizar.setVisibility(View.VISIBLE);
            txtMsj2.setVisibility(View.VISIBLE);
            encodedImage = convertImgString();

           /* } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }*/
        }
        switch (requestCode) {
            case REQUEST_ACTIVITY_BODY:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle.getString("bodyPart") != null) {
                        bodyPart = bundle.getString("bodyPart");
                        section = bundle.getString("section");
                        askCameraPermissions();
                       // String[] animals = {"1- Limpie la lente","2- Use luz natural siempre que sea posible","3- Evite contraluces","4- Evite usar el flash"};
                        //AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        //builder/*.setMessage(R.string.recomendacion_foto_mensaje)*/
                          //      .setItems(animals,null)
                            //    .setTitle(R.string.recomendacion_foto_titulo)
                              //  .setCancelable(false)
                               // .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 //   public void onClick(DialogInterface dialog, int id) {

                                   // }
                                //});
                        //AlertDialog alert = builder.create();
                        //alert.show();


                    }
                }
                break;
            case OPEN_GALERY:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    imageView.setImageURI(selectedImage);
                    if (selectedImage != null) {
                        Bitmap compressedImgBitmap = null;
                        try {
                            compressedImgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        compressedImgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        imageView.setImageBitmap(compressedImgBitmap);
                        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
                        encodedImage = Base64.encodeToString(byteArrayImage, Base64.NO_WRAP);
                        btnAnalizar.setVisibility(View.VISIBLE);
                    }
                }
            case RESULT_ACTIVITY_RESPONSE:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalCacheDir();
        // File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void mostrarCartel_history() {
        progressDialog_history.show();
        progressDialog_history.setContentView(R.layout.progress_dialog_history);
        progressDialog_history.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void cerrarCartel_history() {
        progressDialog_history.dismiss();
    }

    private void mostrarCartel_new() {
        progressDialog_new.show();
        progressDialog_new.setContentView(R.layout.progress_dialog_new);
        progressDialog_new.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void cerrarCartel_new() {
        progressDialog_new.dismiss();
    }


}

