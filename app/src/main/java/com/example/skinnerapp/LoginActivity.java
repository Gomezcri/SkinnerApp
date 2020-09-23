package com.example.skinnerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.LoginUsuarioRequest;
import com.example.skinnerapp.Model.LoginUsuarioResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.getConnection;

public class LoginActivity extends AppCompatActivity {

    private TextView us;
    private TextView pass;
    private TextView olvidoContrasenia;
    private Button ingresar;
    private Button crearUsuario;
    private String textoUsuario;
    private String textoContrasenia;
    private Integer userid;
    public final static int RESULT_ACTIVITY_MAIN = 119;
    public final static int RESULT_ACTIVITY_RESTART = 120;
    public final static int RESULT_ACTIVITY_ACTUALIZAR_PSW = 121;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        us = (TextView) findViewById(R.id.tv_us);
        pass = (TextView) findViewById(R.id.tv_pass);
        ingresar = (Button) findViewById(R.id.bt_ingresar);
        crearUsuario = (Button) findViewById(R.id.bt_crearUsuario);
        olvidoContrasenia = (TextView) findViewById(R.id.textView_olvidoContrasenia);

        //Sppanable para hacer click y abrir otra activity
        setClickableString("Click AQUÍ", "¿Olvidaste tu contraseña? Click AQUÍ", olvidoContrasenia);

        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        //PARA OBTENER EL TOKEN
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
            }
        });

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        userid = sharedPref.getInt(getString(R.string.saved_user_id), 0);

        if(userid != 0){
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
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        if(us.getText()!= null && pass.getText() != null)
        {
            textoUsuario = us.getText().toString();
            textoContrasenia = pass.getText().toString();
            LoginUsuarioRequest req = new LoginUsuarioRequest(textoUsuario,textoContrasenia,token);
            Call<LoginUsuarioResponse> call= service.getUsuarioLogin(req);
            call.enqueue(new Callback<LoginUsuarioResponse>() {
                @Override
                public void onResponse(Call<LoginUsuarioResponse> call, Response<LoginUsuarioResponse> response) {

                    if(response.body().getId()!=null){
                        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
                        boolean changepassword = sharedPref.getBoolean(getString(R.string.saved_password), false);
                        if(changepassword)
                        {
                            Intent resultIntent = new Intent(LoginActivity.this, Cambiar_password.class);
                            resultIntent.putExtra("id_usuario", response.body().getId());
                            resultIntent.putExtra("username", response.body().getNombre() +" "+response.body().getApellido());  // put data that you want returned to activity A
                            resultIntent.putExtra("useremail", response.body().getEmail());  // put data that you want returned to activity A
                            startActivityForResult(resultIntent, RESULT_ACTIVITY_ACTUALIZAR_PSW);
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        }
                        else
                        {
                            Intent resultIntent = new Intent(LoginActivity.this, MainActivity2.class);
                            saveUserData(response.body().getId(),response.body().getEmail(),response.body().getNombre() +" "+response.body().getApellido(),false);
                            resultIntent.putExtra("id_usuario", response.body().getId());  // put data that you want returned to activity A
                            resultIntent.putExtra("username", response.body().getNombre() +" "+response.body().getApellido());  // put data that you want returned to activity A
                            resultIntent.putExtra("useremail", response.body().getEmail());  // put data that you want returned to activity A
                            startActivityForResult(resultIntent, RESULT_ACTIVITY_MAIN);
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta, intente de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<LoginUsuarioResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "No se pudo conectar con el servidor, intenta mas tarde.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveUserData(Integer id, String useremail, String username,Boolean changepassword) {
        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_user_id), id);
        editor.putString(getString(R.string.saved_user_email), useremail);
        editor.putString(getString(R.string.saved_user_name), username);
        editor.putBoolean(getString(R.string.saved_password),changepassword);
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
                    saveUserData(0,"","",false);
                }
            }
        }

        if (requestCode == RESULT_ACTIVITY_RESTART) {
            if(resultCode == RESULT_OK){
                boolean changepassword = data.getBooleanExtra("change",false); //0: close app, -1 delete user data

                if(changepassword){
                    saveUserData(0,"","",true);
                }
            }
        }

        if (requestCode == RESULT_ACTIVITY_ACTUALIZAR_PSW) {
            if(resultCode == RESULT_OK){
                boolean changepassword = data.getBooleanExtra("actualizado",false); //0: close app, -1 delete user data

                if(changepassword){
                    String strusername = data.getStringExtra("username");
                    String struseremail = data.getStringExtra("useremail");
                    Integer id_user = data.getIntExtra("id_usuario",0);

                    Intent resultIntent = new Intent(LoginActivity.this, MainActivity2.class);
                    saveUserData(id_user,struseremail,strusername,false);
                    resultIntent.putExtra("id_usuario", id_user);  // put data that you want returned to activity A
                    resultIntent.putExtra("username", strusername);  // put data that you want returned to activity A
                    resultIntent.putExtra("useremail", struseremail);  // put data that you want returned to activity A
                    startActivityForResult(resultIntent, RESULT_ACTIVITY_ACTUALIZAR_PSW);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            }
        }

    }

    public void setClickableString(String clickableValue, String wholeValue, TextView yourTextView){
        String value = wholeValue;
        SpannableString spannableString = new SpannableString(value);
        int startIndex = value.indexOf(clickableValue);
        int endIndex = startIndex + clickableValue.length();
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true); // <-- (false) this will remove automatic underline in set span
            }

            @Override
            public void onClick(View widget) {
                // do what you want with clickable value
                Intent intent = new Intent(LoginActivity.this, Recuperar_contraseña.class);
                startActivityForResult(intent,RESULT_ACTIVITY_RESTART);
            }
        }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        yourTextView.setText(spannableString);
        yourTextView.setMovementMethod(LinkMovementMethod.getInstance()); // <-- important, onClick in ClickableSpan won't work without this
    }

}