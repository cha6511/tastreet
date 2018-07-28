package com.tastreet;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String LOGIN_ID = "LOGIN_ID";
    private static final String LOGIN_PW = "LOGIN_PW";
    private static final String AUTO_LOGIN = "AUTO_LOGIN";
    private static final String SAVE_ID = "SAVE_ID";



    public SharedPref(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoginId(String loginId){
        editor.putString(LOGIN_ID, loginId);
        editor.commit();
    }
    public String getLoginId(){
        return sharedPreferences.getString(LOGIN_ID, "");
    }

    public void setLoginPw(String loginPw){
        editor.putString(LOGIN_PW, loginPw);
        editor.commit();
    }
    public String getLoginPw(){
        return sharedPreferences.getString(LOGIN_PW, "");
    }

    public void setAutoLogin(boolean b){
        editor.putBoolean(AUTO_LOGIN, b);
        editor.commit();
    }
    public boolean getAutoLogin(){
        return sharedPreferences.getBoolean(AUTO_LOGIN, false);
    }

    public void setSaveId(boolean b){
        editor.putBoolean(SAVE_ID, b);
        editor.commit();
    }
    public boolean getSaveId(){
        return sharedPreferences.getBoolean(SAVE_ID, false);
    }
}
