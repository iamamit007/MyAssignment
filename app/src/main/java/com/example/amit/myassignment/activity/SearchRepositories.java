package com.example.amit.myassignment.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amit.myassignment.API.FetchDataListener;
import com.example.amit.myassignment.API.GETAPIRequest;
import com.example.amit.myassignment.API.RequestQueueService;
import com.example.amit.myassignment.R;
import com.example.amit.myassignment.Utils.DatabaseHelper;
import com.example.amit.myassignment.Utils.MyFont;
import com.example.amit.myassignment.Utils.Util;
import com.example.amit.myassignment.adapter.SearchListAdapter;
import com.example.amit.myassignment.model.OwnerModel;
import com.example.amit.myassignment.model.SearchResultModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import spencerstudios.com.fab_toast.FabToast;



//1page

public class SearchRepositories extends AppCompatActivity {
    private DatabaseHelper db;
    private EditText search_artist_box;
    ArrayList<String> cc = new ArrayList<>();
    private RecyclerView search_list;
    private SearchListAdapter mAdapter;
    private ShimmerFrameLayout shimmer_view_container;
    private LinearLayout go;
    private ArrayList<SearchResultModel> mDataset;
    private ArrayList<OwnerModel> ownerDataSet = new ArrayList<>();
    private SlidingUpPanelLayout mLayout;
    private Button applyfilter;
    private ImageView id_panel_ini;
    private TextView panel_stat,sort,fork,update,asc,dsc;
    private Button apply_filter,cancel;
    String sortValue,ordervalue,lang_str;
    EditText lang;
    String keyNmae="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideup);
        search_artist_box = (EditText) findViewById(R.id.search_artist_box);
        shimmer_view_container = findViewById(R.id.shimmer_view_container);

        search_list = (RecyclerView) findViewById(R.id.search_list);
        id_panel_ini =  findViewById(R.id.id_panel_ini);
        panel_stat =  findViewById(R.id.panel_stat);
        go =  findViewById(R.id.bookmark_list_btn);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        sort =  findViewById(R.id.start);
        fork =  findViewById(R.id.fork);
        update =  findViewById(R.id.update);
        lang =  findViewById(R.id.lang);
        asc = findViewById(R.id.asc);
        dsc = findViewById(R.id.dsc);
        apply_filter = findViewById(R.id.apply_filter);
        cancel = findViewById(R.id.cancel);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);


        search_artist_box.setBackgroundColor(getResources().getColor(R.color.black));
        search_artist_box.addTextChangedListener(searchListener);
        db = new DatabaseHelper(this);

        if(Util.checkNetStatus(this)){
            FabToast.makeText(SearchRepositories.this, "Network Connected", FabToast.LENGTH_LONG, FabToast.SUCCESS,  FabToast.POSITION_DEFAULT).show();

        }else {
            Util.checkNetStatusResult(this);

        }

        try {
            MyFont.applyCustomFont((ViewGroup) this.findViewById(android.R.id.content).getRootView(), MyFont.TYPEFACE.FontLight2(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        getSearchREsults("skia"); //for demo

        search_artist_box.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    go.performClick();
                    // do your stuff here
                }
                return true;
            }
        });


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(Util.checkNetStatusResult(SearchRepositories.this)){
                  String keys = search_artist_box.getText().toString().trim();

                   getSearchREsults(keys);
                }

            }
        });



        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("kkkkkkkk", "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i("kkkkkkkk", "onPanelStateChanged " + newState);
                if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
                    panel_stat.setText("Slide down");
                    id_panel_ini.setImageResource(R.mipmap.ic_down);
                } else {
                    panel_stat.setText("Slide up for filter options");
                    id_panel_ini.setImageResource(R.mipmap.ic_down_arrow);
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort.setBackgroundResource(R.drawable.rounded_back3);
                sortValue = "start";
                update.setBackgroundResource(R.drawable.rounded_back);
                fork.setBackgroundResource(R.drawable.rounded_back);



            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortValue = "update";
                sort.setBackgroundResource(R.drawable.rounded_back);
                fork.setBackgroundResource(R.drawable.rounded_back);
                update.setBackgroundResource(R.drawable.rounded_back3);

            }
        });
        fork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort.setBackgroundResource(R.drawable.rounded_back);
                update.setBackgroundResource(R.drawable.rounded_back);
                fork.setBackgroundResource(R.drawable.rounded_back3);

                sortValue = "fork";
            }
        });
        apply_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
                lang_str = lang.getText().toString();
                if(!lang_str.isEmpty() && !lang_str.equalsIgnoreCase("Type a language (Ex. C, JAVA)") && !lang_str.equalsIgnoreCase("")){
                    getSearchREsultsByFilter(keyNmae,sortValue,ordervalue, lang_str);
                } else {
                    getSearchREsultsByFilter(keyNmae,sortValue,ordervalue,"");
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }


            }
        });

        asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dsc.setBackgroundResource(R.drawable.rounded_back);
                asc.setBackgroundResource(R.drawable.rounded_back3);

                ordervalue = "asc";

            }
        });

        dsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dsc.setBackgroundResource(R.drawable.rounded_back3);
                asc.setBackgroundResource(R.drawable.rounded_back);

                ordervalue = "desc";

            }
        });


    }


    TextWatcher searchListener = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {

            String searchText = s.toString();
            if (searchText != null && searchText.length() > 0) {
                if(Util.checkNetStatus(SearchRepositories.this)){
                    if(searchText.length()>3){
                        getSearchREsults(searchText);
                    }

                }else {
                    Util.checkNetStatusResult(SearchRepositories.this);

                }

            }
            // TODO Auto-generated method stub
        }
    };



    private void getSearchREsults(String keywords) {
        try {
            keyNmae  =keywords;

// URLEncoder.encode(param2Before, "UTF-8");
            GETAPIRequest ApiRequest = new GETAPIRequest();
                //      String url = "https://api.github.com/search/repositories?q=skia&order=desc";
            String url = getResources().getString(R.string.SERVER_URL)+"/search/repositories?q="+keywords+"&order=desc";
            Log.d("repooooooooooooooo-->", "url>>: " + url);
            ApiRequest.request(this, serachListener, url);
            // RequestQueueService.cancelProgressDialog();
        } catch (Exception e) {
            // earningListener.onFetchFailure(getResources().getString(R.string.enter_date));
            e.printStackTrace();

        }
    }


    private void getSearchREsultsByFilter(String keywords,String sort,String orderby,String txt) {
        try {

            String url = getResources().getString(R.string.SERVER_URL)+"/search/repositories?q="+keywords;
            if(!sort.equalsIgnoreCase("")){
                url =   url +"&sort="+sort;
            }
            if(!orderby.equalsIgnoreCase("")){
                url = url + "&order="+orderby;
            }
            if(!txt.isEmpty()){
                url = url + "&language="+txt;
            }
// URLEncoder.encode(param2Before, "UTF-8");
            GETAPIRequest ApiRequest = new GETAPIRequest();
            //      String url = "https://api.github.com/search/repositories?q=skia&order=desc";


            Log.d("repooooooooooooooo-->", "url>>: " + url);
            ApiRequest.request(this, serachListener, url);
            // RequestQueueService.cancelProgressDialog();
        } catch (Exception e) {
            // earningListener.onFetchFailure(getResources().getString(R.string.enter_date));
            e.printStackTrace();

        }
    }


    FetchDataListener serachListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            Log.d("responseData", "onFetchComplete:>>> " + data);
            RequestQueueService.cancelProgressDialog();
            View view = findViewById(android.R.id.content);
            try {
                boolean errorFree = true;
                if (errorFree) {
                    try {
                        if (data != null && data.has("items")) {
                            JSONArray rawArray = new JSONArray();
                            rawArray = data.getJSONArray("items");

                            JSONArray sortedJsonArray = new JSONArray();

                            final List<JSONObject> jsonValues = new ArrayList<JSONObject>();
                            for (int i = 0; i < rawArray.length(); i++) {
                                jsonValues.add(rawArray.getJSONObject(i));
                                Log.d("@sorting", "unsorterfonFetchComplete: "+jsonValues.get(i).getInt("watchers"));
                            }



                            Collections.sort(jsonValues, new Comparator<JSONObject>() {
                                @Override
                                public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
                                    int compare = 0;
                                     final String KEY_NAME = "watchers";
                                    int valueA = jsonObjectA.optInt(KEY_NAME);
                                    int valueB=jsonObjectB.optInt(KEY_NAME);
                                    return Integer.compare(valueA, valueB);
                                }
                            });


                            for (int i = rawArray.length()-1; i > 0; i--) {

                                sortedJsonArray.put(jsonValues.get(i));
                                Log.d("@sorting", "sortedfonFetchComplete: "+jsonValues.get(i).getInt("watchers"));
                            }

                            makeImageArray(sortedJsonArray);
                            Log.d("sortedArray-->", "onFetchComplete: "+sortedJsonArray.toString());
                            mDataset = new ArrayList<>();
                            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                            mDataset.addAll(Arrays.asList(gson.fromJson(sortedJsonArray.toString(), SearchResultModel[].class)));
                            LinearLayoutManager layoutManager = new LinearLayoutManager(SearchRepositories.this);
                            search_list.setLayoutManager(layoutManager);
                            mAdapter = new SearchListAdapter(SearchRepositories.this,mDataset,ownerDataSet);
                            search_list.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            shimmer_view_container.stopShimmerAnimation();
                            shimmer_view_container.setVisibility(View.GONE);

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
        public void onArrayFetchComplete(JSONArray data) {

        }

        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.cancelProgressDialog();

        }

        @Override
        public void onFetchStart() {
            RequestQueueService.showProgressDialog(SearchRepositories.this);

        }


    };




    @Override
    protected void onResume() {
        super.onResume();
        shimmer_view_container.startShimmerAnimation();


    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmer_view_container.stopShimmerAnimation();
    }





    public  void makeImageArray(JSONArray jsonArray){
        if(ownerDataSet.size() > 0){ownerDataSet.clear();}
        for(int i = 0;i< jsonArray.length();i++){
            try {

                OwnerModel model = new OwnerModel();

                JSONObject jo = jsonArray.getJSONObject(i);
                JSONObject ownerOb= jo.getJSONObject("owner");
                String login = ownerOb.getString("login");
                String avatarUrl = ownerOb.optString("avatar_url");
                String html_url = ownerOb.optString("html_url");
                String url = ownerOb.optString("url");
                String repos_url = ownerOb.optString("repos_url");
                String type = ownerOb.optString("type");
                model.setLogin(login);
                model.setAvatar_url(avatarUrl);
                model.setHtml_url(html_url);
                ownerDataSet.add(model);


            }catch (Exception e){
                e.printStackTrace();
            }






        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

