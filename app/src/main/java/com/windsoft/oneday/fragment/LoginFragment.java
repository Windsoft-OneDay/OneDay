package com.windsoft.oneday.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.facebook.login.widget.LoginButton;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.windsoft.oneday.R;
import com.windsoft.oneday.activity.LoginActivity;
import com.windsoft.oneday.login.FacebookLogin;
import com.windsoft.oneday.login.NaverLogin;

/**
 * Created by dongkyu Lee on 2015-08-02.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private LinearLayout snsContainer;          // 네이버, 페북 로그인 버튼 위치할 레이아웃

    private static FacebookLogin facebookLogin; // 페북 로그인 관리 클래스
    private static NaverLogin naverLogin;   // 네이버 로그인 관리 클래스

    private EditText idInput;               // 아이디 입력 상자
    private EditText pwInput;               // 비밀번호 입력 상자

    private String id;                      // 아이디 변수
    private String pw;                      // 비밀번호 변수

    private LoginButton facebookBtn;        // 페이스북 로그인 버튼
    private OAuthLoginButton naverBtn;      // 네이버 로그인 버튼
    private Button submit;                  // 로그인 버튼

    private Button signUpBtn;               // 회원가입 버튼

    private OnLoginHandler sender;

    public static final LoginFragment createInstance(FacebookLogin facebookLogin, NaverLogin naverLogin) {
        LoginFragment.facebookLogin = facebookLogin;
        LoginFragment.naverLogin = naverLogin;
        return new LoginFragment();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sender = (OnLoginHandler) activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        init(rootView);

        return rootView;
    }


    /**
     * TODO: 생성자
     * */
    private void init(View rootView) {
        facebookBtn = facebookLogin.getLoginButton();       // 페이스북 로그인 버튼 받아옴
        naverBtn = naverLogin.getLoginButton();

        snsContainer = (LinearLayout) rootView.findViewById(R.id.fragment_login_sns_container);
        snsContainer.addView(facebookBtn);                  // 페이스북 로그인 버튼 추가
        snsContainer.addView(naverBtn);                     // 네이버 로그인 버튼 추가

        submit = (Button) rootView.findViewById(R.id.fragment_login_submit);

        idInput = (EditText) rootView.findViewById(R.id.fragment_login_id);
        pwInput = (EditText) rootView.findViewById(R.id.fragment_login_pw);

        signUpBtn = (Button) rootView.findViewById(R.id.fragment_login_sign_up);

        setListener();      // 리스너 부착
    }


    /**
     * TODO: 리스너 부착
     * */
    private void setListener() {
        submit.setOnClickListener(new View.OnClickListener() {              // 로그인 버튼
            @Override
            public void onClick(View v) {
                id = idInput.getText().toString();
                pw = pwInput.getText().toString();

                if (id != null && id.length() > 0 && pw != null && pw.length() > 0) {       // 아이디, 비밀번호 모두 입력 되었다면
                    sender.onLoginReq(id, pw);
                }
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sender.onIntentSignUp();
            }
        });
    }


    public interface OnLoginHandler {
        void onLoginReq(String id, String pw);
        void onIntentSignUp();
    }
}