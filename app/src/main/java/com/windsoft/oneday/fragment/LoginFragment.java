package com.windsoft.oneday.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.login.widget.LoginButton;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.windsoft.oneday.R;
import com.windsoft.oneday.login.FacebookLogin;
import com.windsoft.oneday.login.NaverLogin;

/**
 * Created by dongkyu Lee on 2015-08-02.
 */
public class LoginFragment extends Fragment {

    private LinearLayout snsContainer;

    private static FacebookLogin facebookLogin;
    private static NaverLogin naverLogin;

    private LoginButton facebookBtn;
    private OAuthLoginButton naverBtn;

    public static final LoginFragment createInstance(FacebookLogin facebookLogin, NaverLogin naverLogin) {
        LoginFragment.facebookLogin = facebookLogin;
        LoginFragment.naverLogin = naverLogin;
        return new LoginFragment();
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
        snsContainer.addView(facebookBtn);
        snsContainer.addView(naverBtn);
    }
}