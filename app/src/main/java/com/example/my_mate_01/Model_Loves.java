package com.example.my_mate_01;

public class Model_Loves {

    String user_loves;
    String user_loved;

    public Model_Loves(String user_loves, String user_loved) {
        this.user_loves = user_loves;
        this.user_loved = user_loved;
    }

    public Model_Loves() {
    }

//    public String getUser_loved() {
//        return user_loved;
//    }
//
//    public void setUser_loved(String user_loved) {
//        this.user_loved = user_loved;
//    }

    public String getUser_loves() {

        return user_loves;
    }

    public void setUser_loves(String user_loves) {

        this.user_loves = user_loves;
    }

}
