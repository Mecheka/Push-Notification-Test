package com.example.suriya.firebasepushnotification.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.suriya.firebasepushnotification.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPass;
    private Button btnLogin;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        initinstance();
    }

    private void initinstance() {
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPass = (EditText) findViewById(R.id.editPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (MaterialFancyButton) findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginFirebase();

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginFirebase() {

        loadingDialog = ProgressDialog.show(LoginActivity.this, "Log in", "Log in",
                false, true);

        String email = editEmail.getText().toString();
        String pass = editPass.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    String token_id = FirebaseInstanceId.getInstance().getToken();
                    String user_id = mAuth.getCurrentUser().getUid();

                    Map<String, Object> tokenMap = new HashMap<>();
                    tokenMap.put("token_id", token_id);

                    mFirestore.collection("User").document(user_id).update(tokenMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Intent sendToMain = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(sendToMain);
                                    loadingDialog.dismiss();

                                }
                            });


                } else {
                    Toast.makeText(LoginActivity.this, "Error : " + task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                    loadingDialog.dismiss();
                }

            }
        });
    }

}
