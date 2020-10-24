package com.example.skinnerapp.ui.message;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skinnerapp.Model.LesionesResponse;
import com.example.skinnerapp.Model.MensajesPorPacienteResponse;
import com.example.skinnerapp.R;

import java.text.ParseException;
import java.util.ArrayList;

import static util.Util.formatDate;

public class AdaptadorListaMensajes extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context contexto;
    private ArrayList<MensajesPorPacienteResponse> datos;

    public AdaptadorListaMensajes(Context contexto, ArrayList<MensajesPorPacienteResponse> datos)
    {
        this.contexto = contexto;
        this.datos = datos;
        if(contexto !=null)
            inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final View vista = inflater.inflate(R.layout.list_mensajeria, null);
        TextView text_from = (TextView) vista.findViewById(R.id.text_from);
        TextView text_fecha = (TextView) vista.findViewById(R.id.text_fecha);
        TextView text_mensaje = (TextView) vista.findViewById(R.id.text_mensaje);
        TextView text_descripcion=(TextView) vista.findViewById(R.id.text_descripcion);
        ImageView img_lesion=(ImageView) vista.findViewById(R.id.imagen_lesion);
        String fechanueva= "";
        try {
            fechanueva = formatDate(datos.get(i).getFecha(),"dd/MM/yyyy HH:mm");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        text_from.setText(datos.get(i).getNombre_destino() +" "+datos.get(i).getApellido_destino());
        text_fecha.setText(fechanueva);
        text_mensaje.setText(datos.get(i).getMensaje());
        text_descripcion.setText(datos.get(i).getDescripcion());
        byte[] decodedString = Base64.decode(datos.get(i).getImagen(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img_lesion.setImageBitmap(decodedByte);
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
