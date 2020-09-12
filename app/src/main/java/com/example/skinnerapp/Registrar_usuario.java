package com.example.skinnerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Registrar_usuario extends AppCompatActivity {

    private String id_ciudad;
    private String cadena;
    private String[] aux;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_usuario);
        final Spinner sp_localidades= (Spinner) findViewById(R.id.spinner_localidades);
        final ArrayList<String> items = getLocalidades("localidades.json");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,items);
        sp_localidades.setAdapter(adapter);

        //Listener spinner localidades
        sp_localidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cadena = items.get(position);
                aux=cadena.split("-");
                id_ciudad=aux[1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

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