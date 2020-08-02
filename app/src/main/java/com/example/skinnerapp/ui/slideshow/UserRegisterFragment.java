package com.example.skinnerapp.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.ActualizarUsuarioResponse;
import com.example.skinnerapp.Model.AnalizarImagenRequest;
import com.example.skinnerapp.Model.AnalizarImagenResponse;
import com.example.skinnerapp.Model.ObtenerUsuarioResponse;
import com.example.skinnerapp.Model.RegistrarUsuarioRequest;
import com.example.skinnerapp.Model.RegistrarUsuarioResponse;
import com.example.skinnerapp.R;
import com.example.skinnerapp.ResponseActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static util.Util.dismissLoadingDialog;
import static util.Util.getConnection;
import static util.Util.showLoadingDialog;

public class UserRegisterFragment extends Fragment {

    private UserRegisterViewModel slideshowViewModel;
    private Button btnregistrar;
    private EditText text_nombre;
    private EditText text_apellido;
    private EditText text_direccion;
    private EditText text_telefono;
    private View root;
    private Context contexto;
    private Integer userid = null;
    private ArrayList<ObtenerUsuarioResponse> userData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(UserRegisterViewModel.class);
        root = inflater.inflate(R.layout.fragment_user_register, container, false);
        contexto = this.getContext();
        btnregistrar = (Button)root.findViewById(R.id.boton_registrar);
        text_nombre = (EditText) root.findViewById(R.id.text_nombre);
        text_apellido = (EditText) root.findViewById(R.id.text_apellido);
        text_direccion = (EditText) root.findViewById(R.id.text_direccion);
        text_telefono = (EditText) root.findViewById(R.id.text_telefono);

       /* View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        text_nombre.setOnFocusChangeListener(ofcListener);
        text_apellido.setOnFocusChangeListener(ofcListener);
        text_direccion.setOnFocusChangeListener(ofcListener);
        text_telefono.setOnFocusChangeListener(ofcListener);*/
        obtenerUserIdApp();

        if(userid != null)
        {
            btnregistrar.setText("Actualizar datos");
            obtenerDatosUsuario();
        }

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid != null)
                    actualizarUsuario();
               else
                    registrarUsuario();
            }
        });

        return root;
    }

    private void actualizarUsuario() {
        if(datosValidos()){
            if(modificacionValidada())
            {
                Retrofit retrofit = getConnection();
                JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

                RegistrarUsuarioRequest req = new RegistrarUsuarioRequest(text_nombre.getText().toString(),text_apellido.getText().toString(),text_direccion.getText().toString(),text_telefono.getText().toString());
                Call<ActualizarUsuarioResponse> call= service.putUserById("/usuarios/"+userid,req);
                call.enqueue(new Callback<ActualizarUsuarioResponse>() {
                    @Override
                    public void onResponse(Call<ActualizarUsuarioResponse> call, Response<ActualizarUsuarioResponse> response) {
                        Toast.makeText(contexto, "Se actualizó correctamente los datos de su usuario", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<ActualizarUsuarioResponse> call, Throwable t) {
                        Toast.makeText(contexto, "Error al registrar usuario. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(contexto, "No se ha modificado ninguno de sus datos de usuario.", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        else {
            Toast.makeText(contexto, "Debe completar los datos solicitados antes presionar en actualizar datos.", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    //valida si se modifico o no algun dato del usuario
    private boolean modificacionValidada() {
        if(userData.get(0).getNombre().equals(text_nombre.getText().toString())  &&
            userData.get(0).getApellido().equals(text_apellido.getText().toString()) &&
            userData.get(0).getDireccion().equals(text_direccion.getText().toString()) &&
            userData.get(0).getTelefono().equals(text_telefono.getText().toString())
        )
            return false;
        else
            return true;
    }

    private void obtenerDatosUsuario() {

        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

        Call<ArrayList<ObtenerUsuarioResponse>> call= service.getUserById("/usuarios/"+userid);
        showLoadingDialog(getContext(),"Datos usuario","Skinner está recuperando sus datos de usuario, aguarde un momento.");
        call.enqueue(new Callback<ArrayList<ObtenerUsuarioResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ObtenerUsuarioResponse>> call, Response<ArrayList<ObtenerUsuarioResponse>> response) {
                userData = response.body();
                autoCompletarDatosUsuario();
            }

            @Override
            public void onFailure(Call<ArrayList<ObtenerUsuarioResponse>> call, Throwable t) {
                Toast.makeText(contexto, "Error al registrar usuario. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void autoCompletarDatosUsuario() {
        text_nombre.setText(userData.get(0).getNombre());
        text_apellido.setText(userData.get(0).getApellido());
        text_direccion.setText(userData.get(0).getDireccion());
        text_telefono.setText(userData.get(0).getTelefono());
        dismissLoadingDialog();
    }

    private void obtenerUserIdApp() {
        userid = 8; //reemplazar por user id persistido en la app
    }


    private void registrarUsuario(){
        if(datosValidos()){

            final String[] textoRespuesta = {""};
            Retrofit retrofit = getConnection();
            JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

            RegistrarUsuarioRequest req = new RegistrarUsuarioRequest(text_nombre.getText().toString(),text_apellido.getText().toString(),text_direccion.getText().toString(),text_telefono.getText().toString());
            Call<ArrayList<RegistrarUsuarioResponse>> call= service.postRegistrarUsuario(req);
            call.enqueue(new Callback<ArrayList<RegistrarUsuarioResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<RegistrarUsuarioResponse>> call, Response<ArrayList<RegistrarUsuarioResponse>> response) {
                    textoRespuesta[0] =response.body().toString();
                    Toast.makeText(contexto, "Se envío correctamente la petición. Falta retorno del servidor."+ response.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ArrayList<RegistrarUsuarioResponse>> call, Throwable t) {
                    Toast.makeText(contexto, "Error al registrar usuario. "+ textoRespuesta[0], Toast.LENGTH_SHORT).show();
                }

            });

        }
        else {
            Toast.makeText(contexto, "Debe completar los datos solicitados antes presionar en registrar.", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private Boolean datosValidos(){

        if(text_nombre.getText().toString().matches("") ||
                text_apellido.getText().toString().matches("") ||
                text_direccion.getText().toString().matches("") ||
                text_telefono.getText().toString().matches("") ){
            return false;
        }else
            return true;

    }
/*
    private class MyFocusChangeListener implements View.OnFocusChangeListener {
        public void onFocusChange(View v, boolean hasFocus){

            if(v.getId() == R.id.textbox && !hasFocus) {

                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }*/
}