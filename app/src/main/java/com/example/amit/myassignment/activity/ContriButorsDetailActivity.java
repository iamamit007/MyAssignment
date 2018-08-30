package com.example.amit.myassignment.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amit.myassignment.API.FetchDataListener;
import com.example.amit.myassignment.API.GETAPIRequest;
import com.example.amit.myassignment.API.RequestQueueService;
import com.example.amit.myassignment.R;
import com.example.amit.myassignment.Utils.CircleTransform;
import com.example.amit.myassignment.Utils.GlobalDataService;
import com.example.amit.myassignment.Utils.MyFont;
import com.example.amit.myassignment.adapter.ContributorAdapter;
import com.example.amit.myassignment.adapter.RepoListAdapter;
import com.example.amit.myassignment.adapter.SearchListAdapter;
import com.example.amit.myassignment.model.ContributorModel;
import com.example.amit.myassignment.model.OwnerModel;
import com.example.amit.myassignment.model.RepoModel;
import com.example.amit.myassignment.model.SearchResultModel;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ContriButorsDetailActivity extends AppCompatActivity {

    private RecyclerView search_list;
    private RepoListAdapter mAdapter;
    private ArrayList<RepoModel> mDataset;

    public static ContributorModel contributorModel;
    ImageView id_contributors_image;
    TextView contributors_name_tv;

    public static void setContributorModel(ContributorModel contributorModel) {
        ContriButorsDetailActivity.contributorModel = contributorModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributor_details);
        search_list           = (RecyclerView) findViewById(R.id.blist);
        id_contributors_image =  findViewById(R.id.id_contributors_image);
        contributors_name_tv  =  findViewById(R.id.contributors_name_tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        try {
            MyFont.applyCustomFont((ViewGroup) this.findViewById(android.R.id.content).getRootView(), MyFont.TYPEFACE.FontLight2(this));
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(contributorModel!=null){
            String owNerName = contributorModel.getLogin();
            contributors_name_tv.setText(owNerName);

            GlobalDataService.getInstance().setOwnerName(owNerName);
            Picasso.with(this).load(contributorModel.getAvatar_url()).transform(new CircleTransform()).memoryPolicy(MemoryPolicy.NO_CACHE).into(id_contributors_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });
            getRepoListByName(owNerName);
        }




    }


    @Override
    protected void onResume() {
        super.onResume();


      //  manageDataBase(cursor);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void getRepoListByName(String ownerName) {
        try {


// URLEncoder.encode(param2Before, "UTF-8");
            GETAPIRequest ApiRequest = new GETAPIRequest();
            String url =getResources().getString(R.string.SERVER_URL)+"/users/"+ownerName+"/repos";
            Log.d("getSearchREsults", "url>>: " + url);
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
                            mDataset.addAll(Arrays.asList(gson.fromJson(data.toString(), RepoModel[].class)));
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ContriButorsDetailActivity.this);
                            search_list.setLayoutManager(layoutManager);
                            mAdapter = new RepoListAdapter(ContriButorsDetailActivity.this,mDataset);
                            search_list.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

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
            RequestQueueService.showProgressDialog(ContriButorsDetailActivity.this);

        }


    };

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
