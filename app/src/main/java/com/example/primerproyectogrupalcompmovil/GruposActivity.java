package com.example.primerproyectogrupalcompmovil;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.primerproyectogrupalcompmovil.activities.LoginActivity;
import com.example.primerproyectogrupalcompmovil.activities.UsersActivity;
import com.example.primerproyectogrupalcompmovil.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class GruposActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView myGroupList;
    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadUserDetails();
        getToken();
        setListeners();

        setContentView(R.layout.activity_grupos);

        setTitle("Grupos");

        mAuth = FirebaseAuth.getInstance();

    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
    }

    private void loadUserDetails() {
    }

    private void setListeners() {
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), UsersActivity.class)));
    }


    public static class GroupsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null)
        {
            SendUserToLoginActivity();
        }
    }

    private void SendUserToLoginActivity()
    {

        Intent loginIntent = new Intent(GruposActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();

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
                startActivity(intent);
            default:
                break;


        }
        return super.onOptionsItemSelected(item);
    }

}