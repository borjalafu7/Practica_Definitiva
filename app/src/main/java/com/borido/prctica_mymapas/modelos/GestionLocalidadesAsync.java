package com.borido.prctica_mymapas.modelos;

import android.content.Context;
import android.util.Log;

import com.borido.prctica_mymapas.Async.GetAsync;
import com.borido.prctica_mymapas.Clases.Localizacion;
import com.borido.prctica_mymapas.Intefaces.Persistencia;
import java.util.ArrayList;
import java.util.List;

public class GestionLocalidadesAsync implements Persistencia {

    private final static String[]  API_URL = new String[]{
            "https://api.openweathermap.org/data/2.5/weather?q=Torrent,es&APPID=afe65bb24deaa16640c55f532603c7c6",
            "https://api.openweathermap.org/data/2.5/weather?q=Picanya,es&APPID=afe65bb24deaa16640c55f532603c7c6",
            "https://api.openweathermap.org/data/2.5/weather?q=Alaquas,es&APPID=afe65bb24deaa16640c55f532603c7c6",
            "https://api.openweathermap.org/data/2.5/weather?q=Paiporta,es&APPID=afe65bb24deaa16640c55f532603c7c6",
            "https://api.openweathermap.org/data/2.5/weather?q=Aldaia,es&APPID=afe65bb24deaa16640c55f532603c7c6",
            };

    @Override
    public List<Localizacion> leerLocalizaciones() {
        List<Localizacion> localizaciones = new ArrayList<Localizacion>();
        for(int i=0;i<API_URL.length;i++){
            new GetAsync(new GetAsync.IAsyncGet() {
                @Override
                public void onFinish(Localizacion localizacion) {
                    localizaciones.add(localizacion);
                }
            }).execute(API_URL[i]);
        }
        return localizaciones;
    }
}
