package com.example.q.cs496_3.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.q.cs496_3.https.HttpGetRequest;
import com.example.q.cs496_3.https.HttpPatchRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.q.cs496_3.R;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    //FB Login 구현
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken token;

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile","user_birthday", "user_friends","user_gender");
        token = AccessToken.getCurrentAccessToken();
        Log.e(TAG, "onCreate Start");

        //TODO 로그인 되어있다면, 회원일 경우(uid를 통해서 해결) 바로 information으로 이동한다. 회원이 아닌경우 로그아웃을 한다.
        if (token != null) {
            String id = Profile.getCurrentProfile().getId();
            Log.e(TAG, id);
            boolean isUser = false;
            try {
                HttpGetRequest getRequest = new HttpGetRequest();
                String myUrl = "http://143.248.140.106:2580/members";
                String get_result = getRequest.execute(myUrl).get();
                if (get_result != null) {
                    JSONObject jsonObj = new JSONObject(get_result);
                    JSONArray members = jsonObj.getJSONArray("members");

                    for (int i = 0; i < members.length(); ++i) {
                        JSONObject m = members.getJSONObject(i);
                        if (id.equals(m.getString("uId"))) {
                            isUser = true;
                        }
                    }
                }
            } catch (ExecutionException e) {
                Log.e(TAG, "Execution");
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG, "Interrupted");
            } catch (JSONException e) {
                Log.e(TAG, "JSON EXCEPTION: ");
                e.printStackTrace();
            }
            // USER면 바로 information activity로 이동
            if (isUser) {
                startActivity(new Intent(MainActivity.this, FragmentActivity.class));
                finish();
            }
            else {
                LoginManager.getInstance().logOut();
            }
        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    String id = object.getString("id");
                                    boolean isUser = false;
                                    try {
                                        HttpGetRequest getRequest = new HttpGetRequest();
                                        String myUrl = "http://143.248.140.106:2580/members";
                                        String get_result = getRequest.execute(myUrl).get();
                                        JSONObject jsonObj = new JSONObject(get_result);
                                        Log.e(TAG, "Get Result: " + get_result);
                                        JSONArray members = jsonObj.getJSONArray("members");
                                        // TODO(estanie): 로직 변경하기.
                                        for (int i = 0; i < members.length(); ++i) {
                                            JSONObject m = members.getJSONObject(i);
                                            if (id.equals(m.getString("uId"))){
                                                isUser = true;
                                            }
                                        }
                                    } catch (ExecutionException e) {
                                        Log.e(TAG, "Execution");
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "Interrupted");
                                    } catch (JSONException e) {
                                        Log.e(TAG, "JSON EXCEPTION: ");
                                        e.printStackTrace();
                                    }
                                    //USER면 바로 information activity로 이동
                                    if (isUser){
                                        String token = FirebaseInstanceId.getInstance().getToken();
                                        String str = new JSONObject().put("token", token).toString();
                                        Log.e(TAG, str);
                                        new HttpPatchRequest(str, id).execute();
                                        startActivity(new Intent(MainActivity.this, FragmentActivity.class));
                                        finish();
                                        //USER가 아니면 회원가입 페이지로 이동
                                    }else {
                                        Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
                                        intent.putExtra("id", object.getString("id"));
                                        intent.putExtra("name", object.getString("name"));
                                        intent.putExtra("birthday", object.getString("birthday"));
                                        intent.putExtra("gender", object.getString("gender"));
                                        startActivityForResult(intent,1);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,birthday,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {
                Log.d("TAG", "Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1 :
                if(resultCode==RESULT_OK) {
                    finish();
                    break;
                }else{
                    String id = Profile.getCurrentProfile().getId();
                    try {
                        String str = new JSONObject().put("token", "").toString();
                        Log.e(TAG, str);
                        new HttpPatchRequest(str, id).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LoginManager.getInstance().logOut();
                    break;
                }

            default :
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;

        }
    }
}