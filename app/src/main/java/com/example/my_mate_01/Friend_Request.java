package com.example.my_mate_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friend_Request extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseUser fireBUser;
    private FirebaseFirestore fStore;
    String currentUser;

    RecyclerView req_Recycler;
    Adapter_request mAdapter_request;

    List<Model_Like> mModelLikes;

    Context mContext;
    ProgressDialog dialog;

    LinearLayout linearLayoutNotEmpty;
    LinearLayout linearLayoutEmpty;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend__request);

        fbAuth = FirebaseAuth.getInstance();
        fireBUser = fbAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        actionBar = getSupportActionBar();
        actionBar.setTitle("Friend Requests");


        currentUser = fbAuth.getUid();

        req_Recycler = findViewById(R.id.req_Recycler);
        req_Recycler.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        req_Recycler.setLayoutManager(linearLayoutManager);

        linearLayoutNotEmpty = findViewById(R.id.linearLayoutNotEmpty02);
        linearLayoutEmpty = findViewById(R.id.linearLayoutEmpty02);

        mModelLikes = new ArrayList<>();

        dialog = new ProgressDialog(Friend_Request.this);
        dialog.setMessage("Preparing..... ");

        req_UsersDisplay();

    }

    private void req_UsersDisplay() {
        dialog.show();
        fStore.collection("users")
                .document(currentUser)
                .collection("likes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mModelLikes.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Model_Like model_like = document.toObject(Model_Like.class);

                                mModelLikes.add(model_like);

                                mAdapter_request = new Adapter_request(Friend_Request.this, mModelLikes, reqListener);
                                req_Recycler.setAdapter(mAdapter_request);
                                dialog.dismiss();
                            }
                            if (mModelLikes.size() == 0) {

                                linearLayoutEmpty.setVisibility(View.VISIBLE);
                                linearLayoutNotEmpty.setVisibility(View.GONE);
                                dialog.dismiss();

                            }else {

                                linearLayoutEmpty.setVisibility(View.GONE);
                                linearLayoutNotEmpty.setVisibility(View.VISIBLE);
                                dialog.dismiss();

                            }
                        }

                    }
                });
    }

    final RequestClickListner reqListener = new RequestClickListner() {
        @Override
        public void RequestUserMatch() {

            final String clickedUser = Adapter_request.reqID;

            fStore.collection("users")
                    .document(currentUser)
                    .collection("likes")
                    .document(clickedUser)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {

                                DocumentReference docRef = fStore.collection("users").document(clickedUser);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String valueN01 = document.getString("user_name");
                                                String valueE01 = document.getString("user_email");
                                                String valueI01 = document.getString("user_image");
                                                String valueB01 = document.getString("user_about");

                                                Map<String, Object> mapMatchesReq = new HashMap<>();
                                                mapMatchesReq.put("user_matched", Timestamp.now());
                                                mapMatchesReq.put("user_matches", clickedUser);
                                                mapMatchesReq.put("user_name", valueN01);
                                                mapMatchesReq.put("user_email", valueE01);
                                                mapMatchesReq.put("user_image", valueI01);
                                                mapMatchesReq.put("user_about", valueB01);

                                                fStore.collection("users")
                                                        .document(currentUser)
                                                        .collection("matches")
                                                        .document(clickedUser)
                                                        .set(mapMatchesReq)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    DocumentReference docRef = fStore.collection("users").document(currentUser);
                                                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DocumentSnapshot document = task.getResult();
                                                                                if (document.exists()) {
                                                                                    String valueN02 = document.getString("user_name");
                                                                                    String valueE02 = document.getString("user_email");
                                                                                    String valueI02 = document.getString("user_image");
                                                                                    String valueB02 = document.getString("user_about");

                                                                                    Map<String, Object> mapMatchesCurrent = new HashMap<>();
                                                                                    mapMatchesCurrent.put("user_matched", Timestamp.now());
                                                                                    mapMatchesCurrent.put("user_matches", currentUser);
                                                                                    mapMatchesCurrent.put("user_name", valueN02);
                                                                                    mapMatchesCurrent.put("user_email", valueE02);
                                                                                    mapMatchesCurrent.put("user_image", valueI02);
                                                                                    mapMatchesCurrent.put("user_about", valueB02);

                                                                                    fStore.collection("users")
                                                                                            .document(clickedUser)
                                                                                            .collection("matches")
                                                                                            .document(currentUser)
                                                                                            .set(mapMatchesCurrent)
                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        Toast.makeText(Friend_Request.this, "You are friend with "+valueN02, Toast.LENGTH_SHORT).show();

                                                                                                        fStore.collection("users")
                                                                                                                .document(currentUser)
                                                                                                                .collection("likes")
                                                                                                                .document(clickedUser).delete();
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                }

                                                                            }
                                                                        }
                                                                    });

                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
        }
    };
}