package com.windsoft.oneday.login;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by dongkyu Lee on 2015-06-17.
 * TODO: 페이스북 로그인 클래스
 */
public class FacebookLogin {

    private static final String TAG = "FacebookLogin";

    private CallbackManager callback;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private Activity activity;
    private OnFacebookLoginHandler sender;

    private LoginButton loginButton;

    private AccessToken token;

    public FacebookLogin(Activity activity) {
        this.activity = activity;
        sender = (OnFacebookLoginHandler) activity;
        init();
    }


    public LoginButton getLoginButton() {
        return loginButton;
    }


    private void init() {
        FacebookSdk.sdkInitialize(activity);
        loginButton = new LoginButton(activity);

        callback = CallbackManager.Factory.create();

        /**
         * TODO: 프로필 변경 시
         * @params oldProfile : 변경 전 프로필;
         * @params newProfile : 변경 후 프로필;
         * */
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                if (oldProfile != null) {
                    Log.d(TAG, "oldName = " + oldProfile.getName());
                    Log.d(TAG, "oldId = " + oldProfile.getId());
                }

                if (newProfile != null) {
                    Log.d(TAG, "newName = " + newProfile.getName());
                    Log.d(TAG, "newId = " + newProfile.getId());
                }
            }
        };


        /**
         * TODO: 토큰 변경 시
         * @params oldToken : 변경 전 토큰
         * @params newTOken : 변경 후 토큰
         * */
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                if (oldToken != null) {     // 로그아웃
                    Log.d(TAG, "oldToken.token = " + oldToken.getToken());
                    Log.d(TAG, "oldToken.id = " + oldToken.getUserId());
                }

                if (newToken != null) {     // 로그인
                    Log.d(TAG, "new.token = " + newToken.getToken());
                    Log.d(TAG, "new.id = " + newToken.getUserId());
                    sender.OnFacebookLogin(newToken.getToken(), newToken.getUserId());
                }
            }
        };

        token = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "토큰 = " + token);


        if (loginButton != null) {
            loginButton.setReadPermissions("public_profile");

            loginButton.registerCallback(callback, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                }

                @Override
                public void onCancel() {
                    Toast.makeText(activity, "취소 되었습니다. 다시 시도 해주십시오.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException e) {
                    Toast.makeText(activity, "다시 시도 해주십시오.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
            }
        });
    }


    public void activityCallback(int requestCode, int resultCode, Intent data) {
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * TODO: 검색 종료
     * */
    public void stopTracking() {
        if (accessTokenTracker != null)
            accessTokenTracker.stopTracking();


        if (profileTracker != null)
            profileTracker.stopTracking();
    }


    /**
     * TODO: 프래그먼트에서 사용 시 설정해야함.
     * */
    public void setFragment(Fragment fragment) {
        loginButton.setFragment(fragment);
    }



    /**
     * TODO: 앱 실행 시 페북 로그인도 실행
     * */
    public void activeApp() {
        AppEventsLogger.activateApp(activity);
    }



    /**
     * TODO: 앱 종료 시 페북 로그인도 종료
     * */
    public void deactiveApp() {
        AppEventsLogger.deactivateApp(activity);
    }



    /**
     * TODO: 페이스북 로그인 성공 인터페이스
     * */
    public interface OnFacebookLoginHandler {
        void OnFacebookLogin(String token, String id);
    }
}
