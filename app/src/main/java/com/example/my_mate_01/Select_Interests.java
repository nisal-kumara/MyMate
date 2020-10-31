package com.example.my_mate_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Select_Interests extends AppCompatActivity {

    FirebaseFirestore mFirestore;
    private FirebaseUser firebaseUser;

    RecyclerView mRecyclerView;
    ArrayList<Model_Interests> model_interestsArrayList;
    Adapter_Select_Interests mAdapter_select_interests;
    Button btnGetSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__interests);

        this.btnGetSelected = (Button) findViewById(R.id.btnGetSelected);
        this.mRecyclerView = findViewById(R.id.select_Interests_Recy);

        model_interestsArrayList = new ArrayList<>();
        SetUpRecyclerView();
        setUpF_Base();
        GetInterests();

        btnGetSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter_select_interests.getSelected().size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    ArrayList<String> string_Interests = new ArrayList<String>();

                    for (int i = 0; i < mAdapter_select_interests.getSelected().size(); i++) {
                        stringBuilder.append(mAdapter_select_interests.getSelected().get(i).getName());
                        string_Interests.add(mAdapter_select_interests.getSelected().get(i).getName());

                        //String string_Interests = mAdapter_select_interests.getSelected().get(i).getName();
                       // System.out.print("tt : "+string_Interests);

                    }

                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String current_User = firebaseUser.getUid();

                    Map<String, Object> userInterests = new HashMap<>();
                    userInterests.put("user_ID", current_User);
                    userInterests.put("interests_List", string_Interests);

                    mFirestore.collection("selected_Interests")
                            .document(current_User)
                            .set(userInterests);


                    //showToast(stringBuilder.toString().trim());

                    Intent intent = new Intent(Select_Interests.this, Dashbord.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(Select_Interests.this, "Select at least One Interest", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUpF_Base() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    private void GetInterests() {
        if(model_interestsArrayList.size()>0)
            model_interestsArrayList.clear();

        mFirestore.collection("interests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot querySnapshot: task.getResult()){
                            Model_Interests model_interests = new Model_Interests(querySnapshot.getString("name"));
                            model_interestsArrayList.add(model_interests);
                        }

                        mAdapter_select_interests = new Adapter_Select_Interests(Select_Interests.this, model_interestsArrayList);
                        mRecyclerView.setAdapter(mAdapter_select_interests);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Select_Interests.this, "Problem while Loading!!!", Toast.LENGTH_SHORT).show();
                        Log.v("--1--", e.getMessage());
                    }
                });
    }

    private void SetUpRecyclerView() {

        mRecyclerView = findViewById(R.id.select_Interests_Recy);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(Select_Interests.this, LinearLayoutManager.VERTICAL));
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
