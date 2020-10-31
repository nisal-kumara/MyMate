package com.example.my_mate_01;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class Account_Fragment extends Fragment {

    FirebaseAuth mFirebaseAuth;
   // FirebaseUser mUser;
    FirebaseFirestore mFirestore;

    FirebaseUser firebaseUser;
    String currentUser;

    ImageView avatar;
    TextView tvname, tvcity, tvemail;

    String profileUser;

    public Account_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_, container, false);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        avatar = view.findViewById(R.id.avatar);
        tvname = view.findViewById(R.id.tvName);
        tvcity = view.findViewById(R.id.tvCity);
        tvemail = view.findViewById(R.id.tvEmail);


        if (firebaseUser != null) {

            currentUser = firebaseUser.getUid();

            mFirestore.collection("users")
                    .document(currentUser)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            if (documentSnapshot != null && documentSnapshot.exists()) {

                                String user_name = documentSnapshot.getString("user_name");
                                String user_email = documentSnapshot.getString("user_email");
                                String user_city = documentSnapshot.getString("user_city");
                                String user_image = documentSnapshot.getString("user_image");

                                tvname.setText(user_name);
                                tvemail.setText(user_email);
                                tvcity.setText(user_city);

                                if (user_image.equals("image")) {
                                    avatar.setImageResource(R.drawable.ic_add_image);
                                } else {
                                    Picasso.get().load(user_image).into(avatar);
                                }

                               /* try {
                                    Picasso.get().load(user_image).into(avatar);
                                } catch (Exception ex) {
                                    Picasso.get().load(R.drawable.ic_add_image).into(avatar);
                                }*/
                            }
                        }


                    });
        }

        return view;
    }
}
