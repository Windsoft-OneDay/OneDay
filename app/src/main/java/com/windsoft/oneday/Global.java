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
    public static final String KEY_SIGN_UP = "signUp";
    public static final String KEY_READ_NOTICE = "readNotice";
    public static final String KEY_COUNT = "count";
    public static final String KEY_KEY_WORD = "keyWord";

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
    public static SharedPreferences pref;                     // �ڵ� �α���
    public static SharedPreferences.Editor editor;


    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ID_ALREADY = 300;
    public static final int CODE_SIGN_UP_FAIL = 301;
    public static final int CODE_READ_NOTCIE_FAIL = 302;
}
