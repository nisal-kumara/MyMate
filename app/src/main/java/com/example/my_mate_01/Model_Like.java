package com.example.my_mate_01;


import java.util.Date;

public class Model_Like {

    String user_likes;
    String user_liked;
    String user_name;
    String user_email;
    String user_image;
    String user_about;

    public Model_Like(String user_likes, String user_liked, String user_name, String user_email, String user_image, String user_about) {
        this.user_likes = user_likes;
        this.user_liked = user_liked;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_image = user_image;
        this.user_about = user_about;
    }

    public Model_Like() {
    }


    public String getUser_likes() {
        return user_likes;
    }

    public void setUser_likes(String user_likes) {
        this.user_likes = user_likes;
    }

//    public String getUser_liked() {
//        return user_liked;
//    }
//
//    public void setUser_liked(String user_liked) {
//        this.user_liked = user_liked;
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
