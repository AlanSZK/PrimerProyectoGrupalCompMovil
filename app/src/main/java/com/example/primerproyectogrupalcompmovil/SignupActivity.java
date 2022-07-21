package com.example.primerproyectogrupalcompmovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    EditText etEmailSignup;
    EditText etEmailContrasenaSignup;
    EditText etUsuario;
    String uuidUsuario;

    boolean exito=false;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etEmailSignup = findViewById(R.id.etEmailSignup);
        etEmailContrasenaSignup = findViewById((R.id.etContrasenaSignup));
        etUsuario = findViewById(R.id.etUsuario);

        setTitle("Registrarse");


    }

    public void registrar(View view) {


        if ((!etEmailSignup.getText().toString().isEmpty() && !etEmailContrasenaSignup.getText().toString().isEmpty() && !etUsuario.getText().toString().isEmpty())) {

            //Registro de correo y contraseña en Firebase
            auth = FirebaseAuth.getInstance();

            auth.getInstance().createUserWithEmailAndPassword(etEmailSignup.getText().toString(), etEmailContrasenaSignup.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        //Registro de usuario en base de datos firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        Map<String, Object> usuario = new HashMap<>();
                        usuario.put("authUuid",auth.getUid().toString());
                        usuario.put("email", etEmailSignup.getText().toString());
                        usuario.put("nombre", etUsuario.getText().toString());



                        db.collection("usuario")
                                .add(usuario)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());

                                        Toast.makeText(getApplicationContext(), "Registro realizado con éxito!", Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error adding document", e);
                                        Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    } else {
                        Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
        }

    }
}







