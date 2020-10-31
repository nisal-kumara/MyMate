package com.example.my_mate_01;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Chat extends RecyclerView.Adapter<Adapter_Chat.chatHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context mContext;
    List<Model_Chat> mChatList;
    String imageUrl;

    FirebaseUser fUser;
    private SmartReplyVisibility mSmartReplyVisibility;

    public Adapter_Chat(Context context, List<Model_Chat> chatList, String imageUrl,SmartReplyVisibility mSmartReplyVisibility) {
        this.mContext = context;
        this.mChatList = chatList;
        this.imageUrl = imageUrl;
        this.mSmartReplyVisibility = mSmartReplyVisibility;
    }

    @NonNull
    @Override
    public chatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_chat_right, parent, false);
            return new chatHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_chat_left, parent, false);
            return new chatHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull chatHolder holder, int position) {

        String msg = mChatList.get(position).getMessage();
        String timestamp = mChatList.get(position).getTimestamp();

        //convert time stamp to proper format
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String dateFormat = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.messageChat.setText(msg);
        holder.timeChat.setText(dateFormat);

        try{
            Picasso.get().load(imageUrl).into(holder.profileChat);
        }
        catch (Exception e){

        }
        //setting up seen and delivered status
        if (position==mChatList.size()-1){
            if (mChatList.get(position).isSeen()){
                holder.seenChat.setText("Seen");

            }else {
                holder.seenChat.setText("Delivered");

            }
        }else {
            holder.seenChat.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    @Override
    public int getItemViewType(int position) {

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChatList.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

    class chatHolder extends RecyclerView.ViewHolder{

        ImageView profileChat;
        TextView messageChat, timeChat, seenChat;
        LinearLayout mLinearLayout;

        public chatHolder(@NonNull View itemView) {
            super(itemView);

            profileChat = itemView.findViewById(R.id.profileChat);
            messageChat = itemView.findViewById(R.id.messageChat);
            timeChat = itemView.findViewById(R.id.timeChat);
            seenChat = itemView.findViewById(R.id.seenChat);
            mLinearLayout = itemView.findViewById(R.id.suggestionParent);
        }
    }

}
