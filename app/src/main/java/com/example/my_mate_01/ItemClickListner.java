package com.example.my_mate_01;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemClickListner {
    void onItemClicked(RecyclerView.ViewHolder vh, Object item, int pos);
}
