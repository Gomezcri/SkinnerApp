package com.example.skinnerapp.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.skinnerapp.AddLesionActivity;
import com.example.skinnerapp.FindDoctorActivity;
import com.example.skinnerapp.HistoryActivity;
import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Interface.ResultReceiver;
import com.example.skinnerapp.MainActivity2;
import com.example.skinnerapp.Model.LesionesResponse;
import com.example.skinnerapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.getConnection;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;
    private ListView lista;
    private ArrayList<LesionesResponse> datos = null;
    private Context contexto;
    private Button btn_add_lesion;
    private Button btn_gps;
    public ResultReceiver resultreceiver;
    private Integer id_paciente;
    public final static int RESULT_ACTIVITY_LESION = 122;
    public final static int RESULT_ACTIVITY_HISTORICO = 123;

    public ProgressDialog progressDialog_home;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        resultreceiver = (ResultReceiver)context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        contexto = this.getContext();
        progressDialog_home = new ProgressDialog(contexto);
        lista = (ListView) root.findViewById(R.id.lista_lesion);
        btn_add_lesion = (Button) root.findViewById(R.id.button_add_lesion);
        btn_add_lesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddLesionActivity();
            }
        });

        // TODO: SACAR
        btn_gps = (Button) root.findViewById(R.id.idgps);
        btn_gps.setVisibility(View.GONE);
        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(contexto, FindDoctorActivity.class);
                startActivityForResult(resultIntent,RESULT_ACTIVITY_LESION);
            }
        });
        id_paciente = resultreceiver.getResultId();
        datos = obtenerLesiones(id_paciente);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent resultIntent = new Intent(contexto, HistoryActivity.class);
                resultIntent.putExtra("id_lesion", datos.get(i).getId());  // put data that you want returned to activity A
                resultIntent.putExtra("id_doctor", datos.get(i).getId_doctor());  // put data that you want returned to activity A
                resultIntent.putExtra("id_tipo", datos.get(i).getId_tipo());  // put data that you want returned to activity A
                resultIntent.putExtra("id_paciente", id_paciente);
                resultIntent.putExtra("id_lugar", datos.get(i).getId_lugar());
                startActivityForResult(resultIntent,RESULT_ACTIVITY_HISTORICO);
            }
        });

        return root;
    }

    private ArrayList<LesionesResponse> obtenerLesiones(int userid) {

        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        mostrarCartel_home();
        Call<ArrayList<LesionesResponse>> call= service.getLesionesById("/lesiones/paciente/"+userid);
        call.enqueue(new Callback<ArrayList<LesionesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LesionesResponse>> call, Response<ArrayList<LesionesResponse>> response) {
                cerrarCartel_home();
                datos = response.body();
                if(datos != null)
                    lista.setAdapter(new AdaptadorLesion(getContext(),datos));
            }
            @Override
            public void onFailure(Call<ArrayList<LesionesResponse>> call, Throwable t) {
                Toast.makeText(contexto, "Error al obtener lesiones, el servicio no se encuentra disponible. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return datos;
    }

    private void cerrarCartel_home() {progressDialog_home.dismiss();
    }

    private void mostrarCartel_home() {
        progressDialog_home.show();
        progressDialog_home.setContentView(R.layout.progress_dialog_home);
        progressDialog_home.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void openAddLesionActivity(){
        Intent resultIntent = new Intent(contexto, AddLesionActivity.class);
        resultIntent.putExtra("id_lesion", 0);  // put data that you want returned to activity A
        resultIntent.putExtra("id_doctor", 0);  // put data that you want returned to activity A
        resultIntent.putExtra("id_tipo", 0);  // put data that you want returned to activity A
        resultIntent.putExtra("id_user", resultreceiver.getResultId());  // put data that you want returned to activity A
        startActivityForResult(resultIntent,RESULT_ACTIVITY_LESION);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //ACTIVITY RESULT TAKE PICTURE
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTIVITY_LESION) {
            datos = obtenerLesiones(resultreceiver.getResultId());
        }
        if (requestCode == RESULT_ACTIVITY_HISTORICO) {
            datos = obtenerLesiones(resultreceiver.getResultId());
        }

    }

}