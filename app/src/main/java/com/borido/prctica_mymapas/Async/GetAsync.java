package com.borido.prctica_mymapas.Async;

import android.os.AsyncTask;
import android.util.Log;

import com.borido.prctica_mymapas.Clases.Localizacion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetAsync extends AsyncTask<String,String, String> {

    IAsyncGet iAsyncGet;
    String url;
    @Override
    protected String doInBackground(String... urls) {
        return Utilidades.ObtenerDatos(urls[0]);
    }

    public GetAsync(IAsyncGet iAsyncGet) {
        super();
        this.iAsyncGet = iAsyncGet;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject root = new JSONObject(s);

            JSONObject coordenadas = root.getJSONObject("coord");
            String longitud = coordenadas.getString("lon");
            String latitud = coordenadas.getString("lat");
            String titulo = root.getString("name");

            Localizacion localizacion = new Localizacion();
            localizacion.setId((int) Math.floor(Math.random()*(0-100000)+100000));
            localizacion.setTitulo(titulo);
            localizacion.setEtiqueta("ciudad");
            localizacion.setFragmento(titulo.toLowerCase()+"@gmail.com");
            localizacion.setLongitud(Double.parseDouble(longitud));
            localizacion.setLatitud(Double.parseDouble(latitud));

            iAsyncGet.onFinish(localizacion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

    }

    public interface IAsyncGet{
        public void onFinish(Localizacion localizacion);
    }
}
