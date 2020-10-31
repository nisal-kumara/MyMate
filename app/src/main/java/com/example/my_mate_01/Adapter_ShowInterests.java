package com.example.my_mate_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_ShowInterests extends RecyclerView.Adapter<Adapter_ShowInterests.Holder_interests> {

    Context context1;
    List<Model_S_Interests> interest_List;
    ItemClickListner itemClickListener;


    public Adapter_ShowInterests(Context context, List<Model_S_Interests> interest_List,ItemClickListner itemClickListener) {
        context1 = context;
        this.interest_List = interest_List;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public Holder_interests onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context1).inflate(R.layout.show_interests, parent, false);
        return new Holder_interests(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder_interests holder, final int position) {
        //get data
        List<String> Iname = interest_List.get(position).getUsers();

        //set data
        holder.txt_nameI.setText(Iname.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(holder,  holder.txt_nameI.getText(), position);
            }
        });

    }

    @Override
    public int getItemCount() { return interest_List.size(); }


    class Holder_interests extends RecyclerView.ViewHolder {

        TextView txt_nameI;

        public Holder_interests(@NonNull View view) {
            super(view);
            txt_nameI = view.findViewById(R.id.txt_nameI);
        }

    }
}
