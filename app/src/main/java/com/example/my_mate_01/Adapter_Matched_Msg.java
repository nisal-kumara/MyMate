package com.example.my_mate_01;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Adapter_Matched_Msg extends RecyclerView.Adapter<Adapter_Matched_Msg.Holder_A02> {

    Context mContext02;
    List<Model_Matches> userMatches;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference userDbRef;

    public Adapter_Matched_Msg(Context context02, List<Model_Matches> userMatches) {
        this.mContext02 = context02;
        this.userMatches = userMatches;

    }


    @NonNull
    @Override
    public Holder_A02 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext02).inflate(R.layout.adapter_matched_msg, parent, false);
        return new Holder_A02(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder_A02 holder, int position) {

        //get data
        final String receiverID = userMatches.get(position).getUser_matches();
        String uImage02 = userMatches.get(position).getUser_image();
        String uName02 = userMatches.get(position).getUser_name();


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userDbRef = mFirebaseDatabase.getReference("users");

        Query userQ = userDbRef.orderByChild("user_uid").equalTo(receiverID);
        userQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String oStatus = "" + ds.child("status").getValue();
                    if (oStatus.equals("online")) {

                        holder.statuspro02.setText(oStatus);

                    } else {
                        //convert time stamp
                        //convert time stamp to proper format
                        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                        calendar.setTimeInMillis(Long.parseLong(oStatus));
                        String dateFormat = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                        holder.statuspro02.setText("Last seen: " + dateFormat);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //set data
        holder.nameproH02.setText(uName02);

        try {
            Picasso.get().load(uImage02).placeholder(R.drawable.ic_smily_pro)
                    .into(holder.avatarproH02);
        } catch (Exception e) {

        }

        holder.btnAddF02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext02, Chat_Activity.class);
                intent.putExtra("receiverID", receiverID);
                mContext02.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return userMatches.size();
    }

    public class Holder_A02 extends RecyclerView.ViewHolder {

        ImageView avatarproH02;
        TextView nameproH02, statuspro02;
        Button btnAddF02;

        public Holder_A02(@NonNull View itemView) {
            super(itemView);

            avatarproH02 = itemView.findViewById(R.id.avatarPro02);
            nameproH02 = itemView.findViewById(R.id.namePro02);
            statuspro02 = itemView.findViewById(R.id.statusPro02);
            btnAddF02 = itemView.findViewById(R.id.btnAdd_F02);

        }

    }
}