package com.example.q.cs496_3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.q.cs496_3.R;
import com.example.q.cs496_3.adapters.OtherAdapter;
import com.example.q.cs496_3.adapters.SelectPictureAdapter;
import com.example.q.cs496_3.https.HttpGetRequest;
import com.example.q.cs496_3.models.Image;
import com.example.q.cs496_3.models.User;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SelectPictureActivity extends AppCompatActivity {
    private final String TAG = "SelectPictureActivity";
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<User> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //맨위 TITEL_BAR 제거
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_picture);
        userData = new ArrayList<>();

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

        mRecyclerView = (RecyclerView) findViewById(R.id.imageGrid);
        mFab = (FloatingActionButton) findViewById(R.id.selectConfirm);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        // TODO(estanie): mImageData 테스트용임. Random 한 이미지 서버로부터 받기..


        mAdapter = new OtherAdapter(userData);
        mRecyclerView.setAdapter(mAdapter);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO(estanie): 10장 이상 골랐는지 확인하고 넘어가고, 데이터 서버에 보내서 계산하게 시키기.



                Intent intent = new Intent(SelectPictureActivity.this, FragmentActivity.class);
                startActivity(intent);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
