package com.windsoft.oneday.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;
import com.windsoft.oneday.fragment.LoginFragment;
import com.windsoft.oneday.login.FacebookLogin;
import com.windsoft.oneday.login.NaverLogin;

/**
 * Created by dongkyu Lee on 2015-08-02.
 * */
public class LoginActivity extends FragmentActivity implements FacebookLogin.OnFacebookLoginHandler, NaverLogin.OnNaverLoginHandler {

    private static final String TAG = "LoginActivity";

    private FacebookLogin facebookLogin;                    // 페이스북 로그인 클래스
    private NaverLogin naverLoginLogin;                    // 네이버 로그인 클래스

    private LoginFragment loginFragment;                    // 로그인 프레그먼트


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }


    /**
     * TODO: 생성자
     * */
    private void init() {
        facebookLogin = new FacebookLogin(this);
        naverLoginLogin = new NaverLogin(this);

        // 로그인 프레그먼트 부착
        loginFragment = LoginFragment.createInstance(facebookLogin, naverLoginLogin);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_container, loginFragment)
                .commit();


    }


    @Override
    protected void onResume() {
        super.onResume();
        facebookLogin.activeApp();              // 페북 로그인 실행
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        facebookLogin.deactiveApp();            // 페북 로그인 종료
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookLogin.activityCallback(requestCode, resultCode, data);
    }


    @Override
    public void OnFacebookLogin(String token, String id) {
        intentLoginData(id, Global.FACEBOOK);
    }


    @Override
    public void OnNaverLogin(String email) {
        intentLoginData(email, Global.NAVER);
    }


    private void intentLoginData(String id, int cond) {
        Intent intent = new Intent(LoginActivity.this, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_LOGIN);
        intent.putExtra(Global.KEY_LOGIN_ID, id);
        intent.putExtra(Global.KEY_LOGIN_TYPE, Global.FACEBOOK);
        startService(intent);
    }
}
