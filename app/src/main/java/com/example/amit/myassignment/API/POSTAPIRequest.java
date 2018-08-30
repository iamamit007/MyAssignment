package com.example.amit.myassignment.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.amit.myassignment.R;


import org.json.JSONException;
import org.json.JSONObject;


public class POSTAPIRequest {
    public void request(final Context context, final FetchDataListener listener, JSONObject params, final String ApiURL) throws JSONException {
        if (listener != null) {
            //call onFetchComplete of the listener
            //to show progress dialog
            //-indicates calling started
            listener.onFetchStart();
        }
        //base server URL
        String baseUrl=context.getResources().getString(R.string.SERVER_URL);
        //add extension api url received from caller
        //and make full api
        String url = baseUrl + ApiURL;
        Log.d("api", "request: " + url);
        Log.d("params", "params: " + params);
        //Requesting with post body as params
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (listener != null) {
                                listener.onFetchComplete(response);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    if(listener!=null)
                    listener.onFetchFailure("Network Connectivity Problem");
                } else if (error.networkResponse != null && error.networkResponse.data != null) {
                    VolleyError volley_error = new VolleyError(new String(error.networkResponse.data));
                    String errorMessage      = "";
                    try {
                        JSONObject errorJson = new JSONObject(volley_error.getMessage().toString());
                        if(errorJson.has("error")) errorMessage = errorJson.getString("error");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (errorMessage.isEmpty()) {
                        errorMessage = volley_error.getMessage();
                    }
                    if (listener != null) listener.onFetchFailure(errorMessage);
                    } else {
                        listener.onFetchFailure("Something went wrong. Please try again later");
                    }
            }
        });
        RequestQueueService.getInstance(context).addToRequestQueue(postRequest.setShouldCache(false));
    }

}
