package com.example.primerproyectogrupalcompmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText etEmailSignup;
    private EditText etEmailContrasenaSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmailSignup = findViewById(R.id.etEmailSignup);
        etEmailContrasenaSignup = findViewById((R.id.etContrasenaSignup));

        setTitle("Registrarse");
    }

    public void registrar(View view) {
        if (!etEmailSignup.getText().toString().isEmpty() && !etEmailContrasenaSignup.getText().toString().isEmpty()) {
            auth = FirebaseAuth.getInstance();

            auth.getInstance().createUserWithEmailAndPassword(etEmailSignup.getText().toString(),
                    etEmailContrasenaSignup.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registro realizado con éxito!", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Email o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Email o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }


    }
}




