package com.example.my_mate_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Users extends  RecyclerView.Adapter<Adapter_Users.Holder_A> {

    Context mContext;
    List<Model_Users> userList;
    //ArrayList<Model_Users> arrayProfileClasses;

    //constructor


    public Adapter_Users(Context context, List<Model_Users> userList) {
        mContext = context;
        this.userList = userList;
    }

  /*  public Adapter_Users(ArrayList<Model_Users> arrayProfileClasses, Context mContext) {
        this.arrayProfileClasses = arrayProfileClasses;
        this.mContext = mContext;

    }*/

    @NonNull
    @Override
    public Holder_A onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate show_users.xml
        View view = LayoutInflater.from(mContext).inflate(R.layout.show_users, parent, false);

        return new Holder_A(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_A holder, int position) {
        //get data
        String uImage = userList.get(position).getUser_image();
        String uName = userList.get(position).getUser_name();
        final String uEmail = userList.get(position).getUser_email();

        //set data
        holder.nameproH.setText(uName);
        holder.emailproH.setText(uEmail);
        try {
            Picasso.get().load(uImage).placeholder(R.drawable.ic_smily_pro)
                    .into(holder.avatarproH);
        } catch (Exception e) {

        }
        //item click handling
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "" + uEmail, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //view holder class
    class Holder_A extends RecyclerView.ViewHolder {

        ImageView avatarproH;
        TextView nameproH, emailproH;
        Button btnAddF;

        public Holder_A(@NonNull View itemView) {
            super(itemView);

            avatarproH = itemView.findViewById(R.id.avatarPro);
            nameproH = itemView.findViewById(R.id.namePro);
            emailproH = itemView.findViewById(R.id.emailPro);
            btnAddF = itemView.findViewById(R.id.btnAdd_F);
        }

    }
}

