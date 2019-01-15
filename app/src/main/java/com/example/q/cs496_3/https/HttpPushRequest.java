package com.example.q.cs496_3.https;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;

import org.json.JSONObject;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class HttpPushRequest extends AsyncTask<String, Void, JsonArray> {
    HttpClient httpClient = HttpClientBuilder.create().build();
    String str;
    public HttpPushRequest(String myId, String matchId) {
        JSONObject myJson = new JSONObject();
        try {
            if (myId != "") myJson.put("my_id", myId);
            myJson.put("match_id", matchId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        str = myJson.toString();
    }

    public JsonArray doInBackground(String... params) {
        try {
            HttpPost request = new HttpPost("http://143.248.140.106:2580/push");
            //StringEntity params = new StringEntity(params);
            request.addHeader("Content-Type","application/json;charset=UTF-8");
            request.addHeader("Accept","application/json;charset=UTF-8");
            request.setEntity(new StringEntity(str, "UTF-8"));
            Log.d("Request is ", request.toString());
            HttpResponse response = httpClient.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
