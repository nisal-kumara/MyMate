package com.example.my_mate_01;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Messages_Fragment extends Fragment {

    private FirebaseAuth fbAuth;
    private FirebaseUser fireBUser;
    private FirebaseFirestore fStore;
    String currentUser;

    RecyclerView message_Recycler;
    Adapter_Matched_Msg mAdapterMatchedMsg;

    List<Model_Matches> mModelMatches;

    ProgressDialog dialog;

    LinearLayout linearLayoutNotEmpty;
    LinearLayout linearLayoutEmpty;


    public Messages_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        fbAuth = FirebaseAuth.getInstance();
        fireBUser = fbAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        currentUser = fbAuth.getUid();

        message_Recycler = view.findViewById(R.id.message_Recycler);
        message_Recycler.setHasFixedSize(true);
        message_Recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        linearLayoutNotEmpty = view.findViewById(R.id.linearLayoutNotEmpty01);
        linearLayoutEmpty = view.findViewById(R.id.linearLayoutEmpty01);

        mModelMatches = new ArrayList<>();

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Preparing.....");

        msg_UsersDisplay();

        return view;
    }

    private void msg_UsersDisplay() {
        dialog.show();
        fStore.collection("users")
                .document(currentUser)
                .collection("matches")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mModelMatches.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Model_Matches modelMatches = document.toObject(Model_Matches.class);

                                mModelMatches.add(modelMatches);

                                mAdapterMatchedMsg = new Adapter_Matched_Msg(getActivity(), mModelMatches);
                                message_Recycler.setAdapter(mAdapterMatchedMsg);
                                dialog.dismiss();
                            }
                            if (mModelMatches.size() == 0) {

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
}