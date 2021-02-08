package com.borido.prctica_mymapas.Clases;

import java.io.Serializable;

public class Localizacion implements Serializable {

    private int id;
    private String titulo;
    private String fragmento;
    private String etiqueta;
    private double latitud;
    private double longitud;

    public Localizacion(int id, String titulo, String fragmento, String etiqueta, double latitud, double longitud) {
        this.id = id;
        this.titulo = titulo;
        this.fragmento = fragmento;
        this.etiqueta = etiqueta;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Localizacion(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFragmento() {
        return fragmento;
    }

    public void setFragmento(String fragmento) {
        this.fragmento = fragmento;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "Localizacion{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", fragmento='" + fragmento + '\'' +
                ", etiqueta='" + etiqueta + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
