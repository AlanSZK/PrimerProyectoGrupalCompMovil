package com.example.primerproyectogrupalcompmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GruposActivity extends AppCompatActivity {

    ListView listaGrupos;
    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        setTitle("Grupos");

        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");


    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_grupos,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_agregar_grupo:
                Intent intent = new Intent(getApplicationContext(),NuevoGrupoActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            default:
                break;


        }
        return super.onOptionsItemSelected(item);
    }




}