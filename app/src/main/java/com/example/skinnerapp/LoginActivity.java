package com.example.skinnerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private Button crearUsuario;
    private String textoUsuario;
    private String textoContrasenia;
    private Integer userid;
    public final static int RESULT_ACTIVITY_MAIN = 119;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        us = (TextView) findViewById(R.id.tv_us);
        pass = (TextView) findViewById(R.id.tv_pass);
        ingresar = (Button) findViewById(R.id.bt_ingresar);
        crearUsuario = (Button) findViewById(R.id.bt_crearUsuario);
        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        userid = sharedPref.getInt(getString(R.string.saved_user_id), 0);

        if(userid != 0)
        {
            Intent resultIntent = new Intent(LoginActivity.this, MainActivity2.class);
            resultIntent.putExtra("id_usuario", userid);  // put data that you want returned to activity A
            resultIntent.putExtra("username", sharedPref.getString(getString(R.string.saved_user_name),""));
            resultIntent.putExtra("useremail", sharedPref.getString(getString(R.string.saved_user_email),""));
            startActivityForResult(resultIntent, RESULT_ACTIVITY_MAIN);
        }
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoguearUsuario();
            }
        });

        crearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Registrar_usuario.class);
                startActivity(intent);
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

                        saveUserData(response.body().getId(),response.body().getEmail(),response.body().getNombre() +" "+response.body().getApellido());
                        resultIntent.putExtra("id_usuario", response.body().getId());  // put data that you want returned to activity A
                        resultIntent.putExtra("username", response.body().getNombre() +" "+response.body().getApellido());  // put data that you want returned to activity A
                        resultIntent.putExtra("useremail", response.body().getEmail());  // put data that you want returned to activity A
                        startActivityForResult(resultIntent, RESULT_ACTIVITY_MAIN);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

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



    private void saveUserData(Integer id, String useremail, String username) {
        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_user_id), id);
        editor.putString(getString(R.string.saved_user_email), useremail);
        editor.putString(getString(R.string.saved_user_name), username);
        editor.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //ACTIVITY RESULT GPS
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_ACTIVITY_MAIN) {
            if(resultCode == RESULT_OK){
                String userdata = data.getStringExtra("userdata"); //0: close app, -1 delete user data

                if(userdata.equals("0")){
                    finish();
                    System.exit(0);
                }
                if(userdata.equals("-1")){
                    saveUserData(0,"","");
                }
            }
        }

    }

}