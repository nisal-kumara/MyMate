package com.example.my_mate_01;

import java.util.List;

public class Model_Interests {

    String name;
    String where;




    public Model_Interests() {

    }

public Model_Interests(String name, String where) {
        this.name = name;
        this.where = where;
    }


    private boolean isChecked = false;

    public Model_Interests(List<String> tags) { }

    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }


    public Model_Interests(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getWhere() {
        return where;
    }
    public void setWhere(String where) {
        this.where = where;
    }





}
