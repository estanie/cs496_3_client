package com.example.q.cs496_2.fragments;

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

import com.example.q.cs496_2.R;
import com.example.q.cs496_2.adapters.OtherAdapter;
import com.example.q.cs496_2.https.HttpGetRequest;
import com.example.q.cs496_2.models.User;
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
        //Instantiate new instance of our class GET
        HttpGetRequest getRequest = new HttpGetRequest();
        HttpGetRequest getMyRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        userData=new ArrayList<User>();

        try {
            get_my_result = getRequest.execute(myUrl).get();
            JSONObject myJsonObj = new JSONObject(get_my_result);
            //.getJSONObject("member");
            JSONObject member = myJsonObj.getJSONObject("member");
            myGender = member.getString("gender");
            get_result = getMyRequest.execute(mUrl).get();
            JSONObject jsonObj = new JSONObject(get_result); //

            // Getting JSON Array node
            JSONArray members = jsonObj.getJSONArray("members");

            for (int i = 0; i < members.length(); ++i)
            {
                JSONObject m = members.getJSONObject(i);
                gender = m.getString("gender");
                if (!gender.equals(myGender)) {
                    photo = m.getString("photo");
                    name = m.getString("name");
                    age = m.getInt("age");
                    contact = m.getString("contact");
                    residence = m.getString("residence");
                    job = m.getString("job");
                    hobby = m.getString("hobby");
                    id = m.getString("uId");
                    User user1 = new User();
                    user1.setName(name);
                    user1.setAge("" + age);
                    user1.setResidence(residence);
                    user1.setHobby(hobby);
                    user1.setJob(job);
                    user1.setPhoto(photo);
                    user1.setId(id);
                    userData.add(user1);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
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