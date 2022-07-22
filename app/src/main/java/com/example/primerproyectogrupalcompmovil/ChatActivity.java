package com.example.primerproyectogrupalcompmovil;



import android.graphics.drawable.ColorDrawable;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.primerproyectogrupalcompmovil.modelos.Mensaje;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {

    private final int TOMA_FOTO = 1;

    private final int MSG_TXT=1;
    private final int MSG_IMG=2;

    RecyclerView rvChat;
    AdaptadorChat adaptador;
    List<Mensaje> listaMensajes = new ArrayList<>();

    TextView tvNombre;
    TextView tvFecha;
    TextView tvTexto;
    ImageView ivImagen;
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

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ActionBarColor)));


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

                                   if(doc.get("imagen")!=null)
                                   {
                                       listaMensajes.add(new Mensaje(doc.getId(), doc.get("nombreUsuario").toString(),null,doc.get("imagen").toString()));
                                       adaptador.notifyDataSetChanged();
                                   }
                                   else{
                                       listaMensajes.add(new Mensaje(doc.getId(), doc.get("nombreUsuario").toString(),doc.get("texto").toString(), null));
                                       adaptador.notifyDataSetChanged();
                                   }




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
    public void enviarImagen(Bitmap bitmap)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        SimpleDateFormat formatterFechaImagen = new SimpleDateFormat("ddMMyyyy-HHmmss");
        Date date = new Date();
        String fechaImagen = formatterFechaImagen.format(date);


        StorageReference refImagen = storageRef.child(fechaImagen+".jpg");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] data = baos.toByteArray();

        UploadTask uploadTask = refImagen.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Map<String, Object> map = new HashMap<>();

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                String fecha = formatter.format(date);

                map.put("fecha", fecha);
                map.put("nombreUsuario", nombreUsuario);
                map.put("texto", "");
                map.put("imagen",refImagen.toString());
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
        });

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

    public void tomarFoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TOMA_FOTO);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==TOMA_FOTO && resultCode==RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");

            enviarImagen(bitmap);

            //ivMostrar.setImageBitmap(bitmap1);

            /*
            try{
                String fecha = new SimpleDateFormat("yyyyMMss").format(new Date());
                fecha+=".jpg";
                FileOutputStream fos = openFileOutput(fecha, Context.MODE_PRIVATE);
                bitmap1.compress(Bitmap.CompressFormat.JPEG,100,fos);
                fos.close();
            }catch (Exception e){

            }
            */

        }
    }

    private class AdaptadorChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



        @Override
        public int getItemViewType(int position) {

            if(listaMensajes.get(position).getUrl()==null){
                return MSG_TXT;
            }else{
                return MSG_IMG;
            }

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

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view;

            if (viewType == MSG_TXT) {
                view = getLayoutInflater().inflate(R.layout.layout_mensaje,parent,false);
                return new AdaptadorChatHolder(view);

            } else {
                view = getLayoutInflater().inflate(R.layout.layout_imagen,parent,false);
                return new AdaptadorChatImgHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


            if (getItemViewType(position) == MSG_TXT) {
                ((AdaptadorChatHolder) holder).mostrarChat(position);
            } else {
                ((AdaptadorChatImgHolder) holder).mostrarChatImagen(position);
            }
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
        public class AdaptadorChatImgHolder extends RecyclerView.ViewHolder {


            public AdaptadorChatImgHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvNombre);
                tvFecha = itemView.findViewById(R.id.tvFecha);
                ivImagen = itemView.findViewById(R.id.ivImagen);

            }

            public void mostrarChatImagen(int position) {

                tvNombre.setText(listaMensajes.get(position).getNombre());
                tvFecha.setText(listaMensajes.get(position).getFecha());

                String url = listaMensajes.get(position).getUrl();
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(listaMensajes.get(position).getUrl());


                Glide.with(itemView.getContext())
                        .load(storageReference)
                        .into(ivImagen);

            }
        }




}
