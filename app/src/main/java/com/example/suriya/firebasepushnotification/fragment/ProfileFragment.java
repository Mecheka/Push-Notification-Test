package com.example.suriya.firebasepushnotification.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.suriya.firebasepushnotification.R;
import com.example.suriya.firebasepushnotification.activity.LoginActivity;
import com.example.suriya.firebasepushnotification.utill.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private CircleImageView imgProfile;
    private TextView tvName;
    private MaterialFancyButton btnLogout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        intInstance(rootView);
        return rootView;
    }

    private void intInstance(View rootView) {

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        imgProfile = (CircleImageView) rootView.findViewById(R.id.imgProfile);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        btnLogout = (MaterialFancyButton) rootView.findViewById(R.id.btnLogout);

        mFirestore.collection("User").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot != null){
                    User objUser = documentSnapshot.toObject(User.class);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.loading);

                    Glide.with(getActivity())
                            .setDefaultRequestOptions(requestOptions)
                            .load(objUser.getImageUrl())
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(imgProfile);

                    tvName.setText(objUser.getName());
                }

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> removeMapToken = new HashMap<>();
                removeMapToken.put("token_id", FieldValue.delete());

                mFirestore.collection("User").document(mAuth.getCurrentUser().getUid())
                        .update(removeMapToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        mAuth.signOut();
                        Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(logoutIntent);
                        getActivity().finish();

                    }
                });

            }
        });

    }

}
