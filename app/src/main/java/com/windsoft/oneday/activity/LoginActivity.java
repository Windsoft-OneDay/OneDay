package com.windsoft.oneday.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.nispok.snackbar.Snackbar;
import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;
import com.windsoft.oneday.SocketIO;
import com.windsoft.oneday.fragment.FindIdFragment;
import com.windsoft.oneday.fragment.FindPwFragment;
import com.windsoft.oneday.fragment.LoginFragment;
import com.windsoft.oneday.fragment.SignupFragment;
import com.windsoft.oneday.fragment.SplashFragment;
import com.windsoft.oneday.login.FacebookLogin;
import com.windsoft.oneday.login.NaverLogin;

/**
 * Created by dongkyu Lee on 2015-08-02.
 * */
public class LoginActivity extends FragmentActivity implements FacebookLogin.OnFacebookLoginHandler, NaverLogin.OnNaverLoginHandler
                , LoginFragment.OnLoginHandler, SplashFragment.OnSplashHandler, SignupFragment.OnSignUpHandler, FindIdFragment.OnFindIdHandler{

    private static final String TAG = "LoginActivity";

    private FacebookLogin facebookLogin;                    // 페이스북 로그인 클래스
    private NaverLogin naverLoginLogin;                    // 네이버 로그인 클래스

    private LoginFragment loginFragment;                    // 로그인 프레그먼트
    private SplashFragment splashFragment;                    // 로그인 프레그먼트
    private FindIdFragment findIdFragment;                    // 아이디 찾기 프레그먼트
    private FindPwFragment findPwFragment;                    // 비밀번호호 찾기 프레그먼트

    private SignupFragment signUpFragment;                  //회원가입 프레그먼트

    private String pw;              // 자동로그인 패스워드

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
        OneDayService.createInstance(getApplicationContext());

        facebookLogin = new FacebookLogin(this);
        naverLoginLogin = new NaverLogin(this);

        // 로그인 프레그먼트 부착
        loginFragment = LoginFragment.createInstance(facebookLogin, naverLoginLogin);
        splashFragment = new SplashFragment();
        findIdFragment = new FindIdFragment();
        findPwFragment = new FindPwFragment();

        /*프래그먼트 부착 소스*/
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_login_container, splashFragment)
                .commit();

        signUpFragment = new SignupFragment();
        // 자동 로그인 허용된 아이디 탐색

        if (SocketIO.getSocket().connected())
            processConnection();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            String command = intent.getStringExtra(Global.KEY_COMMAND);
            if (command.equals(Global.VALUE_CONNECT)) {
                processConnection();
            } else if (command.equals(Global.KEY_LOGIN)) {
                int code = intent.getIntExtra(Global.KEY_CODE, -1);
                String id = intent.getStringExtra(Global.KEY_USER_ID);
                String name = intent.getStringExtra(Global.KEY_USER_NAME);
                String image = intent.getStringExtra(Global.KEY_USER_IMAGE);
                if (code != -1)
                    processLogin(code, id, name, image);
            } else if (command.equals(Global.KEY_SIGN_UP)) {                       // 회원가입 응답
                int code = intent.getIntExtra(Global.KEY_CODE, -1);
                if (code != -1)
                    processSignUp(code);
            } else if (command.equals(Global.KEY_FIND_ID)) {
                int code = intent.getIntExtra(Global.KEY_CODE, -1);
                String id = intent.getStringExtra(Global.KEY_USER_ID);
                if (code != -1)
                    processFindId(code, id);
            } else if (command.equals(Global.KEY_FIND_PW)) {
                int code = intent.getIntExtra(Global.KEY_CODE, -1);
                if (code != -1)
                    processFindPw(code);
            } else if (command.equals(Global.KEY_SET_PW)) {
                int code = intent.getIntExtra(Global.KEY_CODE, -1);
                if (code != -1)
                    processSetPw(code);
            }
        }

        super.onNewIntent(intent);
    }


    private void processSetPw(int code) {
        if (code == Global.CODE_SUCCESS) {
            Snackbar.with(this)
                    .text(R.string.success)
                    .show(this);

            getSupportFragmentManager().beginTransaction()
                    .remove(findPwFragment)
                    .commit();
        }
    }


    private void processFindPw(int code) {
        if (code == Global.CODE_SUCCESS) {
            findPwFragment.setPw();
        } else if (code == Global.CODE_FIND_PW_NULL) {
            Snackbar.with(this)
                    .text(R.string.find_id_null)
                    .show(this);
        } else {
            Snackbar.with(this)
                    .text(R.string.fail_again)
                    .show(this);
        }
    }


    private void processFindId(int code, String id) {
        if (code == Global.CODE_SUCCESS) {
            findIdFragment.setId(id);
        } else if (code == Global.CODE_FIND_ID_NULL) {
            Snackbar.with(this)
                    .text(R.string.find_id_null)
                    .show(this);
        } else {
            Snackbar.with(this)
                    .text(R.string.fail_again)
                    .show(this);
        }
    }



    /**
     * TODO: 회원가입 응답
     * @param cond : 응답 코드
     * */
    private void processSignUp(int cond) {
        if (cond == Global.CODE_ID_ALREADY) {                           // 아이디 이미 사용 시
            Snackbar.with(getApplicationContext())
                    .text(R.string.sign_up_id_already)
                    .show(this);
        } else if (cond == Global.CODE_SIGN_UP_FAIL) {                  // 회원가입 실패 시
            Snackbar.with(getApplicationContext())
                    .text(R.string.sign_up_fail)
                    .show(this);
        } else if (cond == Global.CODE_SUCCESS) {
            Snackbar.with(getApplicationContext())
                    .text(R.string.success)
                    .show(this);

            getSupportFragmentManager().beginTransaction()
                    .remove(signUpFragment)
                    .commit();
        }
    }


    /**
     * TODO: 로그인 응답 처리
     * @param code : 상태
     *             NULL = 실패
     *             SUCCESS = 성공
     * @param id : 아이디
     * @param image : 프로필 사진
     * */
    private void processLogin(int code, String id, String name, String image) {
        if (code == Global.CODE_LOGIN_NO_ID) {                      // 로그인 실패 시
            Snackbar.with(getApplicationContext())      // 스낵바 띄우기
                    .text(R.string.sign_up_null)
                    .showAnimation(true)
                    .show(this);
        } else if (code == Global.CODE_SUCCESS) {            // 로그인 성공 시
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);     // 메인 액티비티로 이동
            intent.putExtra(Global.KEY_USER_ID, id);
            intent.putExtra(Global.KEY_USER_NAME, name);
            intent.putExtra(Global.KEY_USER_IMAGE, image);
            startActivity(intent);
            finish();

            setAutoLogin(id);
        }
    }


    private void setAutoLogin(String id) {
        String curId = Global.pref.getString(Global.KEY_USER_ID, null);
        String curPw = Global.pref.getString(Global.KEY_USER_PW, null);

        if (curId == null && curPw == null) {                       // 저장된 정보가 없다면
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
        splashFragment.invisible();
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
        this.pw = pw;
    }


    @Override
    public void onIntentSignUp() {
        // 회원가입 버튼 눌렀을 때
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_login_container, signUpFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSplash() {                    // 스플레시 보이고 2초 뒤 실행
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_login_container, loginFragment)
                .commit();
    }


    @Override
    public void onSignUp(String id, String pw, String mail, long birth) {
        Intent intent = new Intent(LoginActivity.this, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_SIGN_UP);
        intent.putExtra(Global.KEY_USER_ID, id);
        intent.putExtra(Global.KEY_USER_PW, pw);
        intent.putExtra(Global.KEY_USER_MAIL, mail);
        intent.putExtra(Global.KEY_USER_BIRTH, birth);
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


    @Override
    public void onIntentFindId() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_login_container, findIdFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onIntentFindPw() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_login_container, findPwFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onIntentLogin() {
        getSupportFragmentManager().beginTransaction()
                .remove(findIdFragment)
                .commit();
    }
}
