package com.borido.prctica_mymapas.Intefaces;

import com.borido.prctica_mymapas.Clases.Localizacion;

import java.util.List;

public interface Persistencia {
    List<Localizacion> leerLocalizaciones();
}
