package com.example.primerproyectogrupalcompmovil;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity
{
    private Toolbar ChattoolBar;
    private ImageButton SendMessageButton, SendImagefileButton;
    private EditText userMessageInput;
    private RecyclerView userMessagesList;
    private TextView receiverName;
    private CircleImageView receiverProfileImage;
    private String messageReceiverID, messageReceiverName, messageSenderID;
    private DatabaseReference Rootref;
    private DataSnapshot dataSnapchot;
    private FirebaseAuth mAuth;


    @Override
    protected void  onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();

        Rootref = FirebaseDatabase.getInstance().getReference();

        messageReceiverID = getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName = getIntent().getExtras().get("userName").toString();

        InitializeFields();

        DisplayReceiverInfo();

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                SendMessage();

            }
        });

    }

    private void SendMessage()
    {

        String messageText = userMessageInput.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {
            Toast.makeText(this, "PorFavor Escriba su mensaje...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String message_sender_red = "Messages/" + messageSenderID + "/" + messageReceiverID;
            String message_receiver_red = "Messages/" + messageReceiverID + "/" + messageSenderID;

            DatabaseReference user_message_key = Rootref.child("Messages").child(messageSenderID)
                    .child(messageReceiverID).push();
            String message_push_id = user_message_key.getKey();



        }

    }

    private void DisplayReceiverInfo() {

        receiverName.setText(messageReceiverName);
        Rootref.child("Users").child(messageReceiverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (dataSnapchot.exists())
                {
                    final String profileImage = dataSnapchot.child("profileimage").getValue().toString();
                    Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(receiverProfileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void InitializeFields() {

        ChattoolBar = (Toolbar) findViewById(R.id.chat_bar_layout);
        setSupportActionBar(ChattoolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(action_bar_view);


        receiverName = (TextView) findViewById(R.id.custom_profile_name);
        receiverProfileImage = (CircleImageView) findViewById(R.id.custom_profile_image);

        SendMessageButton = (ImageButton) findViewById(R.id.send_message_button);
        SendImagefileButton = (ImageButton) findViewById(R.id.send_image_file_button);
        userMessageInput = (EditText) findViewById(R.id.input_message);

    }



    /*private static final int SIGN_IN_REQUEST_CODE = 1;
    RecyclerView rvChat;
    List<Mensaje> listaMensajes = new ArrayList<>();

    TextView tvNombre;
    TextView tvFecha;
    TextView tvTexto;
    */
    /*
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


    }*/
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
    /*
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
    */


}
