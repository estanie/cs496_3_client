package com.example.q.cs496_3.https;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpUploadRequest extends AsyncTask<String, Void, String> {
    private final String TAG = "HttpUploadRequest";
    String path;
    Context context;
    public HttpUploadRequest(String path, Context context) {
        this.path = path;
        this.context = context;
    }

    public String doInBackground(String... params) {
        uploadImage(path);
        return  " ";
    }

    private void uploadImage(String path) {
        String url = "http://socrip4.kaist.ac.kr:2580/uploads";
        final File f = new File(path);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { }
        }, null) {
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>(1);
                try {
                    params.put("image", Base64.encodeToString(Files.readAllBytes(f.toPath()), Base64.DEFAULT));
                    params.put("image_name", f.getName());
                    Log.e(TAG, params.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }
}
