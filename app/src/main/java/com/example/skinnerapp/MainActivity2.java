package com.example.skinnerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Interface.ResultReceiver;
import com.example.skinnerapp.Model.HistoricoResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;

import static util.Util.getConnection;

public class MainActivity2 extends AppCompatActivity implements ResultReceiver{

    private AppBarConfiguration mAppBarConfiguration;
    private static Integer id_user;
    private static String struseremail;
    private static String strusername;
    private TextView useremail;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        id_user = getIntent().getIntExtra("id_usuario",0);
        strusername = getIntent().getStringExtra("username");
        struseremail = getIntent().getStringExtra("useremail");

        //algo
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        useremail = (TextView) headerView.findViewById(R.id.useremail);
        username = (TextView) headerView.findViewById(R.id.username);


        useremail.setText(struseremail);
        username.setText(strusername);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_info, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public Integer getResultId() {
        return id_user;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Aviso")
                .setMessage("¿Desea cerrar la aplicación?")
                .setNegativeButton("NO", null)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeUserData("0");
                    }
                }).create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.st_cerrar_sesion:
                new AlertDialog.Builder(this)
                        .setTitle("Aviso")
                        .setMessage("¿Desea cerrar sesión?")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Cerrar_Sesion();
                            }
                        }).create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void Cerrar_Sesion() {
        Retrofit retrofit = getConnection();
        JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);
        Call<Void> call= service.postCerrarSesion("/cerrar_sesion/"+id_user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                removeUserData("-1");
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });

    }

    private void removeUserData(String close) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("userdata", close);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}