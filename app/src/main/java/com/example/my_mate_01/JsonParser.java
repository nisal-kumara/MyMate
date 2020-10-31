package com.example.my_mate_01;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private HashMap<String, String>  parseJsonObject(JSONObject object){
        //initialize hashmap
        HashMap<String,String> dataList = new HashMap<>();
        try {
            //get name from the object
            String name = object.getString("name");
            //get latitude from the object
            String latitude = object.getJSONObject("geometry").getJSONObject("location")
                    .getString("lat");
            //get longitude from the object
            String longitude = object.getJSONObject("geometry").getJSONObject("location")
                    .getString("lng");

            dataList.put("name", name);
            dataList.put("lat", latitude);
            dataList.put("lng", longitude);

        } catch (JSONException e){
            e.printStackTrace();
        }
        //returning hashmap
        return dataList;
    }
    private List<HashMap<String, String>> parseJsonArray(JSONArray jsonArray){
        //initialize hashmap list
        List<HashMap<String,String>> dataList = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++){
            try {
                HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                //add data to the hashmap list
                dataList.add(data);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        //return datalist
        return dataList;
    }
    public List<HashMap<String,String>> parseResult(JSONObject object){
        //initialize json array
        JSONArray jsonArray = null;
        //getting result array
        try {
            jsonArray = object.getJSONArray("results");
        }catch (JSONException e){
            e.printStackTrace();
        }
        //return array
        return parseJsonArray(jsonArray);
    }

}
