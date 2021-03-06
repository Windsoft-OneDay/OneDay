package com.windsoft.oneday;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

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
    public static final String KEY_GOOD = "good";
    public static final String KEY_BAD = "bad";
    public static final String KEY_FLAG = "flag";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_SHOW_COMMENT = "showComment";
    public static final String KEY_GET_PHOTO = "getPhoto";
    public static final String KEY_SET_PHOTO = "setImage";
    public static final String KEY_SIGN_OUT = "signOut";
    public static final String KEY_LOG_OUT = "logout";
    public static final String KEY_FIND_ID = "findId";
    public static final String KEY_FIND_PW = "findPw";
    public static final String KEY_SET_PW = "setPw";
    public static final String KEY_UPDATE_NOTICE = "updateNotice";
    public static final String KEY_REMOVE_NOTICE = "removeNotice";

    public static final String VALUE_CONNECT = "connection";

    public static final int FACEBOOK = 0;
    public static final int NAVER = 1;
    public static final int ONE_DAY = 2;

    public static final String KEY_USER_ID = "userId";
    public static final String KEY_NOTICE_ID = "noticeId";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_PW = "userPw";
    public static final String KEY_USER_MAIL = "userMail";
    public static final String KEY_USER_BIRTH = "userBirth";
    public static final String KEY_USER_IMAGE = "userImage";
    public static final String KEY_NOTICE = "notice";
    public static final String KEY_POSITION = "position";
    public static final String KEY_COMMENT_POSITION = "commentPosition";


    public static final String PREF_KEY = "windsoft-oneday";
    public static SharedPreferences pref;
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
    public static final int CODE_NOT_ENOUGH_NOTICE = 309;             // 글 db 부족
    public static final int CODE_GOOD_BAD_UPDATE_NOTICE_ERR = 310;             // 좋아요, 싫어요 클릭 시 noticeDB 수정 에러
    public static final int CODE_GOOD_BAD_UPDATE_USER_ERR = 311;             // 좋아요, 싫어요 클릭 시 userDB 수정 에러
    public static final int CODE_COMMENT_UPDATE_USER_ERR = 312;             // 댓글 userDB 수정 에러
    public static final int CODE_COMMENT_UPDATE_NOTICE_ERR = 313;             // 댓글 userDB 수정 에러
    public static final int CODE_GET_PROFILE_FIND_USER_ERR = 314;             // 프로필 요청 userDB 검색 에러
    public static final int CODE_GET_PROFILE_UPDATE_NOTICE_ERR = 315;             // 프로필 요청 noticeDB 검색 에러
    public static final int CODE_SET_IMAGE_UPDATE_USER_ERR = 316;             // 프로필 사진 변경 요청 userDB 수정 에러
    public static final int CODE_SET_IMAGE_UPDATE_NOTICE_ERR = 317;             // 프로필 사진 변경 요청 noticeDB 수정 에러
    public static final int CODE_SIGN_OUT_ERROR = 318;             // 계정 삭제 에러
    public static final int CODE_FIND_ID_USER_ERR = 319;             // 아이디 찾기 userDB 검색 에러
    public static final int CODE_FIND_PW_USER_ERR = 320;             // 비밀번호 찾기 userDB 검색 에러
    public static final int CODE_FIND_ID_NULL = 321;             // 아이디 찾기 userDB 없음
    public static final int CODE_FIND_PW_NULL = 322;             // 비밀번호 찾기 userDB 없음
    public static final int CODE_SET_PW_ERR = 323;             // 비밀번호 설정 에러
    public static final int CODE_UPDATE_NOTICE_ERR = 324;             // 글 수정 에러
    public static final int CODE_REMOVE_NOTICE_ERR = 325;             // 글 삭제 에러

    public static Bitmap decodeImage(String str) {
        byte[] array = Base64.decode(str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }


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
