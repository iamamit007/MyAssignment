package com.example.amit.myassignment.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.amit.myassignment.R;

public class InAppBrowser extends AppCompatActivity {

    public static String gitUrl;

    WebView git_link;

    public static void setGitUrl(String gitUrl) {
        InAppBrowser.gitUrl = gitUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_browser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        git_link = findViewById(R.id.git_link);

        Intent intent = getIntent();
        String url = intent.getStringExtra("git_url");
        git_link.loadUrl(gitUrl);
        git_link.getSettings().setJavaScriptEnabled(true);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
