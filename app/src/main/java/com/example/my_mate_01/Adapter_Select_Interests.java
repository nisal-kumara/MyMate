package com.example.my_mate_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Select_Interests extends RecyclerView.Adapter<Adapter_Select_Interests.Select_Interests_Re_Vholder> {

     Select_Interests mSelect_interests;
    private ArrayList<Model_Interests> model_interestsArrayList;
    //private List<Integer> selectedIds = new ArrayList<>();

    public Adapter_Select_Interests(Select_Interests select_interests, ArrayList<Model_Interests> model_interestsArrayList) {
        this.mSelect_interests = select_interests;
        this.model_interestsArrayList = model_interestsArrayList;
    }

    public void setEmployees(ArrayList<Model_Interests> model_interestsArrayList) {
        this.model_interestsArrayList = new ArrayList<>();
        this.model_interestsArrayList = model_interestsArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Select_Interests_Re_Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mSelect_interests.getBaseContext());
        View view = layoutInflater.inflate(R.layout.single_row, parent, false);

        return new Select_Interests_Re_Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Select_Interests_Re_Vholder holder, int position) {

        holder.bind(model_interestsArrayList.get(position));
       // holder.txtVInterest.setText(model_interestsArrayList.get(position).getName());
       // int id = mModel_interestsArrayList.get(position).get;
    }

    @Override
    public int getItemCount() {
        return model_interestsArrayList.size();
    }

    //view holder class
     class Select_Interests_Re_Vholder extends RecyclerView.ViewHolder {

        private TextView txtVInterest;
        private ImageView imageViewCheck;

        Select_Interests_Re_Vholder(@NonNull View itemView) {
            super(itemView);
            txtVInterest = itemView.findViewById(R.id.txtVInterest);
            imageViewCheck = itemView.findViewById(R.id.imageViewCheck);
        }
        void bind(final Model_Interests interests) {
            imageViewCheck.setVisibility(interests.isChecked() ? View.VISIBLE : View.GONE);
            txtVInterest.setText(interests.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    interests.setChecked(!interests.isChecked());
                    imageViewCheck.setVisibility(interests.isChecked() ? View.VISIBLE : View.GONE);
                }
            });
        }

    }
    public ArrayList<Model_Interests> getAll() {

        return model_interestsArrayList;
    }

    public ArrayList<Model_Interests> getSelected() {
        ArrayList<Model_Interests> selected = new ArrayList<>();
        for (int i = 0; i < model_interestsArrayList.size(); i++) {
            if (model_interestsArrayList.get(i).isChecked()) {
                selected.add(model_interestsArrayList.get(i));
            }
        }
        return selected;
    }
}
