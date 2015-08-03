package com.windsoft.oneday;

import android.content.SharedPreferences;

/**
 * Created by dongkyu Lee on 2015-08-02.
 */
public class Global {

    public static final String KEY_LOGIN = "login";
    public static final String KEY_LOGIN_TYPE = "loginType";
    public static final String KEY_LOGIN_ID = "loginId";
    public static final String KEY_LOGIN_PW = "loginPw";
    public static final String KEY_COMMAND = "command";
    public static final String KEY_COND = "cond";

    public static final String VALUE_CONNECT = "connection";

    public static final int FACEBOOK = 0;
    public static final int NAVER = 1;
    public static final int ONE_DAY = 2;

    public static final int NULL = 0;
    public static final int SUCCESS = 1;
    public static final int ERROR = -1;

    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_PW = "userPw";


    public static final String PREF_KEY = "windsoft-oneday";
    public static SharedPreferences pref;                     // 자동 로그인
    public static SharedPreferences.Editor editor;
}
