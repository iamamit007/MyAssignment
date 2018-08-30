package com.example.amit.myassignment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.example.amit.myassignment.R;
import com.example.amit.myassignment.Utils.MyFont;

public class Splash extends Activity {
    private static long SLEEP_TIME = 3;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new IntentLauncher().start();
        try {
            MyFont.applyCustomFont((ViewGroup) this.findViewById(android.R.id.content).getRootView(), MyFont.TYPEFACE.FontLight2(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class IntentLauncher extends Thread {
        public void run() {
            try { sleep(SLEEP_TIME * 1000); } catch (Exception e) {e.printStackTrace();}
            try {
                startActivity(new Intent(Splash.this, SearchRepositories.class));
                finish();

            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
        }
    }

}

