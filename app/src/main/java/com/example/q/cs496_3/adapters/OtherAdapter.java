package com.example.q.cs496_3.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.example.q.cs496_3.R;
import com.example.q.cs496_3.https.HttpGetRequest;
import com.example.q.cs496_3.https.HttpPatchRequest;
import com.example.q.cs496_3.https.HttpPushRequest;
import com.example.q.cs496_3.models.User;
import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.entity.StringEntity;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.viewHolder> {
    private ArrayList<User> userData;
    private final String TAG = "OtherAdapter";

    public OtherAdapter(ArrayList<User> data){
        userData = data;
    }
    
    public class viewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout viewEntry;
        private ImageView viewPhoto;
        private TextView viewName;
        private TextView viewAge;
        private TextView viewResidence;
        private TextView viewJob;
        private TextView viewHobby;
        public ImageButton heartButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            viewPhoto = itemView.findViewById(R.id.oEntryPhoto);
            viewName = itemView.findViewById(R.id.oEntryName);
            viewAge = itemView.findViewById(R.id.oEntryAge);
            viewResidence = itemView.findViewById(R.id.oEntryResidence);
            viewJob = itemView.findViewById(R.id.oEntryJob);
            viewHobby =itemView.findViewById(R.id.oEntryHobby);
            heartButton = itemView.findViewById(R.id.heartSignalButton);
        }
    }
    
    @NonNull
    @Override
    public OtherAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if (userData.get(i).getIsStyleSet() == 1) {

            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_style, viewGroup, false);
        }
        else {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.entry_others, viewGroup, false);
        }
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OtherAdapter.viewHolder holder, final int i) {
        //holder.viewPhoto;

            Uri uri = null;
            ImageAdapter imageAdapter = new ImageAdapter(holder.viewPhoto.getContext(), uri);
            //ImageView imageView = new ImageView(getContext());
            RequestManager requestManager = Glide.with(imageAdapter.getContext());
            // Create request builder and load image.
            RequestBuilder requestBuilder = requestManager.load("http://143.248.140.106:2580/uploads/" + userData.get(i).getPhoto());
            //requestBuilder = requestBuilder.apply(new RequestOptions().override(250, 250));
            // Show image into target imageview.
            Log.d("PHOTOPHOTO", userData.get(i).getPhoto() == null ? "1" : "WOOOW");
            requestBuilder.into(holder.viewPhoto);
            final String takerId = userData.get(i).getUId();
            final String myId = Profile.getCurrentProfile().getId();
        if (userData.get(i).getIsStyleSet() == 0) {
            holder.viewPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.viewName.setText(userData.get(i).getName());
            holder.viewAge.setText(userData.get(i).getAge());
            holder.viewResidence.setText(userData.get(i).getResidence());
            holder.viewJob.setText(userData.get(i).getJob());
            holder.viewHobby.setText(userData.get(i).getHobby());
            if (userData.get(i).getLike_me() == 1) {
                holder.heartButton.setImageResource(R.drawable.heart_to_me_2);
            }
        }

        holder.heartButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //내 아이디 : myId
                //상대 아이디 : takerId
                //Some url endpoint that you may have
                String mUrl = "http://143.248.140.106:2580/members/";
                String myUrl = mUrl + myId;
                //String to place our result in
                String get_my_result;
                JSONArray my_gave = new JSONArray();
                JSONArray my_received = new JSONArray();
                JSONArray my_success = new JSONArray();
                JSONArray my_style = new JSONArray();

                //내 gaved, received, success 받아오기
                //Instantiate new instance of our class GET

                HttpGetRequest getMyRequest = new HttpGetRequest();
                try {
                    get_my_result = getMyRequest.execute(myUrl).get();
                    Log.d("hihi", get_my_result == null ? "0" : get_my_result);
                    if (get_my_result != null) {
                        JSONObject myJsonObj = new JSONObject(get_my_result);
                        //.getJSONObject("member");
                        JSONObject member = myJsonObj.getJSONObject("member");
                        my_gave = member.getJSONArray("gave");
                        my_received = member.getJSONArray("received");
                        my_success = member.getJSONArray("success");
                        my_style = member.getJSONArray("style");
                        Log.d("gave", my_gave.toString());
                    }
                } catch (ExecutionException e) {
                    Log.e("error", "haha");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("error", "haha");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String yourUrl = mUrl + takerId;
                //String to place our result in
                String get_your_result = "";
                JSONArray your_received = new JSONArray();
                JSONArray your_gave = new JSONArray();
                JSONArray your_success = new JSONArray();
                String your_photo = "";
                //Instantiate new instance of our class GET
                HttpGetRequest getYourRequest = new HttpGetRequest();
                //상대의 received, gave, sucess 받아오기
                try {
                    get_your_result = getYourRequest.execute(yourUrl).get();
                    if (get_your_result != null) {
                        JSONObject jsonObj = new JSONObject(get_your_result);
                        //.getJSONObject("member");
                        JSONObject member = jsonObj.getJSONObject("member");
                        your_received = member.getJSONArray("received");
                        your_gave = member.getJSONArray("gave");
                        your_success = member.getJSONArray("success");
                        your_photo = member.getString("photo");
                    }
                } catch (ExecutionException e) {
                    Log.e("error", "haha");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e("error", "haha");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (userData.get(i).getIsStyleSet() == 1) {
                    Log.d("your_photo", your_photo);
                    my_style.put(your_photo);
                    Log.d("my_style", my_style.toString());
                    new HttpPatchRequest(my_style.toString(), myId).execute();
                } else {
                    JSONObject json = new JSONObject();
                    boolean matched = false;
                    for (int i = 0; i < my_received.length(); ++i) {
                        try {
                            // matching 성공 시
                            if (my_received.getString(i).equals(takerId)) {
                                matched = true;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // matching 성공 시
                    if (matched) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                R.string.match_success, Toast.LENGTH_SHORT);
                        toast.show();
                        Gson gson = new GsonBuilder().create();

                        // 나와 상대방 success에 서로의 uId 추가
                        JSONObject myJson = new JSONObject(); // 중괄호 역할
                        JSONObject yourJson = new JSONObject(); // 중괄호 역할
                        my_success.put(takerId);
                        Log.d("takerId", takerId);
                        your_success.put(myId);
                        Log.d("myId", takerId);
                        try {
                            myJson.put("success", my_success);
                            yourJson.put("success", your_success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 나의 received에서 상대 uId 제거
                        JSONArray new_my_received = new JSONArray();
                        for (int j = 0; j < my_received.length(); ++j) {
                            try {
                                if (!my_received.getString(j).equals(takerId)) {
                                    new_my_received.put(my_received.getString(j));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // 상대의 gave에서 나의 uId 제거
                        JSONArray new_your_gave = new JSONArray();
                        for (int j = 0; j < your_gave.length(); ++j) {
                            try {
                                if (!your_gave.getString(j).equals(myId)) {
                                    new_your_gave.put(your_gave.getString(j));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            myJson.put("received", new_my_received);
                            yourJson.put("gave", new_your_gave);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       //httprequestclass 로 보내서 실행시키기
                    Log.e(TAG, "My : " + myJson.toString() + myId);
                    Log.e(TAG, "Your: " + yourJson.toString() + takerId);
                    new HttpPatchRequest(myJson.toString(), myId).execute();
                    new HttpPatchRequest(yourJson.toString(), takerId).execute();
                    Log.e(TAG, "Push Request!");
                    new HttpPushRequest(myId, takerId).execute();
                    } else { // match 실패
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.match_waiting, Toast.LENGTH_SHORT);
                    toast.show();
                    new HttpPushRequest("", takerId).execute();
                    try {
                        //하트 받는 사람의 received 에 내 id 넣기
                        your_received.put(myId);
                        json.put("received", your_received);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                            //httprequestclass 로 보내서 실행시키기
                            new HttpPatchRequest(json.toString(), takerId).execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //내 gave에 하트 받는 사람 id 넣기
                        JSONObject json_2 = new JSONObject();
                        my_gave.put(takerId);
                        try {
                            json_2.put("gave", my_gave);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            //httprequestclass 로 보내서 실행시키기
                            new HttpPatchRequest(json_2.toString(), myId).execute();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                //하트 아이콘 바꾸기
                holder.heartButton.setImageResource(R.drawable.red_heart);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return userData.size();
    }
}