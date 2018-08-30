package com.example.amit.myassignment.Utils;//

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.app.Fragment;


import org.json.JSONObject;


/**
 * Created by @mit.
 */
public class GlobalDataService {
    private static GlobalDataService mInstance;

    private String OwnerName,Reponame;






    public static synchronized GlobalDataService getInstance() {
        if (mInstance == null) {
            mInstance = new GlobalDataService();
        }
        return mInstance;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getReponame() {
        return Reponame;
    }

    public void setReponame(String reponame) {
        Reponame = reponame;
    }
}