package com.example.suriya.firebasepushnotification.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.suriya.firebasepushnotification.R;
import com.example.suriya.firebasepushnotification.adapter.UsersRecyclerViewAdapter;
import com.example.suriya.firebasepushnotification.utill.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private RecyclerView recyclerUsers;
    private List<User> userList;
    private UsersRecyclerViewAdapter adapter;
    private FirebaseFirestore mFirestore;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        intInstance(rootView, container);
        return rootView;
    }

    private void intInstance(View rootView, ViewGroup container) {

        recyclerUsers = (RecyclerView)rootView.findViewById(R.id.recyclerUsers);
        userList = new ArrayList<>();
        adapter = new UsersRecyclerViewAdapter(container.getContext(), userList);
        recyclerUsers.setHasFixedSize(true);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerUsers.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        mFirestore.collection("User").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (documentSnapshots != null){
                    userList.clear();
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED){

                            String userID = doc.getDocument().getId();

                            User user = doc.getDocument().toObject(User.class).withId(userID);
                            userList.add(user);
                            adapter.notifyDataSetChanged();

                        }
                    }
                }
            }
        });
    }
}
