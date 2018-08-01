package com.tastreet;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    //안드로이드 내부 저장소 SharedPreferences
    //이 앱에서는 로그인 정보를 저장하는데 사용한다.
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String LOGIN_ID = "LOGIN_ID";
    private static final String LOGIN_PW = "LOGIN_PW";
    private static final String AUTO_LOGIN = "AUTO_LOGIN";
    private static final String SAVE_ID = "SAVE_ID";


    //초기화
    public SharedPref(Context context){
        this.context = context;
        //SharedPreferences 를 "pref" 라는 이름으로 만든다. //MODE_PRIVATE => 이 앱에서만 사용한다.
        sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        //SharedPreferences 에 내용을 저장하는 editor
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
