package com.example.skinnerapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skinnerapp.Model.HistoricoResponse;
import com.example.skinnerapp.Model.LesionesResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class AdaptadorHistorico extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context contexto;
    private ArrayList<HistoricoResponse> datos;

    public AdaptadorHistorico(Context contexto, ArrayList<HistoricoResponse> datos)
    {
        this.contexto = contexto;
        this.datos = datos;
        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.list_elemets, null);
        TextView txtdescripcion = (TextView) vista.findViewById(R.id.text_descripcion);
        TextView txtfecha = (TextView) vista.findViewById(R.id.text_fecha);
        TextView txtubicacion = (TextView) vista.findViewById(R.id.text_ubicacion);

        ImageView imagen = (ImageView) vista.findViewById(R.id.image_lesion);
        SimpleDateFormat formato =  new SimpleDateFormat("dd/MM/yyyy");
        txtdescripcion.setText(datos.get(i).getDescripcion());
        txtfecha.setText(datos.get(i).getFecha());
        txtubicacion.setText(datos.get(i).getId().toString());

        byte[] decodedString = Base64.decode(datos.get(i).getImagen(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imagen.setImageBitmap(decodedByte);

        /*imagen.setImageResource(getImage(datos));

        imagen.setTag(i);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent visorImagen = new Intent(contexto, VisorImagen.class);
                visorImagen.putExtra("IMG", datosImg[(Integer)v.getTag()]);
                contexto.startActivity(visorImagen);
            }
        });*/
        return vista;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
