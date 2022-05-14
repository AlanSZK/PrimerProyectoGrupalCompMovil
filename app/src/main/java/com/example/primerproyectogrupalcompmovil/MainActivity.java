package com.example.primerproyectogrupalcompmovil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MainActivity","Botón presionado");
                Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                //Intent intent = new Intent(MainActivity.this,SecondActivity.class); Otra forma de hacer lo mismo

                startActivity(intent);

            }
        });
    }
}