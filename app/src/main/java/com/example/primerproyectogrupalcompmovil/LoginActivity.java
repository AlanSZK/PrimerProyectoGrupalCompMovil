package com.example.primerproyectogrupalcompmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etEmailLogin,etContrasenaLogin;
    private FirebaseAuth auth;

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
                        Toast.makeText(getApplicationContext(), "Datos correctos", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);

                        startActivity(intent);

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


}