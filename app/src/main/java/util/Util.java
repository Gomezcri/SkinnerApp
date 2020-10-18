package util;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.skinnerapp.Model.HistoricoResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {
    private static ProgressDialog progress;
    private static String dirIP= "http://192.168.1.20:8080/";
    public static Retrofit getConnection(){

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(dirIP)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        return retrofit;
    }

    public static void showLoadingDialog(Context contexto, String titulo, String mensaje) {

        if (progress == null) {
            progress = new ProgressDialog(contexto);
            progress.setTitle(titulo);
            progress.setMessage(mensaje);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
        }
        progress.show();
    }

    public static void dismissLoadingDialog() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public static String formatDate(String date, String formato) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = format.parse(date);

        format = new SimpleDateFormat(formato);
        return format.format(newDate);

    }
}
