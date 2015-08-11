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
    public static final String KEY_CODE = "code";
    public static final String KEY_SIGN_UP = "signUp";
    public static final String KEY_GET_PROFILE = "profile";
    public static final String KEY_SET_NAME = "setName";
    public static final String KEY_READ_NOTICE = "readNotice";
    public static final String KEY_POST_NOTICE = "postNotice";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_COUNT = "count";
    public static final String KEY_KEY_WORD = "keyWord";

    public static final String VALUE_CONNECT = "connection";

    public static final int FACEBOOK = 0;
    public static final int NAVER = 1;
    public static final int ONE_DAY = 2;

    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_PW = "userPw";
    public static final String KEY_USER_MAIL = "userMail";
    public static final String KEY_USER_BIRTH = "userBirth";
    public static final String KEY_USER_IMAGE = "userImage";
    public static final String KEY_NOTICE = "notice";


    public static final String PREF_KEY = "windsoft-oneday";
    public static SharedPreferences pref;                     // �ڵ� �α���
    public static SharedPreferences.Editor editor;


    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ID_ALREADY = 300;
    public static final int CODE_SIGN_UP_FAIL = 301;
    public static final int CODE_READ_NOTCIE_FAIL = 302;
    public static final int CODE_SET_NAME_FAIL = 303;
    public static final int CODE_LOGIN_NO_ID = 304;
    public static final int CODE_LOGIN_ERR = 305;
    public static final int CODE_POST_ERR = 306;
    public static final int CODE_NAME_ALREADY = 307;
    public static final int CODE_USER_ADD_NOTICE = 308;             // 사용자 DB에 글 목록 추가 에러


//    public static byte[] bitmapToByte(Bitmap bitmap) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//        return outputStream.toByteArray();
//    }
//
//
//    public static String byteToBase64(byte[] array) {
//        return Base64.encodeToString(array, Base64.DEFAULT);
//    }
}
