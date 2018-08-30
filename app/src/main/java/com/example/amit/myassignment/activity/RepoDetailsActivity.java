package com.example.amit.myassignment.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amit.myassignment.API.FetchDataListener;
import com.example.amit.myassignment.API.GETAPIRequest;
import com.example.amit.myassignment.API.RequestQueueService;
import com.example.amit.myassignment.BuildConfig;
import com.example.amit.myassignment.R;
import com.example.amit.myassignment.Utils.CircleTransform;
import com.example.amit.myassignment.Utils.DatabaseHelper;
import com.example.amit.myassignment.Utils.GlobalDataService;
import com.example.amit.myassignment.Utils.MyFont;
import com.example.amit.myassignment.Utils.Util;
import com.example.amit.myassignment.adapter.ContributorAdapter;
import com.example.amit.myassignment.adapter.RepoListAdapter;
import com.example.amit.myassignment.adapter.SearchListAdapter;
import com.example.amit.myassignment.model.ContributorModel;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import spencerstudios.com.fab_toast.FabToast;

public class RepoDetailsActivity extends AppCompatActivity {
    private ImageView id_artist_img_large,lm_btn;
    private TextView id_watcher_count;
    private TextView id_summ;
    private TextView id_artist_addionalInfo;
    private TextView id_repo_name, id_repo_org;
    String GiT_url,BroURL;
    String artist_name="";
    private ImageView share_btn;
    View rootView;
    File savedFile;
    String imagePath="";
    Uri outputFileUri;
    ArrayList<ContributorModel> mDataset;
    GridView gridView;
    LinearLayout controler_con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);
        id_artist_img_large     = findViewById(R.id.id_artist_img_large);
        id_repo_name            = findViewById(R.id.repo_name);
        id_repo_org             = findViewById(R.id.id_repo_org);
        id_watcher_count        = findViewById(R.id.id_repo_org);
        id_summ                 = findViewById(R.id.id_summ);
        lm_btn                  = findViewById(R.id.lm_btn);
        controler_con           = findViewById(R.id.controler_con);
        rootView                = getWindow().getDecorView().findViewById(android.R.id.content);
        gridView                = (GridView) findViewById(R.id.contributors);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Intent intent     = getIntent();
         artist_name            = intent.getStringExtra("artist_name");
        getRepoDetails(GlobalDataService.getInstance().getOwnerName(),GlobalDataService.getInstance().getReponame());

        try {
            MyFont.applyCustomFont((ViewGroup) this.findViewById(android.R.id.content).getRootView(), MyFont.TYPEFACE.FontLight2(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        lm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                InAppBrowser.setGitUrl(GiT_url);
                Intent openBrowerIntent = new Intent(RepoDetailsActivity.this,InAppBrowser.class);
                intent.putExtra("git_url",GiT_url);
                startActivity(openBrowerIntent);

            }
        });

        controler_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InAppBrowser.setGitUrl(GiT_url);
                Intent openBrowerIntent = new Intent(RepoDetailsActivity.this,InAppBrowser.class);
                intent.putExtra("git_url",GiT_url);
                startActivity(openBrowerIntent);
            }
        });
    }




    private void getSearchREsults(String OwnerName,String repo_name) {
        try {
            GETAPIRequest ApiRequest = new GETAPIRequest();
            String url = "https://api.github.com/repos/"+OwnerName+"/"+repo_name+"/contributors";
            Log.d("oriflame", "url>>: " + url);
            ApiRequest.arrayrequest(this, serachListener,null, url);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    FetchDataListener serachListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {

        }

        @Override
        public void onArrayFetchComplete (JSONArray data) {
            Log.d("responseData", "onFetchComplete:>>> " + data);
            RequestQueueService.cancelProgressDialog();
            View view = findViewById(android.R.id.content);
            try {
                boolean errorFree = true;
                if (errorFree) {
                    try {
                        if (data != null && data.length()>0) {

                            mDataset = new ArrayList<>();
                            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                            mDataset.addAll(Arrays.asList(gson.fromJson(data.toString(), ContributorModel[].class)));
                            ContributorAdapter adapter = new ContributorAdapter(RepoDetailsActivity.this, mDataset);
                            gridView.setAdapter(adapter);

//                       getSearchREsults(selectedOwner.getLogin(),name);
                        } else {

                            Log.d("here", "onFetchComplete: 1");
                        }
                    } catch (Exception e) {
                        //errorpage.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                        Log.d("here", "onFetchComplete: 2");

                    }
                } else {
                    RequestQueueService.cancelProgressDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();

        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(RepoDetailsActivity.this);

        }


    };





    public void openLink(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void getRepoDetails(String owner_name,String reponame) {
        try {

            GETAPIRequest ApiRequest = new GETAPIRequest();
            String url = getResources().getString(R.string.SERVER_URL)+"/repos/"+owner_name+"/"+reponame;
            Log.d("getSearchREsults", "url>>: " + url);
            ApiRequest.request(this, repoDEtailsListener, url);
        } catch (Exception e) {
            // earningListener.onFetchFailure(getResources().getString(R.string.enter_date));
            e.printStackTrace();

        }
    }


    FetchDataListener repoDEtailsListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            Log.d("responseData", "onFetchComplete:>>> " + data);
            RequestQueueService.cancelProgressDialog();
            View view = findViewById(android.R.id.content);
            try {
                boolean errorFree = true;
                if (errorFree) {
                    try {
                        String owner_name="";
                        if(data.has("owner")){
                            JSONObject jo = data.getJSONObject("owner");
                             owner_name = jo.optString("login");
                            String html_url = jo.optString("html_url");
                            String avatar_url = jo.optString("avatar_url");

                            String imgUrl = avatar_url;
                            Picasso.with(RepoDetailsActivity.this).load(imgUrl).transform(new CircleTransform()).memoryPolicy(MemoryPolicy.NO_CACHE).into(id_artist_img_large, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });

                        }

                        String repoFull_name = data.optString("full_name");
                        String reponame = data.optString("name");
                        int repocount = data.optInt("full_name");
                        String gitlink = data.optString("html_url");
                        String description = data.optString("description");


                        id_repo_name.setText(""+repoFull_name);
                        id_watcher_count.setText("Wacther's Count- "+repocount);
                        id_summ.setText(""+description);
                        GiT_url = gitlink;


                        getSearchREsults(owner_name,reponame);
                    } catch (Exception e) {
                        //errorpage.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                        Log.d("here", "onFetchComplete: 2");

                    }
                } else {
                    RequestQueueService.cancelProgressDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onArrayFetchComplete(JSONArray data) {

        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();

        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(RepoDetailsActivity.this);

        }


    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
