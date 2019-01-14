package com.example.q.cs496_3.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.example.q.cs496_3.R;
import com.example.q.cs496_3.activities.ModifyActivity;
import com.example.q.cs496_3.adapters.ImageAdapter;
import com.example.q.cs496_3.activities.MainActivity;
import com.example.q.cs496_3.https.HttpGetRequest;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static com.facebook.FacebookSdk.getApplicationContext;

public class InformationFragment extends Fragment implements View.OnClickListener {
    Animation fab_open, fab_close;
    Boolean isFabOpen = false;
    FloatingActionButton fab, fab1, fab2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        ImageView viewImage = view.findViewById(R.id.infoImage);
        TextView viewName = (TextView) view.findViewById(R.id.infoName);
        TextView viewGender = (TextView) view.findViewById(R.id.infoGender);
        TextView viewAge = (TextView) view.findViewById(R.id.infoAge);
        TextView viewContact = (TextView) view.findViewById(R.id.infoContact);
        TextView viewResidence = (TextView) view.findViewById(R.id.infoResidence);
        TextView viewJob = (TextView) view.findViewById(R.id.infoJob);
        TextView viewHobby = (TextView) view.findViewById(R.id.infoHobby);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) view.findViewById(R.id.infoEdit);
        fab2 = (FloatingActionButton) view.findViewById(R.id.logoutButton);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        String name="";
        String gender="";
        int age=0;
        String contact="";
        String residence="";
        String job="";
        String hobby="";
        String photo="";
        //String to place our result in
        String get_result;
        //Instantiate new instance of our class GET
        HttpGetRequest getRequest = new HttpGetRequest();
        
        //Some url endpoint that you may have
        String id = Profile.getCurrentProfile().getId();
        //TODO "17"부분을 id로 바꾸면 진행된다.
        String myUrl = "http://143.248.140.106:2580/members/"+id;
        String mUrl = "http://143.248.140.106:2580/members/";
        //Perform the doInBackground method, passing in our url
        
        try {
            get_result = getRequest.execute(myUrl).get();
            if (get_result != null) {
                JSONObject jsonObj = new JSONObject(get_result);
                //.getJSONObject("member");
                JSONObject member = jsonObj.getJSONObject("member");
                name = member.getString("name");
                gender = member.getString("gender");
                age = member.getInt("age");
                contact = member.getString("contact");
                residence = member.getString("residence");
                job = member.getString("job");
                hobby = member.getString("hobby");
                photo = member.getString("photo");
                Log.d("PHOTO", photo);
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
        //TODO : 아래 ""안에 데이터베이스에서 꺼낸 값을 넣어줘야한다.
        /*
        viewImage.setImageResource("이미지 위치찾기");
        */


        Uri uri = null;
        ImageAdapter imageAdapter = new ImageAdapter(getContext(), uri);
        //ImageView imageView = new ImageView(getContext());
        RequestManager requestManager = Glide.with(imageAdapter.getContext());
        // Create request builder and load image.
        RequestBuilder requestBuilder = requestManager.load("http://143.248.140.106:2980/uploads/"+photo);
        //requestBuilder = requestBuilder.apply(new RequestOptions().override(250, 250));
        // Show image into target imageview.
        requestBuilder.into(viewImage);
        viewImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        //images.add(imageView);


        viewName.setText(name);
        viewGender.setText(gender);
        viewAge.setText(""+age);
        viewContact.setText(contact);
        viewResidence.setText(residence);
        viewJob.setText(job);
        viewHobby.setText(hobby);

/*
        //FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Edit화면으로 넘어감.
                Intent intent=new Intent(getActivity(),ModifyActivity.class);
                intent.putExtra("isMember", "yes");
                startActivity(intent);
                getActivity().finish();
            }
        });

        //TODO 간이용 Logout 버튼 추후 수정 예정
        Button logout = (Button) view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MainActivity.class);
                LoginManager.getInstance().logOut();
                startActivity(intent);
                getActivity().finish();
            }
        });*/

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id) {
            case R.id.fab:
                anim();
                //Toast.makeText(this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.infoEdit:
                anim();
                intent=new Intent(getActivity(),ModifyActivity.class);
                intent.putExtra("isMember", "yes");
                startActivity(intent);
                getActivity().finish();
                //Toast.makeText(this, "Button1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logoutButton:
                anim();
                intent=new Intent(getActivity(),MainActivity.class);
                LoginManager.getInstance().logOut();
                startActivity(intent);
                getActivity().finish();
                //Toast.makeText(this, "Button2", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }
}