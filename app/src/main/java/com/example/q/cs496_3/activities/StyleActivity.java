package com.example.q.cs496_3.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;

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

public class StyleActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManger;
    private ArrayList<User> userData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.entry_style_select);
        String id = Profile.getCurrentProfile().getId();
        String mUrl = "http://143.248.140.106:2580/members/";
        String myUrl = mUrl + id;

        String myGender;
        String gender="";
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
            Log.d("my_result", get_my_result);
            JSONObject myJsonObj = new JSONObject(get_my_result);
            //.getJSONObject("member");
            JSONObject member = myJsonObj.getJSONObject("member");
            myGender = member.getString("gender");

            get_result = getMyRequest.execute(mUrl).get();
            JSONObject jsonObj = new JSONObject(get_result); //

            // Getting JSON Array node
            JSONArray members = jsonObj.getJSONArray("members");

            for (int i = 0; i < members.length(); ++i) {
                JSONObject m = members.getJSONObject(i);
                gender = m.getString("gender");
                id = m.getString("uId");
                if (!gender.equals(myGender)) {
                    photo = m.getString("photo");
                    User user1 = new User();
                    user1.setPhoto(photo);
                    user1.setUId(id);
                    user1.setIsStyleSet(1);
                    userData.add(user1);
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

        mRecyclerView = findViewById(R.id.othersRecyclerView);

        mLayoutManger = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManger);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new OtherAdapter(userData, true);
        mRecyclerView.setAdapter(mAdapter);
    }

}
