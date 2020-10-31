package com.example.my_mate_01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_mate_01.notifications.APIservice;
import com.example.my_mate_01.notifications.Client;
import com.example.my_mate_01.notifications.Data;
import com.example.my_mate_01.notifications.Response;
import com.example.my_mate_01.notifications.Sender;
import com.example.my_mate_01.notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Chat_Activity extends AppCompatActivity {

    RecyclerView msgRecycler;
    ImageView msgPro;
    TextView msgName, msgStatus;
    EditText msgType;
    ImageButton btnSend;

    private FirebaseAuth fbAuth;
    private FirebaseUser fireBUser;
    private FirebaseFirestore fStore;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference userDbRef;

    String currentUser;
    //to check if receiver has seen the message or not
    ValueEventListener seenListener;
    DatabaseReference seenDbRef;

    List<Model_Chat> mChatList;
    Adapter_Chat mAdapterChat;

    String receiverID;
    String receiverImage;

    DatabaseReference mMsgRef;
    List<Model_Chat> mChatList01;
    private List<TextMessage> mList = new ArrayList<>();
    LinearLayout mSuggestionParent;

    APIservice mAPIservice;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_);

        fbAuth = FirebaseAuth.getInstance();
        fireBUser = fbAuth.getCurrentUser();
        currentUser = fireBUser.getUid();

        Toolbar msgToolbar = findViewById(R.id.msgToolbar);
        setSupportActionBar(msgToolbar);
        msgToolbar.setTitle("");

        msgRecycler = findViewById(R.id.msgRecycler);
        msgPro = findViewById(R.id.msgPro);
        msgName = findViewById(R.id.msgName);
        msgStatus = findViewById(R.id.msgStatus);
        msgType = findViewById(R.id.msgType);
        btnSend = findViewById(R.id.btnSend);
        mSuggestionParent = findViewById(R.id.suggestionParent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        msgRecycler.setHasFixedSize(true);
        msgRecycler.setLayoutManager(linearLayoutManager);

        //create api service
        mAPIservice = Client.getRetrofit("https://fcm.googleapis.com/").create(APIservice.class);

        Intent intent = getIntent();
        receiverID = intent.getStringExtra("receiverID");

        mMsgRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        fStore = FirebaseFirestore.getInstance();

        fStore.collection("users")
                .document(currentUser)
                .collection("matches")
                .document(receiverID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {

                                            mFirebaseDatabase = FirebaseDatabase.getInstance();
                                            userDbRef = mFirebaseDatabase.getReference("users");

                                            Query userQ = userDbRef.orderByChild("user_uid").equalTo(receiverID);
                                            userQ.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                                                        String name = "" + ds.child("user_name").getValue();
                                                        receiverImage = "" + ds.child("user_image").getValue();

                                                        String oStatus = "" + ds.child("status").getValue();
                                                        if (oStatus.equals("online")) {

                                                            msgStatus.setText(oStatus);

                                                        } else {
                                                            //convert time stamp
                                                            //convert time stamp to proper format
                                                            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                                                            calendar.setTimeInMillis(Long.parseLong(oStatus));
                                                            String dateFormat = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
                                                            msgStatus.setText("Last seen: " + dateFormat);
                                                        }
                                                        msgName.setText(name);
                                                        try {
                                                            Picasso.get().load(receiverImage).placeholder(R.drawable.ic_propic_white).into(msgPro);
                                                        } catch (Exception e) {
                                                            Picasso.get().load(R.drawable.ic_propic_white).into(msgPro);
                                                        }
                                                    }}
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                            }
                        }
                    });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = msgType.getText().toString().trim();

                if(TextUtils.isEmpty(message)){
                    Toast.makeText(Chat_Activity.this, "Type here before send a Message", Toast.LENGTH_SHORT).show();
                }else {
                    sendMessage(message);
                }
                //reset edit text value 0 after message sent every time
                msgType.setText("");
            }
        });

        readMessage();

        seenMessage();

        smartReply();
    }

    private void smartReply() {

        mMsgRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot,
                                     @Nullable String s) {


//                Model_Chat pojo = dataSnapshot.getValue(Model_Chat.class);
//                String n = mChatList01.get(mChatList01.size()-1).getSender();
//                String lastMessage = mChatList.get(mChatList.size()-1).toString();
//                Model_Chat n = mChatList.get(mChatList.size());
//                String e = pojo.getMessage();
//                X.add(e);
//                String x = X.get(X.size()-1).toString();
                mList.add(TextMessage.createForLocalUser(
                        "How are you", System.currentTimeMillis()));
//
//                 mList.add(TextMessage.createForRemoteUser(
//                "i'", System.currentTimeMillis(), receiverID));

                suggestReplies();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(final String message) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", currentUser);
        hashMap.put("receiver", receiverID);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("seen", false);
        databaseReference.child("Chats").push().setValue(hashMap);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(currentUser);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Model_Users user = dataSnapshot.getValue(Model_Users.class);

            if(notify){
                sendNotification(receiverID, user.getUser_name(), message);
            }
            notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String receiverID, final String user_name, final String message) {

        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(receiverID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data =new Data(currentUser, user_name+":"+message, "New Message", receiverID, R.drawable.ic_propic_white);

                    Sender sender = new Sender(data, token.getToken());
                    mAPIservice.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void suggestReplies() {
        SmartReplyGenerator smartReply = SmartReply.getClient();
        smartReply.suggestReplies(mList)
                .addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                    @Override
                    public void onSuccess(SmartReplySuggestionResult result) {
                        mSuggestionParent.removeAllViews();

                        for (SmartReplySuggestion suggestion : result.getSuggestions()) {
                            View view = LayoutInflater.from(getApplicationContext()).
                                    inflate(R.layout.smart_reply, null, false);
                            TextView reply = view.findViewById(R.id.reply);
                            reply.setText(suggestion.getText());
                            mSuggestionParent.addView(view);
                            //****
                   /*     if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                            // The conversation's language isn't supported, so
                            // the result doesn't contain any suggestions.
                        } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                            // Task completed successfully
                            // ...
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                });*/
                        }
                    }
                });
    }

    private void readMessage() {

        mChatList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Model_Chat model_chat = snapshot.getValue(Model_Chat.class);
                    if (model_chat.getReceiver().equals(currentUser) && model_chat.getSender().equals(receiverID) ||
                            model_chat.getReceiver().equals(receiverID) && model_chat.getSender().equals(currentUser)){
                        mChatList.add(model_chat);
                    }

                    mAdapterChat = new Adapter_Chat(Chat_Activity.this, mChatList, receiverImage, mSmartReplyVisibility);
                    mAdapterChat.notifyDataSetChanged();
                    msgRecycler.setAdapter(mAdapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void seenMessage() {

        seenDbRef = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = seenDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    Model_Chat modelChat = ds.getValue(Model_Chat.class);
                    if(modelChat.getReceiver().equals(currentUser) && modelChat.getSender().equals(receiverID)){
                        HashMap<String, Object> seenHashMap = new HashMap<>();
                        seenHashMap.put("seen", true);
                        ds.getRef().updateChildren(seenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void onlineStatus(String status){

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser);
        HashMap<String,Object> updates = new HashMap<>();
        updates.put("status", status);
        //update the value of status's field of current user
        dbRef.updateChildren(updates);
    }

    final SmartReplyVisibility mSmartReplyVisibility = new SmartReplyVisibility() {
        @Override
        public void SmartReplyVisibility() {

            mSuggestionParent.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onStart() {
        checkUserStatus();
        //setting online
        onlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //get last seen
        String timestamp = String.valueOf(System.currentTimeMillis());

        //setting offline with last seen
        onlineStatus(timestamp);
        seenDbRef.removeEventListener(seenListener);
    }

    @Override
    protected void onResume() {
        //setting online
        onlineStatus("online");
        super.onResume();
    }

    private void checkUserStatus(){
        if(fireBUser != null){

        }else{
            startActivity(new Intent(this, Login_Form.class));
            finish();
        }
    }
}