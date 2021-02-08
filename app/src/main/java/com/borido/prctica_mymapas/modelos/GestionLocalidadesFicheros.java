package com.borido.prctica_mymapas.modelos;

import android.content.Context;
import android.util.Log;

import com.borido.prctica_mymapas.Clases.Localizacion;
import com.borido.prctica_mymapas.Intefaces.Persistencia;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class GestionLocalidadesFicheros implements Persistencia {

    private String filename = "misLocalizaciones.dat";

    private Context context;

    public GestionLocalidadesFicheros(Context context) {
        this.context = context;
    }

    @Override
    public List<Localizacion> leerLocalizaciones() {
        List<Localizacion> localizaciones = new ArrayList<Localizacion>();
        File appFilesDirectory = context.getFilesDir();
        ObjectInputStream ois = null;
        try {
            FileInputStream fis = new FileInputStream(new File(appFilesDirectory, filename));
            ois = new ObjectInputStream(fis);
            localizaciones = (ArrayList<Localizacion>) ois.readObject();
            ois.close();
        } catch (IOException ex) {
            Log.i("info","Fallo al cargar el fichero");
            Log.i("info", String.valueOf(ex));;
        } finally {
            return localizaciones;
        }
    }
}
