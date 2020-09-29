package com.example.skinnerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.skinnerapp.Model.MensajeResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static util.Util.formatDate;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {

    private List<MensajeResponse> listMensaje = new ArrayList<>();
    private Context c;
    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(List<MensajeResponse> mensajes){
       // listMensaje.add(mensajes);
        listMensaje = mensajes;
        notifyItemInserted(listMensaje.size());
    }

    @Override
    public HolderMensaje onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.view_message,parent,false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(HolderMensaje holder, int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre_destino() + " " +listMensaje.get(position).getApellido_destino());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());

        if(listMensaje.get(position).getId_origen_usuario() == 19){
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
           // Glide.with(c).load(listMensaje.get(position).getUrlFoto()).into(holder.getFotoMensaje());
        }else{
            
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }
       /* if(listMensaje.get(position).getFotoPerfil().isEmpty()){
            holder.getFotoMensajePerfil().setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(c).load(listMensaje.get(position).getFotoPerfil()).into(holder.getFotoMensajePerfil());
        }*/
        String fechanueva = "";
        try {
            fechanueva = formatDate(listMensaje.get(position).getHora(),"hh:mm:ss a");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.getHora().setText(fechanueva);
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
