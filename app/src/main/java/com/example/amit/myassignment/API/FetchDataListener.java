package com.example.amit.myassignment.API;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by amit on 24/3/17.
 */

public interface FetchDataListener {
    void onFetchComplete(JSONObject data);
    void onArrayFetchComplete(JSONArray data);

    void onFetchFailure(String msg);

    void onFetchStart();
}