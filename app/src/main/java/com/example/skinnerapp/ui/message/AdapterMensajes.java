package com.example.skinnerapp.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.skinnerapp.Interface.ResultReceiver;
import com.example.skinnerapp.Model.MensajeResponse;
import com.example.skinnerapp.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static util.Util.formatDate;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {

    private List<MensajeResponse> listMensaje = new ArrayList<>();
    private Integer id_paciente;
    private Context c;
    public AdapterMensajes(Context c) {
        this.c = c;
    }

    public void addMensaje(List<MensajeResponse> mensajes,Integer id){
       // listMensaje.add(mensajes);
        id_paciente = id;
        listMensaje = mensajes;
        if(listMensaje != null)
            notifyItemInserted(listMensaje.size());
    }

    @Override
    public HolderMensaje onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.view_message,parent,false);
        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(HolderMensaje holder, int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre_origen() + " " +listMensaje.get(position).getApellido_origen());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());
        holder.getMensaje().setVisibility(View.VISIBLE);
/*
        if(listMensaje.get(position).getId_origen_usuario() == 19){
            holder.getNombre().setText(listMensaje.get(position).getNombre_destino() + " " +listMensaje.get(position).getApellido_destino());
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
           // Glide.with(c).load(listMensaje.get(position).getUrlFoto()).into(holder.getFotoMensaje());
        }else{
            holder.getNombre().setText(listMensaje.get(position).getNombre_destino() + " " +listMensaje.get(position).getApellido_destino());
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }/*
       /* if(listMensaje.get(position).getFotoPerfil().isEmpty()){
            holder.getFotoMensajePerfil().setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(c).load(listMensaje.get(position).getFotoPerfil()).into(holder.getFotoMensajePerfil());
        }*/

       /* if(listMensaje.get(position).getId_origen_usuario() == id_paciente){
            holder.getNombre().setText(listMensaje.get(position).getNombre_origen() + " " +listMensaje.get(position).getApellido_origen());
        }else{
            holder.getNombre().setText(listMensaje.get(position).getNombre_destino() + " " +listMensaje.get(position).getNombre_destino());
        }
*/
        String fechanueva = "";
        try {
            fechanueva = formatDate(listMensaje.get(position).getHora(),"dd/MM/yyyy HH:mm");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.getHora().setText(fechanueva+" Hs");
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
