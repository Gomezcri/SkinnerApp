package util;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {
    private static ProgressDialog progress;
    public static Retrofit getConnection(){

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.6:8080/")
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
        }
        progress.show();
    }

    public static void dismissLoadingDialog() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }
}
