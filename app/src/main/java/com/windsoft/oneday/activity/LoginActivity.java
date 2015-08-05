package com.windsoft.oneday.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.github.nkzawa.socketio.client.Socket;
import com.nispok.snackbar.Snackbar;
import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;
import com.windsoft.oneday.SocketIO;
import com.windsoft.oneday.fragment.LoginFragment;
import com.windsoft.oneday.fragment.SignUpFragment;
import com.windsoft.oneday.fragment.SplashFragment;
import com.windsoft.oneday.login.FacebookLogin;
import com.windsoft.oneday.login.NaverLogin;

/**
 * Created by dongkyu Lee on 2015-08-02.
 * */
public class LoginActivity extends FragmentActivity implements FacebookLogin.OnFacebookLoginHandler, NaverLogin.OnNaverLoginHandler
                , LoginFragment.OnLoginHandler, SplashFragment.OnSplashHandler, SignUpFragment.OnSignUpHandler{

    private static final String TAG = "LoginActivity";

    private FacebookLogin facebookLogin;                    // 페이스북 로그인 클래스
    private NaverLogin naverLoginLogin;                    // 네이버 로그인 클래스

    private LoginFragment loginFragment;                    // 로그인 프레그먼트
    private SplashFragment splashFragment;                    // 로그인 프레그먼트

    private SignUpFragment signUpFragment;                  //회원가입 프레그먼트

    private String id;              // 자동로그인 아이디
    private String pw;              // 자동로그인 패스워드

    private boolean isLoginShowed = false;
    private boolean isSplashShowed2sec = false;
    private boolean isConnected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        init();
    }


    /**
     * TODO: 생성자
     * */
    private void init() {
        OneDayService.createInstance(getApplicationContext());

        facebookLogin = new FacebookLogin(this);
        naverLoginLogin = new NaverLogin(this);

        // 로그인 프레그먼트 부착
        loginFragment = LoginFragment.createInstance(facebookLogin, naverLoginLogin);
        splashFragment = new SplashFragment();

        /*프래그먼트 부착 소스*/
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_login_container, splashFragment)
                .commit();

        signUpFragment = new SignUpFragment();


        // 자동 로그인 허용된 아이디 탐색
        getAutoLogin();
    }


    /**
     * TODO: 자동로그인
     * */
    private void getAutoLogin() {
        Global.pref = getSharedPreferences(Global.PREF_KEY, MODE_PRIVATE);
        id = Global.pref.getString(Global.KEY_USER_ID, null);
        pw = Global.pref.getString(Global.KEY_USER_PW, null);

        Intent intent = new Intent(LoginActivity.this, OneDayService.class);
        if (id != null) {           // 자동 로그인 허용 된 아이디가 있다면
            intent.putExtra(Global.KEY_COMMAND, Global.KEY_LOGIN);              // 로그인 요청
            intent.putExtra(Global.KEY_LOGIN_ID, id);                           // 아이디 전송
            if (pw != null) {                   // 비밀번호 있다면
                intent.putExtra(Global.KEY_LOGIN_PW, pw);                       // 비밀번호 전송
                intent.putExtra(Global.KEY_LOGIN_TYPE, Global.ONE_DAY);         // 타입 = 원데이
            } else {
                intent.putExtra(Global.KEY_LOGIN_TYPE, Global.FACEBOOK);        // 타입 = 페이스북
            }
        }
        startService(intent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            String command = intent.getStringExtra(Global.KEY_COMMAND);
            if (command.equals(Global.VALUE_CONNECT)) {
                processConnection();
            } else if (command.equals(Global.KEY_LOGIN)) {
                int cond = intent.getIntExtra(Global.KEY_COND, Global.NULL);
                String id = intent.getStringExtra(Global.KEY_USER_ID);
                String pw = intent.getStringExtra(Global.KEY_USER_PW);
                processLogin(cond, id, pw);
            }
        }

        super.onNewIntent(intent);
    }


    /**
     * TODO: 로그인 응답 처리
     * @param cond : 상태
     *             NULL = 실패
     *             SUCCESS = 성공
     * @param id : 아이디
     * @param pw : 패스워드
     * */
    private void processLogin(int cond, String id, String pw) {
        if (cond == Global.NULL) {                      // 로그인 실패 시
            Snackbar.with(getApplicationContext())      // 스낵바 띄우기
                    .text(R.string.login_null)
                    .showAnimation(true)
                    .show(this);
        } else if (cond == Global.SUCCESS) {            // 로그인 성공 시
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);     // 메인 액티비티로 이동
            startActivity(intent);
            finish();

            Global.editor = Global.pref.edit();                     // 자동로그인 데이터 설정
            Global.editor.putString(Global.KEY_USER_ID, id);
            Global.editor.putString(Global.KEY_USER_PW, pw);
            Global.editor.commit();
        }
    }


    /**
     * TODO: 서버와 연결되었을 때 실행
     * */
    private void processConnection() {
        if (!isLoginShowed && isSplashShowed2sec) {
            /*프래그먼트 변경 소스*/
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_login_container, loginFragment)
                    .commit();
            isLoginShowed = true;
        }
        isConnected = true;
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
    public void onFacebookLogin(String token, String id) {
        intentLoginData(id, null, Global.FACEBOOK);
    }


    @Override
    public void onNaverLogin(String email) {
        intentLoginData(email, null, Global.NAVER);
    }


    @Override
    public void onLoginReq(String id, String pw) {
        intentLoginData(id, pw, Global.ONE_DAY);
    }


    @Override
    public void onIntentSignUp() {
        // 회원가입 버튼 눌렀을 때
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_login_container, signUpFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSplash() {                    // 스플레시 보이고 2초 뒤 실행
        Socket socket = SocketIO.getSocket();
        if (socket != null)
            isConnected = socket.connected();
        isSplashShowed2sec = true;
        if (!isLoginShowed && isConnected) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_login_container, loginFragment)
                    .commit();
            isLoginShowed = true;
        }
    }


    @Override
    public void onSignUp(String id, String pw) {
        Intent intent = new Intent(LoginActivity.this, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_SIGN_UP);
        intent.putExtra(Global.KEY_USER_ID, id);
        intent.putExtra(Global.KEY_USER_PW, pw);
        startService(intent);
    }


    private void intentLoginData(String id, String pw, int cond) {
        Intent intent = new Intent(LoginActivity.this, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_LOGIN);
        intent.putExtra(Global.KEY_LOGIN_ID, id);
        intent.putExtra(Global.KEY_LOGIN_PW, pw);
        intent.putExtra(Global.KEY_LOGIN_TYPE, cond);
        startService(intent);
    }
}
