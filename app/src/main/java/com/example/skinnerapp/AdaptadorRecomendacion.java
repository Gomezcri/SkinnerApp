package com.example.skinnerapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.skinnerapp.Model.TratamientoResponse;

import java.util.ArrayList;


public class AdaptadorRecomendacion extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context contexto;
    private ArrayList<TratamientoResponse> datos;

    public AdaptadorRecomendacion(Context contexto, ArrayList<TratamientoResponse> datos) {
        this.contexto = contexto;
        this.datos = datos;
        if(contexto !=null)
            inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final View vista = inflater.inflate(R.layout.list_tratamiento, null);
        TextView txttitulo = (TextView) vista.findViewById(R.id.text_from);
        TextView txtdescripcion = (TextView) vista.findViewById(R.id.text_mensaje);
        if(datos != null)
        {
            txtdescripcion.setText(datos.get(i).getDescripcion());
            txttitulo.setText(datos.get(i).getTitulo());
            txttitulo.setPaintFlags(txttitulo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }


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
