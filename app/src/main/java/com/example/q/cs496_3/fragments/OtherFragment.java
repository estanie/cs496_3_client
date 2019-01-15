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
import com.example.q.cs496_3.models.UserSingleton;
import com.facebook.Profile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OtherFragment extends Fragment {
    private final String TAG = "OtherFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManger;
    private ArrayList<User> userData;
    private View mView;
    User user = new User();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_others, container, false);
        mView = view;
        userData = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.othersRecyclerView);
        mLayoutManger = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManger);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new OtherAdapter(userData, false, user);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible) {
        if (!isFragmentVisible) return;
        if (mAdapter == null) return;
        String id = Profile.getCurrentProfile().getId();
        String mUrl = "http://143.248.140.106:2580/members/";
        String myUrl = mUrl + id;
        //String to place our result in
        mView.findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.waitingText).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.findNewUser).setVisibility(View.INVISIBLE);
        // TODO(gayeon): 로딩중이니까 나중에 다시 시도하라고 해주기!
        JSONArray style = null;
        JSONArray received = null;
        JSONArray success = null;
        JSONObject json = null;
        User me = null;
        try {
            Gson gson = new GsonBuilder().create();
            json = new JSONObject(new HttpGetRequest().execute(myUrl).get())
                    .getJSONObject("member");
            me = gson.fromJson(json.toString(), User.class);
            style = json.getJSONArray("style");
            received = json.getJSONArray("received");
            success = json.getJSONArray("success");
            userData.clear();
            do {
                myUrl = mUrl + "other/" + id;
                JSONArray userList = new JSONObject(new HttpGetRequest().execute(myUrl).get())
                        .getJSONArray("members");
                for (int j = 0; j < received.length(); j++) {
                    UserSingleton.getInstance().setLikeMeTrue(received.getString(j));
                }
                for (int i = 0; i < userList.length(); i++) {
                    userData.add(gson.fromJson(userList.getJSONObject(i).toString(), User.class));
                }
            } while (userData == null && style != null && style.length() != 0);
            String newUrls = "http://143.248.140.106:2580/members";
            JSONArray userLists = new JSONObject(new HttpGetRequest().execute(newUrls).get())
                    .getJSONArray("members");
            ArrayList<String> userIds = new ArrayList<>();
            ArrayList<String> currentUserIds = new ArrayList<>();
            ArrayList<String> successLists = new ArrayList<>();
            ArrayList<String> receivedLists = new ArrayList<>();
            for (int i = 0;i<received.length();i++) {
                receivedLists.add(received.getString(i));
            }
            for (int i = 0;i<success.length();i++) {
                successLists.add(success.getString(i));
            }
            for (int i = 0;i<userLists.length();i++) {
                 userIds.add(userLists.getJSONObject(i).getString("uId"));
            }
            for (int i = 0;i<currentUserIds.size();i++) {
                currentUserIds.add(userData.get(i).getUId());
            }

            Log.e(TAG, "SIZE: "+userLists.length());
            Log.e(TAG, "나: " + me.getUId());
            Log.e(TAG, "마지막 element" + userIds.size() + " 는? " + userIds.get(userIds.size()-1));
            for (int i = 0;i<userIds.size();i++) {
                if (!currentUserIds.contains(userIds.get(i))) {
                    if (!userLists.getJSONObject(i).getString("gender").equals(me.getGender())
                            && !userIds.get(i).equals(me.getUId()) && !successLists.contains(userIds.get(i))
                            && !receivedLists.contains(userIds.get(i))) {
                        currentUserIds.add(userIds.get(i));
                        userData.add(gson.fromJson(userLists.getJSONObject(i).toString(), User.class));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        mView.findViewById(R.id.progressBar2).setVisibility(View.INVISIBLE);
        mView.findViewById(R.id.waitingText).setVisibility(View.INVISIBLE);
        if (userData == null || (userData != null && userData.size() == 0)) {
            mView.findViewById(R.id.findNewUser).setVisibility(View.VISIBLE);
        }
    }
}