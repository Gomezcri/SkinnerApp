package com.example.skinnerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.LoginUsuarioRequest;
import com.example.skinnerapp.Model.LoginUsuarioResponse;
import com.example.skinnerapp.Model.RegistrarUsuarioRequest;
import com.example.skinnerapp.Model.RegistrarUsuarioResponse;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.getConnection;

public class Registrar_usuario extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private EditText nombre;
    private EditText apellido;
    private EditText direccion;
    private EditText telefono;
    private Integer id_ciudad=0;
    private String cadena;
    private String[] aux;
    private Button registro;
    private String emailtexto;
    private String passtexto;
    private String nombretexto;
    private String apellidotexto;
    private String direcciontexto;
    private String telefonotexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_usuario);
        email = (EditText) findViewById(R.id.tv_email);
        pass = (EditText) findViewById(R.id.tv_pass);
        nombre = (EditText) findViewById(R.id.tv_nombre);
        apellido = (EditText) findViewById(R.id.tv_apellido);
        direccion = (EditText) findViewById(R.id.tv_direccion);
        telefono = (EditText) findViewById(R.id.tv_tel);
         registro = (Button) findViewById(R.id.bt_registrar);

        final Spinner sp_localidades= (Spinner) findViewById(R.id.spinner_localidades);
        final ArrayList<String> items = getLocalidades("localidades.json");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,items);
        sp_localidades.setAdapter(adapter);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mandarRegistro();
            }
        });


        //Listener spinner localidades
        sp_localidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cadena = items.get(position);
                aux=cadena.split("-");
                id_ciudad= Integer.valueOf(aux[1]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void mandarRegistro() {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
           if(validarCampos()) {
               emailtexto = email.getText().toString();
               passtexto = pass.getText().toString();
               nombretexto = nombre.getText().toString();
               apellidotexto = apellido.getText().toString();
               direcciontexto = direccion.getText().toString();
               telefonotexto = telefono.getText().toString();

               RegistrarUsuarioRequest req = new RegistrarUsuarioRequest(emailtexto, passtexto, nombretexto, apellidotexto, direcciontexto, telefonotexto, id_ciudad);
               Call<RegistrarUsuarioResponse> call = service.postRegistrarUsuario(req);
               call.enqueue(new Callback<RegistrarUsuarioResponse>() {
                   @Override
                   public void onResponse(Call<RegistrarUsuarioResponse> call, Response<RegistrarUsuarioResponse> response) {

                       Toast.makeText(Registrar_usuario.this,R.string.registroUsuario_ok, Toast.LENGTH_SHORT).show();
                       finish();
                   }

                   @Override
                   public void onFailure(Call<RegistrarUsuarioResponse> call, Throwable t) {
                       Toast.makeText(Registrar_usuario.this, R.string.registroUsuario_error, Toast.LENGTH_SHORT).show();
                   }

               });
           }
           else
               Toast.makeText(Registrar_usuario.this, R.string.registroUsuario_campoincompleto, Toast.LENGTH_SHORT).show();

    }

    private boolean validarCampos() {
        if(email.getText()!=null && pass.getText()!=null && nombre.getText()!=null && apellido.getText()!=null &&
        direccion.getText()!=null && telefono.getText()!= null && id_ciudad!=0)
        return true;
        else
        return false;

    }

    public ArrayList<String> getLocalidades(String archivo){
        JSONArray jsonArray=null;
        String cadena=null;
        String localidad=null;
        String id_ciudad=null;
        ArrayList<String> localidadesList = new ArrayList<String>();

        InputStream is= null;
        try {
            is = getResources().getAssets().open(archivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int size = 0;
        try {
            size = is.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[size];
        try {
            is.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json= null;
        try {
            json = new String(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonArray!=null){
            for(int i =0; i<jsonArray.length();i++)
            {
                try {localidad=jsonArray.getJSONObject(i).getString("name");
                    id_ciudad=jsonArray.getJSONObject(i).getString("id");
                    cadena = localidad+"-"+id_ciudad;
                    localidadesList.add(cadena);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return localidadesList;
    }
}