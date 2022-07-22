package com.example.primerproyectogrupalcompmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.primerproyectogrupalcompmovil.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NuevoGrupoActivity extends AppCompatActivity {

    EditText etNombreGrupo;
    TextView tvNombrePersona;

    List<Usuario> usuarios;
    List<String> listaCheck;
    RecyclerView rvPersonas;


    String email;
    boolean verificacionGrupo=true;

    AdaptadorPersonas adaptador = new AdaptadorPersonas();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Nuevo grupo");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ActionBarColor)));
        usuarios = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        usuarios = obtenerListaUsuarios(email);

        setContentView(R.layout.activity_nuevo_grupo);
        RecyclerView rvPersonas =findViewById(R.id.rvPersonas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPersonas.setLayoutManager(linearLayoutManager);
        rvPersonas.setAdapter(adaptador);



    }


    private class AdaptadorPersonas extends RecyclerView.Adapter<AdaptadorPersonas.AdaptadorPersonasHolder>{


        @NonNull
        @Override
        public AdaptadorPersonas.AdaptadorPersonasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorPersonasHolder(getLayoutInflater().inflate(R.layout.layout_checkbox,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorPersonas.AdaptadorPersonasHolder holder, int position) {
            holder.insertarNombre(position);

            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.checkbox.isChecked()){
                        usuarios.get(holder.getBindingAdapterPosition()).setCheck(true);
                    }else{
                        usuarios.get(holder.getBindingAdapterPosition()).setCheck(false);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            if (usuarios != null){
                return usuarios.size();
            }
            else{
                return 0;
            }
        }


        public class AdaptadorPersonasHolder extends RecyclerView.ViewHolder{
            CheckBox checkbox;

            public AdaptadorPersonasHolder(@NonNull View itemView) {
                super(itemView);
                checkbox = itemView.findViewById(R.id.checkbox);
                tvNombrePersona = itemView.findViewById(R.id.tvNombrePersona);


            }

            public void insertarNombre(int position)
            {
                tvNombrePersona.setText(usuarios.get(position).getNombre());
            }



        }

    }

    public List<Usuario> obtenerListaUsuarios(String email)
    {
        List<Usuario> lista = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuario")
                .whereNotEqualTo("email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Usuario usr = new Usuario(
                                        doc.getId(),
                                        doc.get("authUuid").toString(),
                                        doc.get("nombre").toString(),
                                        doc.get("email").toString()
                                );
                                lista.add(usr);
                            }
                            adaptador.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error obteniendo documentos ", task.getException());
                        }

                    }
                });
                return lista;

    }


    public boolean verificarCheck(List<Usuario> personas)
    {
        for(Usuario u : personas){
            if(u.isCheck()){
                return true;
            }
        }

        return false;
    }
    public void verificarGrupo(String grupo)
    {
        //Verificar que grupo tenga nombre único
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("grupo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                if(doc.get("nombre").equals(grupo)){
                                    verificacionGrupo=false;
                                }
                            }
                        }
                    }
                });



    }

    public void crearGrupo (View view){
        etNombreGrupo = findViewById(R.id.etNombreGrupo);


        if(!etNombreGrupo.getText().toString().isEmpty()&&verificarCheck(usuarios))
        {

            verificarGrupo(etNombreGrupo.getText().toString());
            if(verificacionGrupo==true){
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map map = new HashMap<>();
                map.put("nombre",etNombreGrupo.getText().toString());
                map.put("admin",email);

                listaCheck = new ArrayList<>();
                for(Usuario u : usuarios)
                {
                    if(u.isCheck())
                    {
                        listaCheck.add(u.getEmail());
                    }

                }
                map.put("miembros", listaCheck);


                db.collection("grupo")
                        .add(map)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"Grupo añadido con éxito", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        });

            }
            else{
                Toast.makeText(getApplicationContext(),"El nombre de grupo ya está en uso", Toast.LENGTH_LONG).show();
                verificacionGrupo=true;
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Se debe elegir un nombre de grupo y al menos a una persona", Toast.LENGTH_SHORT).show();
        }

    }




}