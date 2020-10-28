package com.example.skinnerapp.ui.slideshow;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Interface.ResultReceiver;
import com.example.skinnerapp.Model.ActualizarUsuarioRequest;
import com.example.skinnerapp.Model.ActualizarUsuarioResponse;
import com.example.skinnerapp.Model.ObtenerUsuarioResponse;
import com.example.skinnerapp.Model.RegistrarUsuarioRequest;
import com.example.skinnerapp.Model.RegistrarUsuarioResponse;
import com.example.skinnerapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static util.Util.dismissLoadingDialog;
import static util.Util.getConnection;
import static util.Util.showLoadingDialog;

public class UserRegisterFragment extends Fragment {

    private UserRegisterViewModel slideshowViewModel;
    private Button btnregistrar;
    private EditText text_nombre;
    private EditText text_apellido;
    private EditText text_direccion;
    private EditText text_telefono;
    private EditText text_usuario;
    private View root;
    private Context contexto;
    private Integer userid = null;
    private ObtenerUsuarioResponse userData;
    public ResultReceiver resultreceiver;
    private JSONObject issueObj = null;
    private Integer id_ciudad = 0;
    private JSONArray jsonArray = null;
    private Spinner sp_localidades;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        resultreceiver = (ResultReceiver)context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(UserRegisterViewModel.class);
        root = inflater.inflate(R.layout.fragment_user_register, container, false);
        contexto = this.getContext();
        btnregistrar = (Button)root.findViewById(R.id.boton_registrar);
        text_nombre = (EditText) root.findViewById(R.id.text_nombre);
        text_apellido = (EditText) root.findViewById(R.id.text_apellido);
        text_direccion = (EditText) root.findViewById(R.id.text_direccion);
        text_telefono = (EditText) root.findViewById(R.id.text_telefono);
        text_usuario= (EditText) root.findViewById(R.id.tx_email);

        userid = resultreceiver.getResultId();

        sp_localidades = (Spinner) root.findViewById(R.id.spinner_localidades2);
        final ArrayList<String> items = getLocalidades("localidades.json");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(contexto,R.layout.spinner_layout,R.id.txt,items);
        sp_localidades.setAdapter(adapter);

        sp_localidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                try {
                    id_ciudad = Integer.valueOf(jsonArray.getJSONObject(position).getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        if(userid != null)
        {
            btnregistrar.setText("Actualizar datos");
            obtenerDatosUsuario();
        }

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid != null)
                    actualizarUsuario();
            }
        });

        return root;
    }

    private void actualizarUsuario() {
        if(datosValidos()){
            if(modificacionValidada())
            {
                Retrofit retrofit = getConnection();
                JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

                ActualizarUsuarioRequest req = new ActualizarUsuarioRequest(text_nombre.getText().toString(),text_apellido.getText().toString(),text_direccion.getText().toString(),text_telefono.getText().toString(),id_ciudad);
                Call<ActualizarUsuarioResponse> call= service.putUserById("/usuarios/"+userid,req);
                call.enqueue(new Callback<ActualizarUsuarioResponse>() {
                    @Override
                    public void onResponse(Call<ActualizarUsuarioResponse> call, Response<ActualizarUsuarioResponse> response) {
                        Toast.makeText(contexto, "Se actualizó correctamente los datos de su usuario", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<ActualizarUsuarioResponse> call, Throwable t) {
                        Toast.makeText(contexto, "Error al registrar usuario. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(contexto, "No se ha modificado ninguno de sus datos de usuario.", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        else {
            Toast.makeText(contexto, "Debe completar los datos solicitados antes presionar en actualizar datos.", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    //valida si se modifico o no algun dato del usuario
    private boolean modificacionValidada() {
        if(
                userData.getId_ciudad().equals(id_ciudad)  &&
                userData.getNombre().equals(text_nombre.getText().toString())  &&
            userData.getApellido().equals(text_apellido.getText().toString()) &&
            userData.getDireccion().equals(text_direccion.getText().toString()) &&
            userData.getTelefono().equals(text_telefono.getText().toString())
        )
            return false;
        else
            return true;
    }

    private void obtenerDatosUsuario() {

        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

        Call<ObtenerUsuarioResponse> call= service.getUserById("/usuarios/"+userid);
        showLoadingDialog(getContext(),"Datos usuario","Skinner está recuperando sus datos de usuario, aguarde un momento.");
        call.enqueue(new Callback<ObtenerUsuarioResponse>() {
            @Override
            public void onResponse(Call<ObtenerUsuarioResponse> call, Response<ObtenerUsuarioResponse> response) {
                userData = response.body();
                autoCompletarDatosUsuario();
                Integer useridciudad = userData.getId_ciudad();

                for (int i = 0, size = jsonArray.length(); i < size; i++)
                {
                    JSONObject objectInArray = null;
                    try {
                        objectInArray = jsonArray.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Integer value = null;
                    try {
                        value = Integer.valueOf(jsonArray.getJSONObject(i).getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(value != null)
                    {
                        if(value.equals(useridciudad))
                            break;//id_ciudad = i;
                    }
                    id_ciudad ++;
                }

                sp_localidades.setSelection(id_ciudad);
            }

            @Override
            public void onFailure(Call<ObtenerUsuarioResponse> call, Throwable t) {
                Toast.makeText(contexto, "Error al obtener datos del usuario. "+ t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void autoCompletarDatosUsuario() {
        text_nombre.setText(userData.getNombre());
        text_apellido.setText(userData.getApellido());
        text_direccion.setText(userData.getDireccion());
        text_telefono.setText(userData.getTelefono());
        text_usuario.setText(userData.getEmail());


        dismissLoadingDialog();
    }

    private Boolean datosValidos(){

        if(text_nombre.getText().toString().matches("") ||
                text_apellido.getText().toString().matches("") ||
                text_direccion.getText().toString().matches("") ||
                text_telefono.getText().toString().matches("") ||
                text_usuario.getText().toString().matches("")){
            return false;
        }else
            return true;

    }

    public ArrayList<String> getLocalidades(String archivo){

        String cadena=null;
        String localidad=null;
        String id_ciudad=null;
        ArrayList<String> localidadesList = new ArrayList<String>();

        InputStream is= null;
        try {
            is = getResources().getAssets().open(archivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int size = 0;
        try {
            size = is.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[size];
        try {
            is.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json= null;
        try {
            json = new String(data,"UTF-8");
            issueObj = new JSONObject(new String(data,"UTF-16"));
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(jsonArray!=null){
            for(int i =0; i<jsonArray.length();i++)
            {
                try {
                    localidadesList.add(localidad=jsonArray.getJSONObject(i).getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return localidadesList;
    }
}