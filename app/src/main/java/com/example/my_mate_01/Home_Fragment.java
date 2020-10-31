package com.example.my_mate_01;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.opencensus.metrics.export.Summary;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */


public class Home_Fragment extends Fragment {

    private FirebaseAuth fbAuth;
    private FirebaseUser fireBUser;
    private FirebaseFirestore fStore;
    String currentUser;
    String stringCheckLocation;

    private List<String> arrayUserRemove;


    private ArrayList<Model_Users> arrayUserClass;
    ArrayList<Model_S_Interests> mInterests;


    RecyclerView home_ReView, horizontal_Re;
    Adapter_Users mAdapterUsers;
    Adapter_ShowInterests mAdapterShowInterests;

    List<Model_Users> mUsersList;
    //List<Model_S_Interests> mInterestsList;

    List<Model_Users> filteredUsersList;
    ProgressDialog dialog;

    public Home_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_, container, false);

        fbAuth = FirebaseAuth.getInstance();
        fireBUser = fbAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

       /* if (fireBUser != null) {
            currentUser = fireBUser.getUid();
        }*/

        currentUser = fbAuth.getUid();

        mInterests = new ArrayList<>();

        arrayUserClass = new ArrayList<>();
        arrayUserRemove = new ArrayList<>();
        arrayUserRemove.add("demoUserWhenZero");

        //recycle views
        home_ReView = view.findViewById(R.id.home_Recycle);
        horizontal_Re = view.findViewById(R.id.horizontal_Re);

        home_ReView.setHasFixedSize(true);
        home_ReView.setLayoutManager(new LinearLayoutManager(getActivity()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontal_Re.setHasFixedSize(true);
        horizontal_Re.setLayoutManager(layoutManager);


        mUsersList = new ArrayList<>();
        //mInterestsList = new ArrayList<>();
        filteredUsersList = new ArrayList<>();

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Preparing..... ");



        UserRecyclerView();
        dialog.show();


        return view;
    }


    private void get_Interests() {
        if (mInterests.size() > 0)
            mInterests.clear();

        fStore.collection("selected_Interests")
                .document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();

                            List<String> interestsList = (List<String>) documentSnapshot.get("interests_List");
                            Model_S_Interests model_s_interests = new Model_S_Interests(interestsList);

                             for(int i=0; i<interestsList.size(); i++) {
                            System.out.println(interestsList.get(i));

                            mInterests.add(model_s_interests);
                        }

                        ItemClickListner listener = new ItemClickListner(){
                            @Override
                            public void onItemClicked(RecyclerView.ViewHolder vh, Object item, int pos){
                                Toast.makeText(getActivity(), "Interest clicked: " + pos, Toast.LENGTH_SHORT).show();
                              filteredUsersList.clear();
                                for (Model_Users u:mUsersList) {
                                    if(u.selected_interests.contains(item))
                                    {
                                        filteredUsersList.add(u);
                                    }
                                }
                                mAdapterUsers = new Adapter_Users(getActivity(), filteredUsersList);
                                home_ReView.setAdapter(mAdapterUsers);
                                mAdapterUsers.notifyDataSetChanged();

                            }
                        };

                        mAdapterShowInterests = new Adapter_ShowInterests(getActivity(), mInterests,listener);
                        horizontal_Re.setAdapter(mAdapterShowInterests);

                    }
                });
               /* .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Problem while Loading!!!", Toast.LENGTH_SHORT).show();
                        Log.v("--1--", e.getMessage());
                    }
                });*/
    }

    private void UserRecyclerView() {

        if (currentUser != null) {

            fStore.collection("users")
                    .document(currentUser)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            DocumentSnapshot documentSnapshot = task.getResult();

                            String stringShowLocation = documentSnapshot.getString("show_user_location");
                            if (stringShowLocation != null) {
                                stringCheckLocation = stringShowLocation;

                            } else {
                                String stringUserState = documentSnapshot.getString("user_city");

                                Map<String, Object> mapShowLocation = new HashMap<>();
                                mapShowLocation.put("show_user_location", stringUserState);
                                fStore.collection("users")
                                        .document(currentUser)
                                        .update(mapShowLocation);

                                stringCheckLocation = stringUserState;

                            }
                            UsersDisplay(stringCheckLocation);
                            get_Interests();
                        }
                    });
        }
    }

    private void UsersDisplay(final String stringCheckLocation) {

        final String currentUser = fireBUser.getUid();

        fStore.collection("users")
                .orderBy("user_name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        for (QueryDocumentSnapshot querySnapshot : task.getResult()) {

                            final Model_Users model_users = querySnapshot.toObject(Model_Users.class);

                            fStore.collection("selected_Interests").document(model_users.user_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    List<String> interestsList = (List<String>) documentSnapshot.get("interests_List");
                                    model_users.selected_interests = interestsList;

                                }
                            });

                            if (!model_users.getUser_uid().equals(currentUser)) {

                                if (stringCheckLocation.equals("Any")) {


                                    if (!arrayUserRemove.contains(model_users.getUser_uid())) {

                                        if (model_users.getShow_profile() == null || model_users.getShow_profile().equals("yes")) {

                                            mUsersList.add(model_users);

                                        }
                                    }
                                } else {
                                    if (model_users.getUser_city().equals(stringCheckLocation)) {

                                        if (!arrayUserRemove.contains(model_users.getUser_uid())) {

                                            if (model_users.getShow_profile() == null || model_users.getShow_profile().equals("yes")) {

                                                mUsersList.add(model_users);

                                            }
                                        }

                                    }
                                }
                            }


                            mAdapterUsers = new Adapter_Users(getActivity(), mUsersList);
                            home_ReView.setAdapter(mAdapterUsers);

                            if (mUsersList.size() == 0) {

                                dialog.dismiss();
                            } else {

                                dialog.dismiss();
                            }


                        }


                    }
                });
    }

    private void RequestUserMatch(String requestedUser) {

        fStore.collection("users")
                .document(currentUser)
                .collection("likes")
                .document(requestedUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {

                            Map<String, Object> mapMatchesReq = new HashMap<>();
                           // mapMatchesReq.put("user_matched", Timestamp.now());
                            mapMatchesReq.put("user_matches", requestedUser);

                            fStore.collection("users")
                                    .document(currentUser)
                                    .collection("matches")
                                    .document(requestedUser)
                                    .set(mapMatchesReq)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Map<String, Object> mapMatchesCurrent = new HashMap<>();
                                               // mapMatchesCurrent.put("user_matched", Timestamp.now());
                                                mapMatchesCurrent.put("user_matches", currentUser);

                                                fStore.collection("users")
                                                        .document(requestedUser)
                                                        .collection("matches")
                                                        .document(currentUser)
                                                        .set(mapMatchesCurrent)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(getContext(), "Hoorayy!! Matched!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                            }
                                        }
                                    });

                        }
                    }
                });

    }

    public void SwipeUserLoves() {


        final String requestedUser = arrayUserClass.get(intSwipePositionFirst).getUser_uid();

        Map<String, Object> mapLovesUser = new HashMap<>();
        mapLovesUser.put("user_loves", requestedUser);
        mapLovesUser.put("user_loved", Timestamp.now());
        mapLovesUser.put("user_super", "no");

        fStore.collection("users")
                .document(currentUser)
                .collection("loves")
                .document(requestedUser)
                .set(mapLovesUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Map<String, Object> mapLikesUser = new HashMap<>();
                        mapLikesUser.put("user_likes", currentUser);
                        mapLikesUser.put("user_liked", Timestamp.now());
                        mapLovesUser.put("user_super", "no");

                        fStore.collection("users")
                                .document(requestedUser)
                                .collection("likes")
                                .document(currentUser)
                                .set(mapLikesUser)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            RequestUserMatch(requestedUser);
                                        }
                                    }
                                });


                    }
                });

    }


}