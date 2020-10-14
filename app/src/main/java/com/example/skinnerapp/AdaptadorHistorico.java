package com.example.skinnerapp;

import android.content.Context;
import android.content.Intent;
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

import java.text.ParseException;
import java.util.ArrayList;

import static util.Util.formatDate;

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
        TextView txtdescripcion = (TextView) vista.findViewById(R.id.text_from);
        TextView txtfecha = (TextView) vista.findViewById(R.id.text_fecha);
        TextView txtubicacion = (TextView) vista.findViewById(R.id.text_mensaje);

        ImageView imagen = (ImageView) vista.findViewById(R.id.image_lesion);
        String fechanueva= "";
        try {
            fechanueva = formatDate(datos.get(i).getFecha(),"dd/MM/yyyy HH:mm a");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtdescripcion.setText(datos.get(i).getDescripcion());
        txtubicacion.setText(fechanueva);
        String comentario = datos.get(i).getComentario();

        if(comentario == null)
            comentario = "Sin comentario";

        txtfecha.setText(comentario);

        byte[] decodedString = Base64.decode(datos.get(i).getImagen(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imagen.setImageBitmap(decodedByte);
        /*
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
