package com.borido.prctica_mymapas.SQLITE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.borido.prctica_mymapas.Clases.Localizacion;

import java.util.ArrayList;
import java.util.List;

public class SQLManager {

    public static LocalizacionDBHelper productoDBHelper;
    Context c;

    public SQLManager(Context c) {
        this.c = c;
    }

    public LocalizacionDBHelper getInstance(){
        if(productoDBHelper == null){
            productoDBHelper = new LocalizacionDBHelper(c);
        }
        return productoDBHelper;
    }
    public long insert(Localizacion l){
        LocalizacionDBHelper dbHelper = getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_TITULO, l.getTitulo());
        values.put(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_FRAGMENTO, l.getFragmento());
        values.put(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_ETIQUETA, l.getEtiqueta());
        values.put(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_LATITUD, l.getLatitud());
        values.put(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_LONGITUD, l.getLongitud());

        long newRowId = db.insert(LocalizacionContract.LocalizacionInfo.TABLE_NAME, null, values);
        return newRowId;
    }

    public List<Localizacion> selectAll(){
        LocalizacionDBHelper dbHelper = getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                LocalizacionContract.LocalizacionInfo.COLUMN_NAME_TITULO,
                LocalizacionContract.LocalizacionInfo.COLUMN_NAME_FRAGMENTO,
                LocalizacionContract.LocalizacionInfo.COLUMN_NAME_ETIQUETA,
                LocalizacionContract.LocalizacionInfo.COLUMN_NAME_LATITUD,
                LocalizacionContract.LocalizacionInfo.COLUMN_NAME_LONGITUD
        };

        String sortOrder = LocalizacionContract.LocalizacionInfo.COLUMN_NAME_TITULO + " ASC";

        Cursor cursor = db.query(
                LocalizacionContract.LocalizacionInfo.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        List<Localizacion> localizacions = new ArrayList<Localizacion>();
        while(cursor.moveToNext()) {
            int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo._ID));
            String titulo = cursor.getString(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_TITULO));
            String fragmento = cursor.getString(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_FRAGMENTO));
            String etiqueta = cursor.getString(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_ETIQUETA));
            double latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_LATITUD));
            double longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_LONGITUD));
            localizacions.add(new Localizacion(itemId, titulo, fragmento, etiqueta, latitud, longitud));
        }
        cursor.close();

        return localizacions;
    }

    public List<Localizacion> selectByName(String name){
        LocalizacionDBHelper dbHelper = getInstance();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                LocalizacionContract.LocalizacionInfo.COLUMN_NAME_TITULO,
                LocalizacionContract.LocalizacionInfo.COLUMN_NAME_FRAGMENTO,
                LocalizacionContract.LocalizacionInfo.COLUMN_NAME_ETIQUETA,
                LocalizacionContract.LocalizacionInfo.COLUMN_NAME_LATITUD,
                LocalizacionContract.LocalizacionInfo.COLUMN_NAME_LONGITUD
        };

        String selection = LocalizacionContract.LocalizacionInfo.COLUMN_NAME_TITULO + " = ?";
        String[] selectionArgs = { name };

        String sortOrder = LocalizacionContract.LocalizacionInfo.COLUMN_NAME_TITULO + " ASC";

        Cursor cursor = db.query(
                LocalizacionContract.LocalizacionInfo.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        List<Localizacion> localizacions = new ArrayList<Localizacion>();
        while(cursor.moveToNext()) {
            int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo._ID));
            String titulo = cursor.getString(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_TITULO));
            String fragmento = cursor.getString(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_FRAGMENTO));
            String etiqueta = cursor.getString(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_ETIQUETA));
            double latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_LATITUD));
            double longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_LONGITUD));
            localizacions.add(new Localizacion(itemId, titulo, fragmento, etiqueta, latitud, longitud));
        }
        cursor.close();

        return localizacions;
    }

    public int deleteById(long id){
        LocalizacionDBHelper dbHelper = getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = LocalizacionContract.LocalizacionInfo._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        return db.delete(LocalizacionContract.LocalizacionInfo.TABLE_NAME, selection, selectionArgs);
    }

    public int updateNameById(long id, String newName){
        LocalizacionDBHelper dbHelper = getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocalizacionContract.LocalizacionInfo.COLUMN_NAME_TITULO, newName);

        String selection = LocalizacionContract.LocalizacionInfo._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        return db.update(
                LocalizacionContract.LocalizacionInfo.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void cerrar(){
        LocalizacionDBHelper dbHelper = getInstance();
        dbHelper.close();
    }
}


