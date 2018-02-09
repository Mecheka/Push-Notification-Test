package com.example.suriya.firebasepushnotification.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.firebasepushnotification.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {

    private TextView tvUserID;
    private EditText editMessenger;
    private MaterialFancyButton btnSend;
    private ProgressBar progressBar;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initInstance();
    }

    private void initInstance() {

        final String userID = getIntent().getStringExtra("userID");
        String userName = getIntent().getStringExtra("userName");
        final String cerrenUserID = mAuth.getUid();
        tvUserID = (TextView)findViewById(R.id.tvUerID);
        editMessenger = (EditText)findViewById(R.id.editMessenger);
        btnSend = (MaterialFancyButton)findViewById(R.id.btnSend);
        progressBar = (ProgressBar)findViewById(R.id.progreesBar);

        tvUserID.setText("Send To " + userName);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messenger = editMessenger.getText().toString();

                progressBar.setVisibility(View.VISIBLE);

                if (messenger.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    editMessenger.setError("Pleas enter your messenger");
                    editMessenger.requestFocus();
                    return;
                }

                Map<String, Object> notificationMessage = new HashMap<>();
                notificationMessage.put("message", messenger);
                notificationMessage.put("from", cerrenUserID);

                mFirestore.collection("User/" + userID + "/Notification").add(notificationMessage)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(SendActivity.this, "Notification send.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SendActivity.this, "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Error ", e.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
