package com.borido.prctica_mymapas.SQLITE;

import android.provider.BaseColumns;

public class LocalizacionContract {

        private LocalizacionContract(){}

        public static class LocalizacionInfo implements BaseColumns {
            public static final String TABLE_NAME = "localizacion";
            public static final String COLUMN_NAME_TITULO = "titulo";
            public static final String COLUMN_NAME_FRAGMENTO = "fragmento";
            public static final String COLUMN_NAME_ETIQUETA = "etiqueta";
            public static final String COLUMN_NAME_LATITUD = "latitud";
            public static final String COLUMN_NAME_LONGITUD = "longitud";
        }

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + LocalizacionInfo.TABLE_NAME + " (" +
                        LocalizacionInfo._ID + " INTEGER PRIMARY KEY," +
                        LocalizacionInfo.COLUMN_NAME_TITULO + " TEXT," +
                        LocalizacionInfo.COLUMN_NAME_FRAGMENTO + " TEXT," +
                        LocalizacionInfo.COLUMN_NAME_ETIQUETA + " TEXT," +
                        LocalizacionInfo.COLUMN_NAME_LATITUD + " REAL," +
                        LocalizacionInfo.COLUMN_NAME_LONGITUD + " REAL)";

        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + LocalizacionInfo.TABLE_NAME;
}


