package com.example.q.cs496_3.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.example.q.cs496_3.R;
import com.example.q.cs496_3.adapters.ImageAdapter;
import com.example.q.cs496_3.https.HttpGetRequest;
import com.example.q.cs496_3.https.HttpPatchRequest;
import com.example.q.cs496_3.https.HttpPostRequest;
import com.facebook.Profile;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import cz.msebera.android.httpclient.entity.StringEntity;
import info.hoang8f.android.segmented.SegmentedGroup;

public class ModifyActivity extends AppCompatActivity {
    private String id;
    private String name;
    private String gender;
    private String contact;
    private String job;
    private String hobby;
    private String birthday;
    private String residence;
    private RadioButton male, female;
    private boolean isMember;
    private boolean isPhotoChange = false;
    private String photo;
    public String path;
    public File f;
    public String file_name;
    public ImageView editPhoto;
    public final int REQUEST_CODE = 1;
    JSONObject json;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //맨위 TITEL_BAR 제거
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.mypage_modify);

        loadOrRequestPermission();
    }

    public void loadOrRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            doLoad();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    public void doLoad()
    {
        setResult(RESULT_CANCELED);
        //layout과의 연결을 담당하는 부분
        editPhoto = (ImageView) findViewById(R.id.modifyImage);
        final TextView editName = (TextView) findViewById(R.id.modifyName);
        final SegmentedGroup editGender = (SegmentedGroup) findViewById(R.id.segmented);
        final TextView editBirthday = (TextView) findViewById(R.id.modifyBirthDay);
        final EditText editContact = (EditText) findViewById(R.id.modifyContact);
        final EditText editResidence = (EditText) findViewById(R.id.modifyResidence);
        final EditText editJob = (EditText) findViewById(R.id.modifyJob);
        final EditText editHobby = (EditText) findViewById(R.id.modifyHobby);
        male = (RadioButton)findViewById(R.id.radioMale);
        female = (RadioButton)findViewById(R.id.radioFemale);

        //초기 로그인 : id, name, birthday, gender 받기
        Intent intent=getIntent();
        if (intent.getStringExtra("isMember")!=null){
            isMember = true;
        }else{ isMember = false;}

        json = new JSONObject();


        //TODO 이미 회원인 경우 모든 데이터를 이전과 동일하게 채워넣는다.
        if (isMember){
            HttpGetRequest getMyRequest = new HttpGetRequest();
            id = Profile.getCurrentProfile().getId();
            String myUrl = "http://143.248.140.106:2580/members/"+id;
            try {
                JSONObject myJsonObj = new JSONObject(getMyRequest.execute(myUrl).get());
                json = myJsonObj.getJSONObject("member");
                name = json.getString("name");
                gender = json.getString("gender");
                birthday = json.getString("date_of_birth");
                contact = json.getString("contact");
                //아래부분은 공통정보가 아님
                residence = json.getString("residence");
                job = json.getString("job");
                hobby = json.getString("hobby");
                contact = json.getString("contact");
                photo = json.getString("photo");
                editContact.setText(contact);
                editResidence.setText(residence);
                editJob.setText(job);
                editHobby.setText(hobby);
                Uri uri = null;
                ImageAdapter imageAdapter = new ImageAdapter(editPhoto.getContext(), uri);
                //ImageView imageView = new ImageView(getContext());
                RequestManager requestManager = Glide.with(imageAdapter.getContext());
                // Create request builder and load image.
                RequestBuilder requestBuilder = requestManager.load("http://143.248.140.106:2980/uploads/"+photo);
                //requestBuilder = requestBuilder.apply(new RequestOptions().override(250, 250));
                // Show image into target imageview.
                requestBuilder.into(editPhoto);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{//TODO 이미 회원이 아닌 경우 indent에서 데이터를 가져와서 채워넣는다.
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            birthday = intent.getStringExtra("birthday");//생년월일 순서 정렬
            gender = intent.getStringExtra("gender");
        }

        //받아온 정보 입력
        editName.setText(name);
        editBirthday.setText(birthday);
        if (gender != null) {
            if (gender.equals("female")) {
                female.setChecked(true);
            } else {
                male.setChecked(true);
            }
        }

        //이미지 버튼 클릭시
        editPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO PHOTO SELECT 화면으로 넘어가는 기능
                Intent fintent = new Intent(Intent.ACTION_GET_CONTENT);
                fintent.setType("image/jpeg");
                try {
                    startActivityForResult(fintent, 100);
                } catch (ActivityNotFoundException e) {

                }
            }
        });

        //완료버튼 클릭시
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.modifyConfirm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //데이터 유효성 검사 Text부분
                if(notAllWritten()){
                    Toast toast = Toast.makeText(getApplicationContext(), "간절해야 이루어진다구", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                //gender check, 생년월일 -> 나이
                birthday = changeOrder(birthday);
                String gender;
                if (female.isChecked()){
                    gender = "female";
                }else {
                    gender = "male";
                }
                ArrayList<String> key = new ArrayList<String>(Arrays.asList(
                        "uId", "date_of_birth", "job", "gender", "contact", "residence","name","hobby"));
                ArrayList<String> value = new ArrayList<String>(Arrays.asList(id, birthday, editJob.getText().toString(), gender,
                        editContact.getText().toString(), editResidence.getText().toString(),
                        editName.getText().toString(), editHobby.getText().toString()));
                //데이터 유효성 검사 Photo부분, 신규인원이거나 사진변경을 했으면 확인해야함
                if (!isMember || isPhotoChange) {
                    try {
                        f = new File(path);
                        file_name = f.getName();
                        key.add("photo");
                        value.add(file_name);
                        Future uploading = Ion.with(ModifyActivity.this)
                                .load("http://143.248.140.106:2980/upload")
                                .setMultipartFile("image", f)
                                .asString()
                                .withResponse()
                                .setCallback(new FutureCallback<Response<String>>() {
                                    @Override
                                    public void onCompleted(Exception e, Response<String> result) {
                                        try {
                                            JSONObject jobj = new JSONObject(result.getResult());
                                            Toast.makeText(getApplicationContext(), jobj.getString("response"), Toast.LENGTH_SHORT).show();

                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }

                                    }
                                });
                    } catch (NullPointerException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "당.당.하.게.얼.굴.공.개", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                }

                // 여기가 데이터 보내는 부분. 아래있는 형식대로 데이터를 넘기면 된다.
                try {
                    //http에 넣을 수 있는 형식으로 만들기
                    //httprequestclass 로 보내서 실행시키기
                    if (isMember) {
                        JSONArray linkerList = new JSONArray();
                        for(int i=0; i<key.size(); i++) {
                            JSONObject a = new JSONObject();
                            a.put("propName", key.get(i));
                            a.put("value", value.get(i));
                            linkerList.put(a);
                        }
                        StringEntity json_string = new StringEntity(linkerList.toString());
                        new HttpPatchRequest(json_string,id).execute();
                    }else{
                        for(int i=0; i<key.size(); ++i){
                            json.put(key.get(i),value.get(i));
                        }
                        StringEntity json_string = new StringEntity(json.toString());
                        new HttpPostRequest(json_string).execute();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("ID!!!", id);
                Log.d("Name!!!", editName.getText().toString());
                Log.d("Gender!!!", gender);
                Log.d("Age!!!", editBirthday.getText().toString());
                Log.d("Contact!!!", editContact.getText().toString());
                Log.d("Residence!!!", editResidence.getText().toString());
                Log.d("Job!!!", editJob.getText().toString());
                Log.d("Hobby!!!", editHobby.getText().toString());


                //다음 Activity로 이동
                startActivity(new Intent(ModifyActivity.this, FragmentActivity.class));
                setResult(RESULT_OK);
                finish();
            }

            private boolean notAllWritten() {
                if (editContact.getText().toString().matches("")) return true;
                if (editResidence.getText().toString().matches("")) return true;
                if (editJob.getText().toString().matches("")) return true;
                if (editHobby.getText().toString().matches("")) return true;
                return false;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch(requestCode)
        {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    doLoad();
                }
                else
                {
                    Toast.makeText(this, "Need to allow access!", Toast.LENGTH_SHORT).show();
                    loadOrRequestPermission();
                }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    path = getRealPathFromURI(this,data.getData());
                    //file_name = f.getName();
                    Log.d(" Real Path : ", path);
                    editPhoto.setImageURI(data.getData());
                    //upload.setVisibility(View.VISIBLE);
                    isPhotoChange = true;
                }
        }
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    @NonNull
    private String changeOrder(String birthday) {
        String[] date = birthday.split("/");
        return date[2]+"/"+date[0]+"/"+date[1];
    }
}
