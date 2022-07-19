package com.example.primerproyectogrupalcompmovil;

import static com.example.primerproyectogrupalcompmovil.LoginActivity.authUuid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.primerproyectogrupalcompmovil.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NuevoGrupoActivity extends AppCompatActivity {

    EditText etNombreGrupo;


    TextView tvNombrePersona;
    CheckBox checkbox;

    List<Usuario> usuarios;
    RecyclerView rvPersonas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Nuevo grupo");

        usuarios=new ArrayList<>();

       //usuarios = obtenerListaUsuarios(LoginActivity.authUuid);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuario")
                .whereNotEqualTo("authUuid",authUuid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Usuario usr = new Usuario(
                                        doc.getId(),
                                        doc.getString("authUuid"),
                                        doc.getString("nombre")
                                );
                                System.out.println(doc.getId());
                                usuarios.add(usr);
                            }
                        } else {
                            Log.d("TAG", "Error obteniendo documentos ", task.getException());
                        }

                    }
                });

       // imprimirLista(usuarios);


        setContentView(R.layout.activity_nuevo_grupo);

        etNombreGrupo = findViewById(R.id.etNombreGrupo);
        RecyclerView rvPersonas =findViewById(R.id.rvPersonas);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPersonas.setLayoutManager(linearLayoutManager);
        rvPersonas.setAdapter(new AdaptadorPersonas());







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
    public List<Usuario> obtenerListaUsuarios(String usuarioAdminUuid)
    {
        List<Usuario> lista = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuario")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Usuario usr = new Usuario(
                                        doc.getId(),
                                        doc.get("authUuid").toString(),
                                        doc.get("nombre").toString()
                                );
                                lista.add(usr);
                            }
                        } else {
                            Log.d("TAG", "Error obteniendo documentos ", task.getException());
                        }

                    }
                });
                return lista;

    }


    public void crearGrupo (View view){


    }

    public void imprimirLista (List<Usuario> lista)
    {
        for (Usuario usr : lista)
        {
            Log.d("Lista",usr.getNombre());
        }
    }


}