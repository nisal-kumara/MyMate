package com.example.my_mate_01;

import com.google.firebase.firestore.auth.User;

import java.util.List;
import java.util.Map;

public class Model_S_Interests {

    String interests_List;
    String user_ID;
    //List<Map<String, Object>> users;

    List<String> interestsList;


    public List<String> getUsers() {
        return interestsList;
    }
    public Model_S_Interests(List<String> users) {
        this.interestsList = users;
    }



    public String getInterests_List() {

        return interests_List;
    }
    public void setInterests_List(String interests_List) {

        this.interests_List = interests_List;
    }

    public String getUser_ID() {

        return user_ID;
    }
    public void setUser_ID(String user_ID) {

        this.user_ID = user_ID;
    }
}
