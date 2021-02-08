package com.borido.prctica_mymapas.modelos;

import android.content.Context;

import com.borido.prctica_mymapas.Clases.Localizacion;
import com.borido.prctica_mymapas.Intefaces.Persistencia;
import com.borido.prctica_mymapas.SQLITE.SQLManager;

import java.util.ArrayList;
import java.util.List;

public class GestionLocalidadesSQLITE implements Persistencia {

    private Context context;

    public GestionLocalidadesSQLITE(Context context) {
        this.context = context;
    }

    @Override
    public List<Localizacion> leerLocalizaciones() {
        List<Localizacion> localizaciones = new ArrayList<Localizacion>();
        SQLManager sQLManager = new SQLManager(context);
        localizaciones = new ArrayList<Localizacion>();
        localizaciones = (ArrayList<Localizacion>) sQLManager.selectAll();
        return localizaciones;
    }
}
