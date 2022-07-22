package com.example.primerproyectogrupalcompmovil.modelos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mensaje {


    private String docId;
    private String nombre;
    private String fecha;
    private String texto;



    private String url;



    public Mensaje(String docId, String nombre, String texto, String url) {
        this.docId = docId;
        this.nombre = nombre;
        this.texto = texto;
        this.url = url;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.fecha = formatter.format(date);

    }


    public String getDocId() {
        return docId;
    }
    public void setDocId(String docId) {
        this.docId = docId;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public String toString() {
        return "mensaje{" +
                "nombre='" + nombre + '\'' +
                ", fecha='" + fecha + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }
}
