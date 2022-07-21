package com.example.primerproyectogrupalcompmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    EditText etEmailLogin,etContrasenaLogin;

    public static FirebaseAuth auth;

    String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void aRegistrar(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void ingresar(View view) {
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etContrasenaLogin = findViewById(R.id.etContrasenaLogin);



        if(!etEmailLogin.getText().toString().isEmpty()&&!etContrasenaLogin.getText().toString().isEmpty()){
            auth = FirebaseAuth.getInstance();

            auth.getInstance().signInWithEmailAndPassword(etEmailLogin.getText().toString(),etContrasenaLogin.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser user = auth.getCurrentUser();

                        String uuid = user.getUid();

                        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);

                        obtenerNombreUsuario(etEmailLogin.getText().toString());

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Email o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
            Toast.makeText(this,
                    "Email o Contraseña incorrectos",
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void obtenerNombreUsuario(String email)
    {


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuario")
                .whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot doc : task.getResult()){
                            nombreUsuario = doc.get("nombre").toString();

                            Intent intent = new Intent(getApplicationContext(),GruposActivity.class);
                            intent.putExtra("email",etEmailLogin.getText().toString());
                            intent.putExtra("nombreUsuario",nombreUsuario);
                            startActivity(intent);

                        }

                    }
                });


    }


}