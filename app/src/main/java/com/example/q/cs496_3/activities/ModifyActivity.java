package com.example.q.cs496_3.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
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
import com.example.q.cs496_3.models.User;
import com.facebook.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import info.hoang8f.android.segmented.SegmentedGroup;

public class ModifyActivity extends AppCompatActivity {
    public final int IMAGE_PICK = 100;
    public final int REQUEST_CODE = 1;
    public final String TAG = "ModifyActivity";
    private String id;
    private String name;
    private String gender;
    private String birthday;
    private RadioButton male, female;
    private boolean isMember;
    private boolean isPhotoChange = false;
    private boolean isUserStyleSelected = false;
    private String photo;
    public String path;
    public File f;
    public String file_name;
    public ImageView editPhoto;
    JSONObject json;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    @Override
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Log.d(TAG, idToken);
                        }
                    }
                });
                */
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
            Log.e(TAG, "IS MEMBER");
            try {
                String str = getMyRequest.execute(myUrl).get();
                JSONObject myJsonObj = new JSONObject(str);
                Gson gson = new GsonBuilder().create();
                String jsons = myJsonObj.getString("member");
                if (!myJsonObj.getJSONObject("member").getJSONArray("style").toString().equals("[]")) {
                    isUserStyleSelected = true;
                }
                User user = gson.fromJson(jsons, User.class);
                name = user.getName();
                gender = user.getGender();
                birthday = user.getDate_of_birth();
                editContact.setText(user.getContact());
                editResidence.setText(user.getResidence());
                editJob.setText(user.getJob());
                editHobby.setText(user.getHobby());
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
            birthday = changeOrder(birthday);
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
        // TODO(estanie): 직접 사진 찍어 올리는 기능도 있으면 좋겠다.
        editPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO PHOTO SELECT 화면으로 넘어가는 기능
                Intent fintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                fintent.setType("image/*");
                Log.e(TAG, "Select the images.");
                startActivityForResult(
                        Intent.createChooser(fintent, "사진을 선택해주세요."), IMAGE_PICK);
            }
        });

        // 완료 버튼 클릭 시
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.modifyConfirm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //데이터 유효성 검사 Text부분
                if(notAllWritten()){
                    Toast toast = Toast.makeText(
                            getApplicationContext(), R.string.info_not_found, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                String gender;
                if (female.isChecked()){
                    gender = "female";
                }else {
                    gender = "male";
                }

                // TODO(estanie): 기존 유저이면서 다른 핸드폰으로 가면 수정하기 전까지는 전 핸드폰에 알람감.
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.e(TAG, token);
                User user = new User(null, editName.getText().toString(), gender,null,
                        editResidence.getText().toString(), editContact.getText().toString(),
                        editJob.getText().toString(), editHobby.getText().toString(),
                        null, id, birthday, 0 , token);

                //데이터 유효성 검사 Photo부분, 신규가입이거나 사진변경을 했으면 확인해야함
                if (!isMember || isPhotoChange) {
                    try {
                        f = new File(path);
                        file_name = f.getName();
                        user.setPhoto(file_name);
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
                        Toast toast = Toast.makeText(getApplicationContext(),
                                R.string.face_not_found, Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                }

                // 여기가 데이터 보내는 부분. 아래있는 형식대로 데이터를 넘기면 된다.
                try {
                    Gson gson = new GsonBuilder().create();
                    String str = gson.toJson(user);

                    //http에 넣을 수 있는 형식으로 만들기
                    //httprequestclass 로 보내서 실행시키기
                    if (isMember) {
                        new HttpPatchRequest(str,id).execute();
                    }else{
                        Log.e(TAG, str);
                        new HttpPostRequest(str).execute();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                // 원래 멤버일 경우 이전 페이지로 이동. 아닌 경우, 마음에 드는 얼굴 찾는 페이지로 이동.
                if (isMember && isUserStyleSelected) {
                    startActivity(new Intent(
                            ModifyActivity.this, FragmentActivity.class));
                } else {
                    startActivity(new Intent(
                            ModifyActivity.this, SelectPictureActivity.class));
                }
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
        Log.e(TAG, "IMAGE DATA"+data);
        if (data == null)
            return;
        switch (requestCode) {
            case IMAGE_PICK:
                if (resultCode == RESULT_OK) {
                    if (checkIsFace(data.getData())) {
                        path = getRealPathFromURI(this, data.getData());
                        //file_name = f.getName();
                        Log.d(TAG, "IMAGE PATH: " + path);
                        Log.d(TAG, "DATA: " + data.getData());
                        Glide.with(this)
                                .load(data.getData())
                                .into(editPhoto);
                        //upload.setVisibility(View.VISIBLE);
                        isPhotoChange = true;
                    }
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

    private Boolean checkIsFace(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream inputStream;
        try {
            inputStream = getBaseContext().getContentResolver().openInputStream(uri);
        } catch(FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
        Bitmap myBitmap = null;
        try {
            myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch(IOException e){
            e.printStackTrace();
        }
        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).build();
        if (!faceDetector.isOperational()) {
            Toast.makeText(getApplicationContext(),
                    R.string.detector_not_avaliable, Toast.LENGTH_SHORT).show();
            return false;
        }
        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
        if (faces.size() == 0) {
            Toast.makeText(getApplicationContext(),
                    R.string.face_not_found, Toast.LENGTH_SHORT).show();
            return false;
        } else if (faces.size() > 1) {
            Toast.makeText(getApplicationContext(),
                    R.string.too_many_face, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
