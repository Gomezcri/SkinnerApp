package com.example.skinnerapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.skinnerapp.Interface.JsonPlaceHolderApi;
import com.example.skinnerapp.Model.AsignacionRequest;
import com.example.skinnerapp.Model.AsignacionResponse;
import com.example.skinnerapp.Model.RegistrarHistoricoResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import util.MyItem;

import static util.Util.dismissLoadingDialog;
import static util.Util.getConnection;

public class FindDoctorActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

        private GoogleMap mMap;
        private GoogleApiClient googleApiClient;
        private LocationRequest locationRequest;
        private Location lastLocation;
        private Marker currentUserLocationMarker;
        private static final int Request_User_Location_Code = 99;
        private Marker myMarker;
        AlertDialog alert = null;
        Spinner combo_opciones;
        private final static String mLogTag = "GeoJsonDemo";
        private String[] osdefeatures = {"DIRECTOR","CALLE","ALTURA","TELEFONO"};
        private String[] hospitalesfeatures = {"nombre","domicilio","",""};
        // Declare a variable for the cluster manager.
        private ClusterManager<MyItem> mClusterManager;
        private Integer id_lesion;
        private Integer id_paciente;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);
        combo_opciones = (Spinner) findViewById(R.id.sp_opcion);
        final ArrayList<String> items = getOpciones("opciones_findDoctor.json");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txt,items);
        combo_opciones.setAdapter(adapter);

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.combo_Opciones,android.R.layout.simple_spinner_item);
        //combo_opciones.setAdapter(adapter);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
                checkUserLocationPermission();
        id_paciente = getIntent().getIntExtra("id_paciente",0);
        id_lesion = getIntent().getIntExtra("id_lesion",0);
        checkEnableGPS();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
}

        private ArrayList<String> getOpciones(String archivo) {
                JSONArray jsonArray=null;
                String opcion=null;
                ArrayList<String> opcionesList = new ArrayList<String>();

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
                } catch (UnsupportedEncodingException e) {
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
                                try {opcion=jsonArray.getJSONObject(i).getString("name");
                                        opcionesList.add(opcion);
                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }
                        }
                }

                return opcionesList;
        }


        private void setUpClusterer() {
                // Position the map.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

                // Initialize the manager with the context and the map.
                // (Activity extends context, so we can pass 'this' in the constructor.)
                mClusterManager = new ClusterManager<MyItem>(this, mMap);
                mClusterManager.setRenderer(new MarkerClusterRenderer(this,mMap,mClusterManager));
                // Point the map's listeners at the listeners implemented by the cluster
                // manager.
                mMap.setOnCameraIdleListener(mClusterManager);
                mMap.setOnMarkerClickListener(mClusterManager);
                mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>(){
                        @Override
                        public boolean onClusterClick(final Cluster<MyItem> cluster) {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        cluster.getPosition(), (float) Math.floor(mMap
                                                .getCameraPosition().zoom + 1)), 300,
                                        null);
                                return true;
                        }
                });

                mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>(){
                        @Override
                        public boolean onClusterItemClick(MyItem item) {
                                itemClickShowDialog(item);
                                return true;
                        }
                });


        }

        private void itemClickShowDialog(MyItem item) {
                final Integer id_doctor = Integer.parseInt(item.getmId_doctor());

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("¿Desea solicitar su atención con el doctor " + item.getTitle()+"?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        //TO-DO mostrar patalla de comunicacion con el medico
                                        addAsignacion(id_doctor);

                                }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                        dialog.cancel();
                                }
                        });
                alert = builder.create();
                alert.show();
        }

        private void addAsignacion(Integer id_doctor) {
                Retrofit retrofit = getConnection();
                JsonPlaceHolderApi service = retrofit.create(JsonPlaceHolderApi.class);

                AsignacionRequest req = new AsignacionRequest(id_lesion,id_doctor,id_paciente);
                Call<AsignacionResponse> call= service.postRegistrarAsignacion(req);

                call.enqueue(new Callback<AsignacionResponse>() {
                        @Override
                        public void onResponse(Call<AsignacionResponse> call, Response<AsignacionResponse> response) {
                                Toast.makeText(FindDoctorActivity.this, "Su solicitud ha sido enviada, volviendo al menu principal.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                        }
                        @Override
                        public void onFailure(Call<AsignacionResponse> call, Throwable t) {
                        }

                });
        }

        private void checkEnableGPS() {
                LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                boolean network_enabled = false;

                try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch(Exception ex) {}

                if(!gps_enabled && !network_enabled) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(R.string.open_location_settings)
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                dialog.cancel();
                                        }
                                });
                        alert = builder.create();
                        alert.show();
                }
        }

        @Override
        protected void onDestroy(){
                super.onDestroy();
                if(alert != null)
                {
                        alert.dismiss ();
                }
        }

        /**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */

@Override
public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                setUpClusterer();
                combo_opciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //Toast.makeText(getApplicationContext(), "ELEGISTE OPCION: "+position,Toast.LENGTH_SHORT).show(); // numero/posicion del elemento elegido
                                //Toast.makeText(getApplicationContext(), "ELEGISTE OPCION: "+parent.getItemAtPosition(position),Toast.LENGTH_SHORT).show(); // contenido de la posicion elegida
                        switch (position) {
                                case 0://no seleccionado
                                        mClusterManager.clearItems();
                                        mMap.clear();
                                        break;
                                case 1://hospitales
                                        retrieveFileFromResource(R.raw.hospitales,hospitalesfeatures);
                                        break;
                               /* case 2://galeno
                                        Toast.makeText(getApplicationContext(), "SOY UNA TOSTADA" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show(); // contenido de la posicion elegida
                                        break;*/
                                case 2://osde
                                        retrieveFileFromResource(R.raw.docosde,osdefeatures);
                                        break;
                        }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                });

        }

        }




public boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
        }
        else
        {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
        }
                return false;
        }
        else
        {
                return true;
        }
        }


@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
        case Request_User_Location_Code:
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
        if(googleApiClient == null){
        buildGoogleApiClient();
        }
        mMap.setMyLocationEnabled(true);
        }
        }
        else{
        Toast.makeText(this,"PERMISO DENEGADO",Toast.LENGTH_LONG);
        }
        return;
        }
        }

protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        googleApiClient.connect();
        }

@Override
public void onLocationChanged(Location location) {
        lastLocation = location;
        if(currentUserLocationMarker!=null){
                currentUserLocationMarker.remove();
                }

       // LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        LatLng latLng = new LatLng(-34.642978,-58.541109);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Posición actual");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        currentUserLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

        if(googleApiClient!=null){
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
                }
        }


@Override
public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
                onLocationChanged(location);
        }
        }

@Override
public void onConnectionSuspended(int i) {

        }

@Override
public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }


private void retrieveFileFromResource(int selection, String[] features) {
                try {
                        mMap.clear();
                        mClusterManager.clearItems();
                        GeoJsonLayer layer = new GeoJsonLayer(mMap,selection,getApplicationContext() );

                        //layer.removeLayerFromMap();

                      //  addGeoJsonLayerToMap(layer);
                        // Set a listener for geometry clicked events.
                       // layer.setOnFeatureClickListener(new GeoJsonLayer.OnFeatureClickListener() {
                         //       @Override
                           //     public void onFeatureClick(Feature feature) {
                             //           Point coordenada= (Point) feature.getGeometry();
                               //         Log.i("GeoJsonClick", "Feature clicked: " + feature.getProperty("NOMBRE"));
                                        /*Marker melbourne = mMap.addMarker(
                                                new MarkerOptions()
                                                        .position(((GeoJsonPoint) feature.getGeometry()).getCoordinates())
                                                        .title(feature.getProperty("NOMBRE")));

                                        melbourne.showInfoWindow();
*/
                                //}
                        //});
                     //   BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_medico);

                        for (GeoJsonFeature feature : layer.getFeatures()) {

                                // Set the lat/long coordinates for the marker.
                                LatLng coordenada = (LatLng) feature.getGeometry().getGeometryObject();
                                double lat = coordenada.latitude;
                                double lng = coordenada.longitude;
                                String id_doctor = feature.getProperty("ID");

// Set the title and snippet strings.
                                String title = feature.getProperty(features[0]);
                                String snippet = "Calle: "+feature.getProperty(features[1])+
                                        " Altura: "+feature.getProperty(features[2])+System.getProperty("line.separator")+
                                        "Telefono: "+feature.getProperty(features[3])+"\n";

// Create a cluster item for the marker and set the title and snippet using the constructor.
                                MyItem infoWindowItem = new MyItem(lat, lng, title, snippet,id_doctor);

// Add the cluster item (marker) to the cluster manager.
                                mClusterManager.addItem(infoWindowItem);
                                /*GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
                                // Set options for the point style
                                pointStyle.setIcon(icon);
                                pointStyle.setTitle(feature.getProperty(features[0]));//DIRECTOR
                                pointStyle.setSnippet("Calle: "+feature.getProperty(features[1])+
                                        " Altura: "+feature.getProperty(features[2])+System.getProperty("line.separator")+
                                        "Telefono: "+feature.getProperty(features[3])+"\n"
                                );
                                feature.setPointStyle(pointStyle);*/
                                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                        @Override
                                        public View getInfoWindow(Marker arg0) {
                                                return null;
                                        }

                                        @Override
                                        public View getInfoContents(Marker marker) {

                                                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                                                LinearLayout info = new LinearLayout(context);
                                                info.setOrientation(LinearLayout.VERTICAL);

                                                TextView title = new TextView(context);
                                                title.setTextColor(Color.BLACK);
                                                title.setGravity(Gravity.CENTER);
                                                title.setTypeface(null, Typeface.BOLD);
                                                title.setText(marker.getTitle());

                                                TextView snippet = new TextView(context);
                                                snippet.setTextColor(Color.GRAY);
                                                snippet.setText(marker.getSnippet());

                                                info.addView(title);
                                                info.addView(snippet);

                                                return info;
                                        }
                                });
                              /*  if (feature.hasProperty("NOMBRE")) {
                                        String
                                }

                                if (feature.hasProperty("CALLE")) {
                                        String calle = feature.getProperty("CALLE");
                                }
                                if (feature.hasProperty("ALTURA")) {
                                        String altura = feature.getProperty("ALTURA");
                                }

                                if (feature.hasProperty("TELEFONO")) {
                                        String telefono = feature.getProperty("TELEFONO");
                                }

                               */
                        }
                } catch (IOException e) {
                        Log.e(mLogTag, "GeoJSON file could not be read");
                } catch (JSONException e) {
                        Log.e(mLogTag, "GeoJSON file could not be converted to a JSONObject");
                }
        }


private void addGeoJsonLayerToMap(GeoJsonLayer layer) {

                //addColorsToMarkers(layer);
                layer.addLayerToMap();
                // Demonstrate receiving features via GeoJsonLayer clicks.
                layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
                        @Override
                        public void onFeatureClick(Feature feature) {
                                Toast.makeText(getApplicationContext(),
                                        "Feature clicked: " + feature.getProperty("title"),
                                        Toast.LENGTH_SHORT).show();
                        }

                });
        }

        @Override
        public void onBackPressed() {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Aviso")
                        .setMessage("¿Desea volver al menú principal?")
                        .setNegativeButton("NO", null)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                        goToMainMenu();
                                }
                        }).create().show();
        }

        private void goToMainMenu() {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
        }
}