package com.example.primerproyectogrupalcompmovil;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.primerproyectogrupalcompmovil.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GruposActivity extends AppCompatActivity  {

    RecyclerView rvGrupos;
    String email;
    TextView tvGrupo;
    List<String> grupos;

    AdaptadorGrupo adaptador;

    String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        grupos = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        nombreUsuario = extras.getString("nombreUsuario");
        Toast.makeText(getApplicationContext(),"Bievenido!", Toast.LENGTH_SHORT);
        setTitle("Grupos de "+nombreUsuario);

        setContentView(R.layout.activity_grupos);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ActionBarColor)));

        grupos = obtenerListaGrupos();
        rvGrupos = findViewById(R.id.rvGrupos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvGrupos.setLayoutManager(linearLayoutManager);
        adaptador = new AdaptadorGrupo();

        adaptador.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String nombreGrupo = grupos.get(rvGrupos.getChildAdapterPosition(view));
                Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("nombreGrupo",nombreGrupo);
                intent.putExtra("nombreUsuario",nombreUsuario);
                startActivity(intent);

            }
        });

        rvGrupos.setAdapter(adaptador);


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

    private class AdaptadorGrupo extends RecyclerView.Adapter<AdaptadorGrupo.AdaptadorGrupoHolder> implements View.OnClickListener {

        private View.OnClickListener listener;


        @NonNull
        @Override
        public AdaptadorGrupo.AdaptadorGrupoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            View view = getLayoutInflater().inflate(R.layout.layout_grupo,parent,false);
            view.setOnClickListener(this);

            return new AdaptadorGrupoHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorGrupo.AdaptadorGrupoHolder holder, int position) {
            holder.mostrarGrupos(position);
        }

        @Override
        public int getItemCount() {
            return grupos.size();
        }

        public void setOnClickListener(View.OnClickListener listener){
            this.listener = listener;
        }

        @Override
        public void onClick(View view){
            if(listener!=null){
                listener.onClick(view);
            }
        }

        public class AdaptadorGrupoHolder extends RecyclerView.ViewHolder {
            public AdaptadorGrupoHolder(@NonNull View itemView) {
                super(itemView);
                tvGrupo = itemView.findViewById(R.id.tvGrupo);
            }

            public void mostrarGrupos(int position) {
                tvGrupo.setText(grupos.get(position));

            }
        }
    }

    public List<String> obtenerListaGrupos()
    {
        List<String> lista = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("grupo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                               if(doc.get("admin").equals(email)){
                                   lista.add(doc.get("nombre").toString());
                               }else {
                                   List<String> miembros = (List<String>) doc.get("miembros");
                                   for(String s : miembros){
                                       if(s.equals(email)){
                                           lista.add(doc.get("nombre").toString());
                                       }
                                   }
                                }
                            }
                            adaptador.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error obteniendo documentos ", task.getException());
                        }

                    }
                });
        return lista;

    }
}