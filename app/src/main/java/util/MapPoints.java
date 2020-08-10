package util;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapPoints {

    private ArrayList<Puntos> puntos;

    public MapPoints(){
        puntos = new ArrayList<Puntos>();
        puntos.add(new Puntos(new LatLng(37.416216,-122.088819),"Doctor Esteban",1));
        puntos.add(new Puntos(new LatLng(37.418912,-122.093059),"Doctor Quito",2));
        puntos.add(new Puntos(new LatLng(37.414489,-122.091814),"Doctor Suarez",3));
    }

    public ArrayList<Puntos> getPuntos() {
        return puntos;
    }

    public class Puntos{
        private LatLng ubicacion;
        private String title;
        private Integer tag;

        public Puntos(LatLng ubicacion, String title, Integer tag){
            this.ubicacion = ubicacion;
            this.title = title;
            this.tag = tag;
        }

        public LatLng getUbicacion() {
            return ubicacion;
        }

        public String getTitle() {
            return title;
        }

        public Integer getTag() {
            return tag;
        }
    }

}
