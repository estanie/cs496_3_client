package com.example.q.cs496_3.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.q.cs496_3.R;
import com.example.q.cs496_3.adapters.OtherAdapter;
import com.example.q.cs496_3.https.HttpGetRequest;
import com.example.q.cs496_3.models.User;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class OtherFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManger;
    private ArrayList<User> userData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_others,  container, false);

        String id = Profile.getCurrentProfile().getId();
        String mUrl = "http://143.248.140.106:2580/members/";
        String myUrl = mUrl + id;

        String myGender;
        JSONArray mySuccess;
        JSONArray myReceived;
        JSONArray mySorted;
        String name="";
        String gender="";
        int age=0;
        String contact="";
        String residence="";
        String job="";
        String hobby="";
        String photo="";
        //String to place our result in
        String get_result = "";
        String get_my_result;
        String get_giver_result;
        //Instantiate new instance of our class GET
        HttpGetRequest getRequest = new HttpGetRequest();
        HttpGetRequest getMyRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        userData=new ArrayList<User>();

        try {
            get_my_result = getRequest.execute(myUrl).get();
            Log.d("my_result", get_my_result);
            JSONObject myJsonObj = new JSONObject(get_my_result);
            //.getJSONObject("member");
            JSONObject member = myJsonObj.getJSONObject("member");
            myGender = member.getString("gender");
            mySuccess = member.getJSONArray("success");
            myReceived = member.getJSONArray("received");
            mySorted = member.getJSONArray("sorted");
            Log.d("yayaya", "yiyi");

            for (int i = 0; i < myReceived.length(); ++i)
            {
                String giverId = myReceived.getString(i);
                String giverUrl = mUrl + giverId;
                HttpGetRequest getGiverRequest = new HttpGetRequest();
                get_giver_result = getGiverRequest.execute(giverUrl).get();
                JSONObject giverJsonObj = new JSONObject(get_giver_result);
                JSONObject m = giverJsonObj.getJSONObject("member");
                photo = m.getString("photo");
                name = m.getString("name");
                age = m.getInt("age");
                contact = m.getString("contact");
                residence = m.getString("residence");
                job = m.getString("job");
                hobby = m.getString("hobby");
                User user1 = new User();
                user1.setName(name);
                user1.setAge("" + age);
                user1.setResidence(residence);
                user1.setHobby(hobby);
                user1.setJob(job);
                user1.setPhoto(photo);
                user1.setUId(giverId);
                Log.d("like_me","hahaha");
                user1.setLike_me(1);
                userData.add(user1);
            }

            Log.d("wowow", "wiwiwi");


            // sorted 생기면 여기 주석 풀고 밑을 주석처리하기
            /*
            for (int i = 0; i < mySorted.length(); ++i)
            {
                boolean run = true;
                for (int j = 0; j < myReceived.length(); ++j)
                {
                    if (mySorted.getString(i).equals(myReceived.getString(j)))
                    {
                        run = false;
                        break;
                    }
                }
                if (run)
                {
                    HttpGetRequest getSortedRequest = new HttpGetRequest();
                    String get_sorted_result = getSortedRequest.execute(mUrl + mySorted.getString(i)).get();
                    JSONObject m = new JSONObject(get_sorted_result);

                    photo = m.getString("photo");
                    name = m.getString("name");
                    age = m.getInt("age");
                    contact = m.getString("contact");
                    residence = m.getString("residence");
                    job = m.getString("job");
                    hobby = m.getString("hobby");

                    User user1 = new User();
                    user1.setName(name);
                    user1.setAge("" + age);
                    user1.setResidence(residence);
                    user1.setHobby(hobby);
                    user1.setJob(job);
                    user1.setPhoto(photo);
                    user1.setUId(id);
                    userData.add(user1);
                }
            }*/

            get_result = getMyRequest.execute(mUrl).get();
            JSONObject jsonObj = new JSONObject(get_result); //

            // Getting JSON Array node
            JSONArray members = jsonObj.getJSONArray("members");

            for (int i = 0; i < members.length(); ++i) {
                JSONObject m = members.getJSONObject(i);
                gender = m.getString("gender");
                id = m.getString("uId");
                if (!gender.equals(myGender)) {
                    boolean run = true;
                    if (mySuccess != null)
                    {
                        for (int j = 0; j < mySuccess.length(); ++j)
                        {
                            if (id.equals(mySuccess.getString(j)))
                            {
                                run = false;
                            }
                        }
                    }
                    if (myReceived != null)
                    {
                        for (int j = 0; j < myReceived.length(); ++j)
                        {
                            if (id.equals(myReceived.getString(j)))
                            {
                                run = false;
                            }
                        }
                    }
                    if (run) {
                        photo = m.getString("photo");
                        name = m.getString("name");
                        age = m.getInt("age");
                        contact = m.getString("contact");
                        residence = m.getString("residence");
                        job = m.getString("job");
                        hobby = m.getString("hobby");

                        User user1 = new User();
                        user1.setName(name);
                        user1.setAge("" + age);
                        user1.setResidence(residence);
                        user1.setHobby(hobby);
                        user1.setJob(job);
                        user1.setPhoto(photo);
                        user1.setUId(id);
                        userData.add(user1);
                    }
                    else {

                    }
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            Log.e("!!!!!","nullpointerexception");
        }

        mRecyclerView = view.findViewById(R.id.othersRecyclerView);

        mLayoutManger = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManger);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new OtherAdapter(userData);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}