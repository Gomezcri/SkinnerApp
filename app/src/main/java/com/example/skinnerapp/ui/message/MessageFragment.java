package com.example.skinnerapp.ui.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.skinnerapp.HistoryActivity;
import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Interface.ResultReceiver;
import com.example.skinnerapp.MessageActivity;
import com.example.skinnerapp.Model.LesionesResponse;
import com.example.skinnerapp.Model.MensajesPorPacienteResponse;
import com.example.skinnerapp.R;
import com.example.skinnerapp.ui.home.AdaptadorLesion;
import com.example.skinnerapp.ui.slideshow.UserRegisterViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.getConnection;


public class MessageFragment extends Fragment {
    private View root;
    private Button btn_msj;
    private Context contexto;
    private Integer RESULT_ACTIVITY_MESSAGE = 150;
    public ResultReceiver resultreceiver;
    private Integer id_paciente;
    private ArrayList<MensajesPorPacienteResponse> datos;
    private ListView lista;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        resultreceiver = (ResultReceiver)context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_message, container, false);
        lista = (ListView) root.findViewById(R.id.lista_mensajes);
        contexto = this.getContext();
        id_paciente = resultreceiver.getResultId();;
        datos = obtenerMensajesPorPaciente(id_paciente);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent resultIntent = new Intent(contexto, MessageActivity.class);
                resultIntent.putExtra("id_lesion", datos.get(i).getId_lesion());  // put data that you want returned to activity A
                resultIntent.putExtra("id_doctor", datos.get(i).getId_destino_usuario());  // put data that you want returned to activity A
                resultIntent.putExtra("id_paciente", id_paciente);  // put data that you want returned to activity A
                resultIntent.putExtra("nombre_doctor", datos.get(i).getNombre_destino() + " " +datos.get(i).getApellido_destino());  // put data that you want returned to activity A
                startActivityForResult(resultIntent,RESULT_ACTIVITY_MESSAGE);
            }
        });

        return root;
    }

    private ArrayList<MensajesPorPacienteResponse> obtenerMensajesPorPaciente(int userid) {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

        Call<ArrayList<MensajesPorPacienteResponse>> call= service.getMensajesByPacienteId("/mensajes/paciente/"+userid);
        call.enqueue(new Callback<ArrayList<MensajesPorPacienteResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MensajesPorPacienteResponse>> call, Response<ArrayList<MensajesPorPacienteResponse>> response) {
                datos = response.body();
                if(datos != null)
                    lista.setAdapter(new AdaptadorListaMensajes(getContext(),datos));
            }
            @Override
            public void onFailure(Call<ArrayList<MensajesPorPacienteResponse>> call, Throwable t) {
                Toast.makeText(contexto, "Error al obtener lesiones, el servicio no se encuentra disponible. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return datos;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //ACTIVITY RESULT TAKE PICTURE
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTIVITY_MESSAGE) {
            datos = obtenerMensajesPorPaciente(id_paciente);
        }

    }

}