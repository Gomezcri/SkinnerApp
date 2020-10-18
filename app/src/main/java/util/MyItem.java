package util;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private String mId_doctor;
    private String mId_lugar;

    public String getmId_doctor() {
        return mId_doctor;
    }

    public String getmId_lugar() {
        return mId_lugar;
    }

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(double lat, double lng, String title, String snippet,String id_doctor, String id_lugar) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mId_doctor = id_doctor;
        mId_lugar = id_lugar;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
