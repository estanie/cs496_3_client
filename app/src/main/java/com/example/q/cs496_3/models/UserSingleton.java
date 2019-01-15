package com.example.q.cs496_3.models;

import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserSingleton {
    public final String TAG = "UserSingleton";
    public static UserSingleton instance;
    private User user;
    private Map<String, Boolean> isMyStyleList = new HashMap<>();
    private UserSingleton() { }
    static {
        try {
            instance = new UserSingleton();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMyStyleTrue(String uId) {
        Log.e(TAG, "Set" + uId);
        isMyStyleList.put(uId, true);
    }

    public boolean getMyStyle(String uId) {
        Log.e(TAG, "Get" + uId);
        if (isMyStyleList.get(uId) != null) return true;
        return false;
    }
    public static UserSingleton getInstance() {
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
        if (user.getAge() == null) {
            user.setAge(calculateAge(user.getDate_of_birth()));
        }
    }
    public User getUser() {
        return user;
    }

    private String calculateAge(String date) {
        int age = 0;
        int myYear = 0;
        String[] dates = date.split("/");
        if (dates[0].length() == 4) myYear = Integer.parseInt(dates[0]);
        else if (dates[1].length() == 4) myYear = Integer.parseInt(dates[1]);
        else if (dates[2].length() == 4) myYear = Integer.parseInt(dates[2]);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        age = thisYear - myYear + 1;
        Log.e("MY AGE", ""+age);
        return ""+age;
    }
}
