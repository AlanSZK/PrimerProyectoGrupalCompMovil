package com.example.primerproyectogrupalcompmovil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.primerproyectogrupalcompmovil.modelos.Mensaje;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE = 1;
    RecyclerView rvChat;
    AdaptadorChat adaptador;
    List<Mensaje> listaMensajes = new ArrayList<>();

    TextView tvNombre;
    TextView tvFecha;
    TextView tvTexto;
    EditText etInput;

    String nombreGrupo;
    String nombreUsuario;
    String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adaptador = new AdaptadorChat();

        setContentView(R.layout.activity_chat);
        rvChat = findViewById(R.id.rvChat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvChat.setLayoutManager(linearLayoutManager);
        rvChat.setAdapter(adaptador);

        Bundle extras = getIntent().getExtras();
        nombreGrupo = extras.getString("nombreGrupo");
        nombreUsuario = extras.getString("nombreUsuario");

        setTitle(nombreGrupo);

        obtenerUuidGrupo(nombreGrupo);



    }

    private void obtenerUuidGrupo(String nombreGrupo) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("grupo")
                .whereEqualTo("nombre",nombreGrupo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                            uuid = doc.getId();
                            //Se obtiene la uuid y se procede a cargar el chat
                            mostrarChat(db);

                        }
                    }
                });




    }

    public void mostrarChat(FirebaseFirestore db) {

        db.collection("grupo")
                .document(uuid)
                .collection("chat")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.w("TAG","Listen fallido",error);
                            return;
                        }

                        for(DocumentChange dc : snapshots.getDocumentChanges()){
                            switch(dc.getType()){

                                case ADDED:
                                   DocumentSnapshot doc = dc.getDocument();
                                   listaMensajes.add(new Mensaje(doc.getId(), doc.get("nombreUsuario").toString(),doc.get("texto").toString()));
                                   adaptador.notifyDataSetChanged();



                            }
                        }

                    }
                });

    }

    public void enviarMensaje(View view) {

        etInput = findViewById(R.id.etInput);

        if(!etInput.getText().toString().isEmpty()) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();


            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String fecha = formatter.format(date);

            Map<String, Object> map = new HashMap<>();
            map.put("fecha", fecha);
            map.put("nombreUsuario", nombreUsuario);
            map.put("texto", etInput.getText().toString());

            db.collection("grupo")
                    .document(uuid)
                    .collection("chat")
                    .add(map)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("TAG", "DocumentSnapshot añadido con ID: " + documentReference.getId());
                        }
                    });
        }
    }
    public int buscarMensajePorUuid(String uuid,List<Mensaje>lista)
    {
        for(int i=0;i<lista.size();i++)
        {
            if(lista.get(i).getDocId().equals(uuid))
            {
                return i;
            }
        }
        return -1;
    }


    private class AdaptadorChat extends RecyclerView.Adapter<AdaptadorChat.AdaptadorChatHolder> {



        @NonNull
        @Override
        public AdaptadorChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new AdaptadorChatHolder(getLayoutInflater().inflate(R.layout.layout_mensaje,parent,false));


        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorChatHolder holder, int position) {
            holder.mostrarChat(position);

        }

        @Override
        public int getItemCount() {
            if (listaMensajes != null){
                return listaMensajes.size();
            }
            else{
                return 0;
            }


        }



        public class AdaptadorChatHolder extends RecyclerView.ViewHolder {


            public AdaptadorChatHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvNombre);
                tvFecha = itemView.findViewById(R.id.tvFecha);
                tvTexto = itemView.findViewById(R.id.tvTexto);

            }

            public void mostrarChat(int position) {

                tvNombre.setText(listaMensajes.get(position).getNombre());
                tvFecha.setText(listaMensajes.get(position).getFecha());
                tvTexto.setText(listaMensajes.get(position).getTexto());
            }
        }
    }



}
