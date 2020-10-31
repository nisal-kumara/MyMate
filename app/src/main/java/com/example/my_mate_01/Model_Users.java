package com.example.my_mate_01;

import java.util.List;

public class Model_Users {

    String user_name, user_email, user_city, user_about, user_gender, user_image, user_uid, show_profile, show_user_location;
    List<String> selected_interests;
    public Model_Users() {

    }

    public Model_Users(String user_name, String user_email, String user_city, String user_about, String user_gender, String user_image, String user_uid, String show_profile, String show_user_location,   List<String> selected_interests) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_city = user_city;
        this.user_about = user_about;
        this.user_gender = user_gender;
        this.user_image = user_image;
        this.user_uid = user_uid;
        this.show_profile = show_profile;
        this.show_user_location = show_user_location;
        this.selected_interests = selected_interests;
    }

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

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_about() {
        return user_about;
    }

    public void setUser_about(String user_about) {
        this.user_about = user_about;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getShow_profile() {
        return show_profile;
    }

    public void setShow_profile(String show_profile) {
        this.show_profile = show_profile;
    }

    public String getShow_user_location() {
        return show_user_location;
    }

    public void setShow_user_location(String show_user_location) {
        this.show_user_location = show_user_location;
    }
}