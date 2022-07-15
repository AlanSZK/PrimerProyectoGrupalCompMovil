package com.example.primerproyectogrupalcompmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.primerproyectogrupalcompmovil.modelos.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE = 1;
    RecyclerView rvChat;
    List<Mensaje> listaMensajes = new ArrayList<>();

    TextView tvNombre;
    TextView tvFecha;
    TextView tvTexto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Wasapp");

        mostrarChat();

        /*
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Empezar actividad de Login
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        }
        else {
            // Usuario Logueado, saludo
            Toast.makeText(this,
                    "Bienvenido " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            // Mostrar chat
            mostrarChat();
        }

        */
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                mostrarChat();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }

    }
    */

    private void mostrarChat() {

        listaMensajes.add(new Mensaje("Cris","Hola Gente"));
        listaMensajes.add(new Mensaje("Javier","Wena xd"));

        setContentView(R.layout.activity_chat);


        rvChat = findViewById(R.id.rvChat);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvChat.setLayoutManager(linearLayoutManager);
        rvChat.setAdapter(new AdaptadorChat());




    }

    private class AdaptadorChat extends RecyclerView.Adapter<AdaptadorChat.AdaptadorChatHolder>{

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
