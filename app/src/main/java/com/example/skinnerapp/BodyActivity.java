package com.example.skinnerapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class BodyActivity extends AppCompatActivity {

    private ImageView frontImage;
    private ImageView backImage;
    //CODIGO DE COLORES CUERPO FRENTE
    private int FRONT_ROSTRO = -16118205;
    private int FRONT_CUELLO = -7536645;
    private int FRONT_HOMBRO_IZQ = -15806139;
    private int FRONT_HOMBRO_DER = -3866866;
    private int FRONT_PECHO = -16733965;
    private int FRONT_ANTEBRAZO_IZQ = -3584;
    private int FRONT_ANTEBRAZO_DER = -136026;
    private int FRONT_BRAZO_IZQ = -13800;
    private int FRONT_BRAZO_DER = -32985;
    private int FRONT_MANO_IZQ = -7864293;
    private int FRONT_MANO_DER = -1303516;
    private int FRONT_MUSLO_IZQ = -20792;
    private int FRONT_MUSLO_DER = -6125549;
    private int FRONT_PIERNA_IZQ = -4621738;
    private int FRONT_PIERNA_DER = -2136731;
    private int FRONT_PIE_IZQ = -5240020;
    private int FRONT_PIE_DER = -10746876;
    private int FRONT_ABDOMEN = -12629812;
    private int FRONT_CINTURA = -4702790;
    //CODIGO DE COLORES CUERPO DETRAS
    private int BACK_NUNCA = -7864293;
    private int BACK_HOMBRO_IZQ = -13800;
    private int BACK_HOMBRO_DER = -32985;
    private int BACK_ESPALDA = -1303516;
    private int BACK_ANTEBRAZO_IZQ =-136026;
    private int BACK_ANTEBRAZO_DER = -3584;
    private int BACK_BRAZO_IZQ = -3866866;
    private int BACK_BRAZO_DER = -15806139;
    private int BACK_CINTURA = -12629812;
    private int BACK_MANO_IZQ = -7536645;
    private int BACK_MANO_DER = -16733965;
    private int BACK_MUSLO_IZQ = -4702790;
    private int BACK_MUSLO_DER = -20792;
    private int BACK_PANTORRILLA_IZQ = -4621738;
    private int BACK_PANTORRILLA_DER = -6125549;
    private int BACK_TALON_IZQ = -2136731;
    private int BACK_TALON_DER = -10746876;

    private Map<Integer,String> front_map;
    private Map<Integer,String> back_map;
    private String parte = null;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;


    private String body_part=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);
        tv1 = (TextView) findViewById(R.id.tv_SeleccioneUbicacion);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv1.setPaintFlags(tv1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv2.setPaintFlags(tv1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv3.setPaintFlags(tv1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        iniciarMapping();

        frontImage = (ImageView) findViewById (R.id.front);
        if (frontImage != null) {
            frontImage.setOnTouchListener (new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return onTouchListener(v,event,"frente");
                }
            });
        }

        backImage = (ImageView) findViewById (R.id.back);
        if (backImage != null) {
            backImage.setOnTouchListener (new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return onTouchListener(v,event,"detras");
                }
            });
        }

    }

    private void iniciarMapping() {
        front_map = new HashMap<Integer,String>();
        back_map = new HashMap<Integer,String>();
        //MAPPING FRONT
        front_map.put(FRONT_ROSTRO,"Rostro");
        front_map.put(FRONT_CUELLO ,"Cuello");
        front_map.put(FRONT_HOMBRO_IZQ ,"Hombro Derecho");
        front_map.put(FRONT_HOMBRO_DER, "Hombro Izquierdo");
        front_map.put(FRONT_PECHO, "Pecho");
        front_map.put(FRONT_ANTEBRAZO_IZQ ,"Antebrazo Derecho");
        front_map.put(FRONT_ANTEBRAZO_DER ,"Antebrazo Izquierdo");
        front_map.put(FRONT_BRAZO_IZQ ,"Brazo Derecho");
        front_map.put(FRONT_BRAZO_DER ,"Brazo Izquierdo");
        front_map.put(FRONT_MANO_IZQ ,"Mano Derecha");
        front_map.put(FRONT_MANO_DER ,"Mano Izquierda");
        front_map.put(FRONT_MUSLO_IZQ ,"Muslo Derecho");
        front_map.put(FRONT_MUSLO_DER ,"Muslo Izquierdo");
        front_map.put(FRONT_PIERNA_IZQ ,"Pierna Derecha");
        front_map.put(FRONT_PIERNA_DER ,"Pierna Izquierda");
        front_map.put(FRONT_PIE_IZQ ,"Pie Derecho");
        front_map.put(FRONT_PIE_DER ,"Pie Izquierdo");
        front_map.put(FRONT_ABDOMEN ,"Abdomen");
        front_map.put(FRONT_CINTURA ,"Cintura");

        //MAPPING BACK
        back_map.put(BACK_NUNCA, "Nuca");
        back_map.put(BACK_HOMBRO_IZQ ,"Hombro Izquierdo");
        back_map.put(BACK_HOMBRO_DER ,"Hombro Derecho");
        back_map.put(BACK_ESPALDA ,"Espalda");
        back_map.put(BACK_ANTEBRAZO_IZQ ,"Antebrazo Izquierdo");
        back_map.put(BACK_ANTEBRAZO_DER ,"Antebrazo Derecho");
        back_map.put(BACK_BRAZO_IZQ ,"Brazo Izquierdo");
        back_map.put(BACK_BRAZO_DER ,"Brazo Derecho");
        back_map.put(BACK_CINTURA ,"Cintura");
        back_map.put(BACK_MANO_IZQ ,"Mano Izquierda");
        back_map.put(BACK_MANO_DER ,"Mano Derecha");
        back_map.put(BACK_MUSLO_IZQ ,"Muslo Izquierdo");
        back_map.put(BACK_MUSLO_DER ,"Muslo Derecho");
        back_map.put(BACK_PANTORRILLA_IZQ ,"Pantorrilla Izquierda");
        back_map.put(BACK_PANTORRILLA_DER ,"Pantorrilla Derecha");
        back_map.put(BACK_TALON_IZQ ,"Talón Izquierdo");
        back_map.put(BACK_TALON_DER ,"Talón Derecho");
    }

    public boolean onTouchListener(View v, MotionEvent ev,String selected) {
        boolean handledHere = false;
        int touchColor;
        final int action = ev.getAction();

        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN :
                handledHere = true;
                break;

            case MotionEvent.ACTION_UP :

                if(selected == "frente")
                {
                    touchColor = getHotspotColor (R.id.front_mask, evX, evY);
                    if(front_map.containsKey(touchColor)) {
                        body_part = front_map.get(touchColor);
                        parte = "frontal";
                    }

                }
                else
                {
                    touchColor = getHotspotColor (R.id.back_mask, evX, evY);
                    if(back_map.containsKey(touchColor)) {
                        body_part = back_map.get(touchColor);
                        parte = "trasera";
                    }
                }

                handledHere = true;
                if(body_part!= null)
                {
                    showConfirmationDialog();
                }
                break;

            default:
                handledHere = false;
        }

        return handledHere;
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmación");
        builder.setMessage("La parte del cuerpo seleccionada fue "+ body_part +" ¿Desea proseguir tomando una foto?");
        builder.setPositiveButton("Confirmar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("bodyPart",body_part);
                        resultIntent.putExtra("section",parte);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                body_part=null;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById (hotspotId);
        img.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
        img.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }

    public boolean closeMatch (int color1, int color2, int tolerance) {
        if ((int) Math.abs (color1 - color2) > tolerance ) return false;
        return true;

    }

}
