package com.example.q.cs496_3.https;


import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPatch;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class HttpPatchRequest extends AsyncTask<String, Void, JsonArray> {
    HttpClient httpClient = HttpClientBuilder.create().build();
    String str;
    String user_id;

    public HttpPatchRequest(String str, String id) {
        this.str = str;
        user_id = id;
    }

    public JsonArray doInBackground(String... params) {
        try {
            HttpPatch request = new HttpPatch("http://143.248.140.106:2580/members/"+user_id);
            //StringEntity params = new StringEntity(params);
            request.addHeader("Content-Type","application/json;charset=UTF-8");
            request.addHeader("Accept","application/json;charset=UTF-8");
            Log.e("PATCHENTITY", new StringEntity(str, "UTF-8").toString());
            request.setEntity(new StringEntity(str, "UTF-8"));
            Log.d("Request is ", request.toString());
            HttpResponse response = httpClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}