package com.example.q.cs496_3.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.q.cs496_3.R;
import com.example.q.cs496_3.adapters.MatchAdapter;
import com.example.q.cs496_3.https.HttpGetRequest;
import com.example.q.cs496_3.models.User;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MatchFragment extends Fragment {
    private final String TAG = "MatchFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManger;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<User> userData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        userData=new ArrayList<User>();

        String id = Profile.getCurrentProfile().getId();
        String mUrl = "http://143.248.140.106:2580/members/"; //jsonMembers의 위치
        String myUrl = mUrl + id; //내 계정 정보의 위치
        HttpGetRequest getObjRequest = new HttpGetRequest();
        HttpGetRequest getMyRequest = new HttpGetRequest();
        
        try {
            JSONObject myJsonObj = new JSONObject(getMyRequest.execute(myUrl).get());
            JSONObject member = myJsonObj.getJSONObject("member");//member성분 추출
            JSONArray matchesJSON = member.getJSONArray("success"); //나와 match된 사람들의 uid를 담고 있는 Jsonarray
            ArrayList<String> matchesList = new ArrayList<String>();
            if (matchesJSON != null) {
                int len = matchesJSON.length();
                for (int i=0;i<len;i++){
                    matchesList.add(matchesJSON.get(i).toString());
                }
            }
            JSONObject jsonAll = new JSONObject(getObjRequest.execute(mUrl).get()); // 모든 사람의 데이터를 담고있는 json

            // Getting JSON Array node
            JSONArray jsonMembers = jsonAll.getJSONArray("members");
            for (int i = 0; i < jsonMembers.length(); ++i)
            {
                JSONObject other = jsonMembers.getJSONObject(i);
                String otheruId = other.getString("uId");
                //모든 객체에 대해서 uId 가 내 계정의 mathces에 있다면 userData에 담아서 보여준다.
                if (matchesList.contains(otheruId)) {
                    String photo = other.getString("photo");
                    String name = other.getString("name");
                    int age = other.getInt("age");
                    String contact = other.getString("contact");
                    String residence = other.getString("residence");
                    String job = other.getString("job");
                    String hobby = other.getString("hobby");
                    User user = new User();
                    user.setName(name);
                    user.setAge("" + age);
                    user.setContact(contact);
                    user.setResidence(residence);
                    user.setHobby(hobby);
                    user.setJob(job);
                    user.setPhoto(photo);
                    userData.add(user);
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
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.matchesRecyclerView);

        mLayoutManger = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManger);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MatchAdapter(userData);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}