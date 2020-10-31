package com.example.my_mate_01;

public class Model_Matches {

    String user_matches;
    String user_matched;
    String user_name;
    String user_email;
    String user_image;
    String user_about;

    public Model_Matches(String user_matches, String user_matched, String user_name, String user_email, String user_image, String user_about) {
        this.user_matches = user_matches;
        this.user_matched = user_matched;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_image = user_image;
        this.user_about = user_about;
    }

    public Model_Matches() {
    }

    public String getUser_matches() {
        return user_matches;
    }

    public void setUser_matches(String user_matches) {
        this.user_matches = user_matches;
    }

//    public String getUser_matched() {
//        return user_matched;
//    }
//
//    public void setUser_matched(String user_matched) {
//        this.user_matched = user_matched;
//    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_about() {
        return user_about;
    }

    public void setUser_about(String user_about) {
        this.user_about = user_about;
    }
}
