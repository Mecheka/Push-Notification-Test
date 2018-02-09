package com.example.suriya.firebasepushnotification.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.suriya.firebasepushnotification.R;
import com.example.suriya.firebasepushnotification.utill.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 101;
    private CircleImageView imgGelery;
    private EditText editName;
    private EditText editEmail;
    private EditText editPass;
    private Button btnSingUp;
    private com.rilixtech.materialfancybutton.MaterialFancyButton btnBackto;
    private Uri imgUri;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imgUri = null;
        mStorageRef = FirebaseStorage.getInstance().getReference().child("image");
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        initInstancr();
    }

    private void initInstancr() {
        imgGelery = (CircleImageView) findViewById(R.id.imgGelery);
        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPass = (EditText) findViewById(R.id.editPass);
        btnSingUp = (Button) findViewById(R.id.btnSingUp);
        btnBackto = (MaterialFancyButton) findViewById(R.id.btnBacktoLogin);

        // Set!!

        imgGelery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                singupFirebase();

            }
        });

        btnBackto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void singupFirebase() {
        if (imgGelery != null) {

            loadingDialog = ProgressDialog.show(RegisterActivity.this, "Loading", "Loading...",
                    false, true);

            final String name = editName.getText().toString();
            String email = editEmail.getText().toString();
            String pass = editPass.getText().toString();

            if (name.isEmpty()) {
                loadingDialog.dismiss();
                editName.setError("Pleas enter your name");
                editName.requestFocus();
                return;
            }
            if (email.isEmpty()) {
                loadingDialog.dismiss();
                editEmail.setError("Pleas enter your email");
                editEmail.requestFocus();
                return;
            }
            if (pass.isEmpty()) {
                loadingDialog.dismiss();
                editPass.setError("Pleas enter your password");
                editPass.requestFocus();
                return;
            }
            if (pass.length() < 6) {
                loadingDialog.dismiss();
                editPass.setError("Your password minimum");
                editPass.requestFocus();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        final String userID = mAuth.getCurrentUser().getUid();
                        StorageReference userProfile = mStorageRef.child(userID + ".jpg");
                        userProfile.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {

                                if (uploadTask.isSuccessful()) {

                                    final String downloadUrl = uploadTask.getResult().getDownloadUrl().toString();

                                    String token_id = FirebaseInstanceId.getInstance().getToken();

                                    User user = new User(name, downloadUrl, token_id);

                                    mFirestore.collection("User").document(userID).set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    loadingDialog.dismiss();
                                                    sendToMainActivity();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            loadingDialog.dismiss();
                                            Log.d("Error Put File : ", e.getMessage());
                                            Toast.makeText(RegisterActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    loadingDialog.dismiss();
                                    Log.d("Error Put File : ", uploadTask.getException().getMessage());
                                    Toast.makeText(RegisterActivity.this, "Error : " + uploadTask.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                    } else {
                        loadingDialog.dismiss();
                        Log.d("Error Sing Up : ", task.getException().getMessage());
                        Toast.makeText(RegisterActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }

    private void sendToMainActivity() {

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                imgUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver()
                            , imgUri);
                    imgGelery.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
