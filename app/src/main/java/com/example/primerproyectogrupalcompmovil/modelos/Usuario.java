package com.example.primerproyectogrupalcompmovil.modelos;

public class Usuario {
    private String docUuid;
    private String authUuid;
    private String nombre;
    private boolean check;

    public Usuario(String docUuid, String authUuid, String nombre) {
        this.docUuid = docUuid;
        this.authUuid = authUuid;
        this.nombre = nombre;
        this.check = false;
    }

    public String getDocUuid() {
        return docUuid;
    }

    public void setDocUuid(String docUuid) {
        this.docUuid = docUuid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
