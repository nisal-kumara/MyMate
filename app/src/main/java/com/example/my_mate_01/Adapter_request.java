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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_request extends  RecyclerView.Adapter<Adapter_request.Holder_A01>{

    private RequestClickListner reqListener;
    public static String reqID;

    Context mContext01;
    List<Model_Like> userLikes01;

    public Adapter_request(Context context01, List<Model_Like> userLikes01, RequestClickListner reqListener) {
        this.mContext01 = context01;
        this.userLikes01 = userLikes01;
        this.reqListener = reqListener;
    }

    @NonNull
    @Override
    public Holder_A01 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext01).inflate(R.layout.adapter_request, parent, false);
        return new Holder_A01(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder_A01 holder, int position) {

        //get data
        String uImage01 = userLikes01.get(position).getUser_image();
        String uName01 = userLikes01.get(position).getUser_name();
        final String uEmail01 = userLikes01.get(position).getUser_email();

        final String UId = userLikes01.get(position).getUser_likes();

        //set data
        holder.nameproH01.setText(uName01);
        holder.emailproH01.setText(uEmail01);

        try {
            Picasso.get().load(uImage01).placeholder(R.drawable.ic_smily_pro)
                    .into(holder.avatarproH01);
        } catch (Exception e) {

        }

        holder.btnAddF01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reqID = UId;
//                Toast.makeText(mContext01, "" + reqID, Toast.LENGTH_SHORT).show();
                reqListener.RequestUserMatch();
                holder.btnAddF01.setClickable(false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userLikes01.size();
    }

    class Holder_A01 extends RecyclerView.ViewHolder {

        ImageView avatarproH01;
        TextView nameproH01, emailproH01;
        Button btnAddF01;

        public Holder_A01(@NonNull View itemView) {
            super(itemView);

            avatarproH01 = itemView.findViewById(R.id.avatarPro01);
            nameproH01 = itemView.findViewById(R.id.namePro01);
            emailproH01 = itemView.findViewById(R.id.emailPro01);
            btnAddF01 = itemView.findViewById(R.id.btnAdd_F01);

        }

    }
}
