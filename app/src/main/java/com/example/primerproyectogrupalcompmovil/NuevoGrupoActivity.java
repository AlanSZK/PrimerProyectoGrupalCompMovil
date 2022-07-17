package com.example.primerproyectogrupalcompmovil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NuevoGrupoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Nuevo grupo");
        setContentView(R.layout.activity_nuevo_grupo);
    }
}