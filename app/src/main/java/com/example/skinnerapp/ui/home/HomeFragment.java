package com.example.skinnerapp.ui.home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.skinnerapp.BodyActivity;
import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.AnalizarImagenRequest;
import com.example.skinnerapp.Model.AnalizarImagenResponse;
import com.example.skinnerapp.R;
import com.example.skinnerapp.ResponseActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ImageView takePictureButton;
    private ImageView imageView;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int OPEN_GALERY = 10;
    private String currentPhotoPath;
    private ImageView btnAnalizar;
    private TextView textView;
    private File f;
    private String bodyPart;
    private String section;
    private String encodedImage;
    private ProgressDialog progress;
    private Button btngaleria;
    private View root;

    public final static int REQUEST_ACTIVITY_BODY = 100;
    public final static int RESULT_ACTIVITY_BODY = 101;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        takePictureButton = (ImageView) root.findViewById(R.id.button_image);
        imageView = (ImageView) root.findViewById(R.id.imageview);
        textView = (TextView) root.findViewById(R.id.text_call);

        btngaleria =(Button)root.findViewById(R.id.btngaleria);
        btnAnalizar = (ImageView) root.findViewById(R.id.button_analizar);

        btnAnalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.alpha);
                btnAnalizar.startAnimation(animation);
                callService();
            }
        });
        btnAnalizar.setVisibility(View.GONE);

        final Intent activityBody = new Intent(getActivity(), BodyActivity.class);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.alpha);
                takePictureButton.startAnimation(animation);
                startActivityForResult(activityBody, REQUEST_ACTIVITY_BODY);
                //askCameraPermissions();
            }
        });

        btngaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                openGalery();
            }
        });

        return root;
    }

    private void openGalery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , OPEN_GALERY);
    }

    private void callService() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.4:8080/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final String[] textoRespuesta = {""};
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        showLoadingDialog();
        AnalizarImagenRequest req = new AnalizarImagenRequest(encodedImage,bodyPart,section,"skinner@gmail.com");
        Call<AnalizarImagenResponse> call= service.savePost(req);
        call.enqueue(new Callback<AnalizarImagenResponse>() {
            @Override
            public void onResponse(Call<AnalizarImagenResponse> call, Response<AnalizarImagenResponse> response) {

                textView.setText(response.body().getMessage());
                dismissLoadingDialog();
                textoRespuesta[0] =response.body().getMessage();
                Intent resultIntent = new Intent(getActivity(), ResponseActivity.class);
                resultIntent.putExtra("respuestaServidor", textoRespuesta[0]);  // put data that you want returned to activity A
                resultIntent.putExtra("estado", response.code());  // put data that you want returned to activity A
                startActivityForResult(resultIntent,RESULT_ACTIVITY_BODY);
                //Toast.makeText(MainActivity.this, "Se envío correctamente la petición. Falta retorno del servidor."+ response.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AnalizarImagenResponse> call, Throwable t) {
                textView.setText(t.getMessage());
                dismissLoadingDialog();
                Intent resultIntent = new Intent(getActivity(),ResponseActivity.class);
                resultIntent.putExtra("respuestaServidor", t.getMessage());  // put data that you want returned to activity A
                resultIntent.putExtra("estado", 404);  // put data that you want returned to activity A
                startActivityForResult(resultIntent,RESULT_ACTIVITY_BODY);
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

        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }else{
            dispatchTakePictureIntent();
            textView.setText("");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(getActivity(),"Se requiere permisos para utilizar la cámara",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.skinnerapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //ACTIVITY RESULT TAKE PICTURE
        if(requestCode == REQUEST_TAKE_PHOTO){
            if(resultCode == RESULT_OK){
               f =new File(currentPhotoPath);
               // imageView.setImageURI(Uri.fromFile(f));
                //Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
              //  mediaScanIntent.setData(contentUri);
               // getActivity().sendBroadcast(mediaScanIntent);
                Toast.makeText(getActivity(),"Recortar la imagen en donde se encuentra su lesión.",Toast.LENGTH_SHORT).show();
                CropImage.activity(contentUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .start(getContext(), this);

                //btnAnalizar.setVisibility(View.VISIBLE);

                //encodedImage = convertImgString();

/*
               Bitmap compressedImgBitmap = null;
               try {
                   compressedImgBitmap = new Compressor(this).compressToBitmap(f);
               } catch (IOException e) {
                   e.printStackTrace();
               }

               ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
               compressedImgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
               imageView.setImageBitmap(compressedImgBitmap);
               byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
               encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);*/
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
                getActivity().sendBroadcast(mediaScanIntent);

                btnAnalizar.setVisibility(View.VISIBLE);
                encodedImage = convertImgString();

           /* } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }*/
        }
        switch(requestCode) {
            case REQUEST_ACTIVITY_BODY:
                if(resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    if(bundle.getString("bodyPart")!= null){
                        bodyPart = bundle.getString("bodyPart");
                        section = bundle.getString("section");
                        askCameraPermissions();
                    }

                }
                break;
            case OPEN_GALERY:
                if(resultCode == RESULT_OK){

                    Uri selectedImage = data.getData();
                    imageView.setImageURI(selectedImage);
                    if (selectedImage != null) {
                        Bitmap compressedImgBitmap = null;
                        try {
                            compressedImgBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        compressedImgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        imageView.setImageBitmap(compressedImgBitmap);
                        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
                        encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        btnAnalizar.setVisibility(View.VISIBLE);
                    }

                }
                break;
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalCacheDir();
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

    public void showLoadingDialog() {

        if (progress == null) {
            progress = new ProgressDialog(getContext());
            progress.setTitle("Analizador");
            progress.setMessage("Skinner está analizando su imagen, aguarde un momento.");
        }
        progress.show();
    }

    public void dismissLoadingDialog() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }
}