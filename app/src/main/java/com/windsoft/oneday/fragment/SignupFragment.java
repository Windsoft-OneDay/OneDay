package com.windsoft.oneday.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.windsoft.oneday.R;

/**
 * Created by kim on 2015-08-04.
 */
public class SignUpFragment extends Fragment {
    NumberPicker year;
    NumberPicker month;
    NumberPicker day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        init(rootView);

        return rootView;
    }

    private void init(View rootView) {
        setNumberPicker(rootView);
    }


    /**
     * TODO; 생일 입력 NumberPicker 세팅
     * */
    private void setNumberPicker(View rootView) {
        year = (NumberPicker) rootView.findViewById(R.id.fragment_sign_up_year);
        month = (NumberPicker) rootView.findViewById(R.id.fragment_sign_up_month);
        day = (NumberPicker) rootView.findViewById(R.id.fragment_sign_up_day);

        year.setMinValue(1950);
        year.setMaxValue(2015);
        year.setWrapSelectorWheel(false);

        month.setMinValue(1);
        month.setMaxValue(12);
        month.setWrapSelectorWheel(false);
    }


//      비밀번호 난이도 검사
//    int cond = Secure.checkPasswordSecureLevel(pw);
//    if (cond == Secure.SUCCESS) {
//        sender.onLoginReq(id, pw);              // 로그인 요청
//    } else if (cond == Secure.NOT_ENOUGH_LETTER) {
//        Snackbar.with(getActivity())
//                .text(R.string.login_not_enough_letter)
//                .showAnimation(true)
//                .show(getActivity());
//    } else if (cond == Secure.NO_SPECIAL_LETTER) {
//        Snackbar.with(getActivity())
//                .text(R.string.login_no_special_letter)
//                .showAnimation(true)
//                .show(getActivity());
//    }
}