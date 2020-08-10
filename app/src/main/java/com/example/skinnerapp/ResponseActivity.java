package com.example.skinnerapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResponseActivity extends AppCompatActivity {

    private ImageView imagenDeResultado;
    private TextView textoResultado;
    private Integer id_tipo;
    private String mensaje = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_response);
        int codigo = getIntent().getIntExtra("estado",404);
        mensaje = getIntent().getStringExtra("respuestaServidor");
        id_tipo  = getIntent().getIntExtra("id_tipo",0);

        imagenDeResultado = (ImageView) findViewById(R.id.imagenResultado);
        textoResultado= (TextView) findViewById(R.id.textoResultado);
        if(codigo==200){
            imagenDeResultado.setImageResource(R.drawable.checkverde);

            switch (codigo){
                case 1:
                    textoResultado.setText("Detectamos que no te encontras con ninguna lesion en tu piel. Si queres realizar otro chequeo, volve para atras");
                    break;
                case 2:
                    textoResultado.setText("Detectamos que no te encontras con ninguna lesion en tu piel. Si queres realizar otro chequeo, volve para atras");
                    break;
                case 3:
                    textoResultado.setText("Detectamos que no te encontras con ninguna lesion en tu piel. Si queres realizar otro chequeo, volve para atras");
                    break;
                case 4:
                    textoResultado.setText("Detectamos que no te encontras con ninguna lesion en tu piel. Si queres realizar otro chequeo, volve para atras");
                    break;
            }
        }


        if(codigo==404){
            imagenDeResultado.setImageResource(R.drawable.signopregunta);
            textoResultado.setText("No hay conexion con el servidor, porfavor intenta en unos instantes");
        }
    }
}