package com.example.primerproyectogrupalcompmovil.modelos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mensaje {

    private String nombre;
    private String fecha;
    private String texto;

    public Mensaje(String nombre, String texto) {
        this.nombre = nombre;
        this.texto = texto;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.fecha = formatter.format(date);

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

    @Override
    public String toString() {
        return "mensaje{" +
                "nombre='" + nombre + '\'' +
                ", fecha='" + fecha + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }
}
