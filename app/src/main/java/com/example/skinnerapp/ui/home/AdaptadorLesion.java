package com.example.skinnerapp.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skinnerapp.Model.LesionesResponse;
import com.example.skinnerapp.R;

import java.text.ParseException;
import java.util.ArrayList;

import static util.Util.formatDate;

public class AdaptadorLesion extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context contexto;
    private ArrayList<LesionesResponse> datos;

    public AdaptadorLesion(Context contexto, ArrayList<LesionesResponse> datos)
    {
        this.contexto = contexto;
        this.datos = datos;
        if(contexto !=null)
            inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.list_elemets, null);
        TextView txtdescripcion = (TextView) vista.findViewById(R.id.text_from);
        TextView txtfecha = (TextView) vista.findViewById(R.id.text_fecha);
        TextView txtubicacion = (TextView) vista.findViewById(R.id.text_mensaje);

        txtdescripcion.setPaintFlags(txtdescripcion.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        ImageView imagen = (ImageView) vista.findViewById(R.id.image_lesion);
        String fechanueva= "";
        try {
            fechanueva = formatDate(datos.get(i).getFecha_creacion(),"dd/MM/yyyy HH:mm");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtdescripcion.setText(datos.get(i).getDescripcion());
        txtubicacion.setText(fechanueva+" Hs");
        txtfecha.setText(datos.get(i).getUbicacion());

        byte[] decodedString = Base64.decode(datos.get(i).getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imagen.setImageBitmap(decodedByte);

        //imagen.setTag(i);
        /*
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent visorImagen = new Intent(contexto, VisorImagen.class);
                visorImagen.putExtra("IMG", datosImg[(Integer)v.getTag()]);
                contexto.startActivity(visorImagen);
            }
        });
    */
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
