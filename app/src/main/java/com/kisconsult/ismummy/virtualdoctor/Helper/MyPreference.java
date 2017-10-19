package com.kisconsult.ismummy.virtualdoctor.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kisconsult.ismummy.virtualdoctor.Model.User;

/**
 * Created by ISMUMMY on 6/13/2016.
 */
public class MyPreference {
    private static final String KEY_USER = "user";
    private String TAG = MyPreference.class.getSimpleName();
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "virtualDoctor";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public MyPreference(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public void saveUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(KEY_USER, json);
        editor.commit();
    }

    public User getUser() {
        if (pref.getString(KEY_USER, null) != null) {
            Gson gson = new Gson();
            String json = pref.getString(KEY_USER, null);
            User user = gson.fromJson(json, User.class);
            return user;
        }
        return null;
    }
}
