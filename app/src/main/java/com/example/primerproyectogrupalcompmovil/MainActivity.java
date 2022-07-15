package com.example.primerproyectogrupalcompmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.primerproyectogrupalcompmovil.modelos.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvChat;
    List<Mensaje> listaMensajes = new ArrayList<>();

    TextView tvNombre;
    TextView tvFecha;
    TextView tvTexto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        listaMensajes.add(new Mensaje("Cris","Hola Gente"));
        listaMensajes.add(new Mensaje("Javier","Wena xd"));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTitle("Wasapp");



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
