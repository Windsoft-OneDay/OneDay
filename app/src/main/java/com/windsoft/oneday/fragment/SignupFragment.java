package com.windsoft.oneday.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.nispok.snackbar.Snackbar;
import com.windsoft.oneday.R;
import com.windsoft.oneday.Secure;

import java.util.Calendar;

/**
 * Created by kim on 2015-08-04.
 */
public class SignUpFragment extends Fragment {
    private static final String TAG = "SignnUpFragment";

    private NumberPicker year;
    private NumberPicker month;
    private NumberPicker day;
    private EditText id;
    private EditText pw;
    private EditText config;
    private EditText mail;
    private Button submit;

    private String idStr;
    private String pwStr;
    private String mailStr;
    private String configStr;
    private long birth;

    private OnSignUpHandler sender;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sender = (OnSignUpHandler) activity;
    }

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
        id = (EditText) rootView.findViewById(R.id.fragment_sign_up_id);
        pw = (EditText) rootView.findViewById(R.id.fragment_sign_up_pw);
        config = (EditText) rootView.findViewById(R.id.fragment_sign_up_config);
        mail = (EditText) rootView.findViewById(R.id.fragment_sign_up_mail);
        submit = (Button) rootView.findViewById(R.id.fragment_sign_up_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idStr = id.getText().toString();
                pwStr = pw.getText().toString();
                mailStr = mail.getText().toString();
                configStr = config.getText().toString();

                if (idStr.length() > 0 && pwStr.length() > 0 && mailStr.length() > 0 && configStr.length() > 0) {
                    if (pwStr.equals(configStr)) {
                        Calendar calendar = Calendar.getInstance();

                        calendar.setTimeInMillis(0);
                        calendar.set(Calendar.YEAR, year.getValue());
                        calendar.set(Calendar.MONTH, month.getValue() - 1);
                        calendar.set(Calendar.DATE, day.getValue());

                        birth = calendar.getTimeInMillis();
                        sendInfo(idStr, pwStr, mailStr, birth);
                    } else {
                        Snackbar.with(getActivity())
                                .text("비밀번호를 확인하세요.")
                                .showAnimation(true)
                                .show(getActivity());
                    }
                } else if (idStr.length() == 0) {
                    Snackbar.with(getActivity())
                            .text("아이디를 입력하세요.")
                            .showAnimation(true)
                            .show(getActivity());
                    id.requestFocus();
                } else if (pwStr.length() == 0) {
                    Snackbar.with(getActivity())
                            .text("비밀번호를 입력하세요.")
                            .showAnimation(true)
                            .show(getActivity());
                    pw.requestFocus();
                } else if (mailStr.length() == 0) {
                    Snackbar.with(getActivity())
                            .text("메일을 입력하세요.")
                            .showAnimation(true)
                            .show(getActivity());
                    mail.requestFocus();
                } else if (configStr.length() == 0) {
                    Snackbar.with(getActivity())
                            .text("비밀번호확인을 입력하세요.")
                            .showAnimation(true)
                            .show(getActivity());
                    config.requestFocus();
                }


            }
        });


<<<<<<< HEAD
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int yearInt = calendar.get(Calendar.YEAR);


=======
>>>>>>> origin/master
        year.setMinValue(1950);
        year.setMaxValue(yearInt);
        year.setValue(1997);
        year.setWrapSelectorWheel(false);

        month.setMinValue(1);
        month.setMaxValue(12);
        month.setValue(2);
        month.setWrapSelectorWheel(false);

        day.setMinValue(1);
        day.setMaxValue(31);
        day.setValue(24);
        day.setWrapSelectorWheel(false);
    }


    public interface OnSignUpHandler {
        void onSignUp(String id, String pw, String mail, long birth);
    }


    private void sendInfo(String id, String pw, String mail, long birth) {
        //비밀번호 난이도 검사
        int cond = Secure.checkPasswordSecureLevel(pw);
        if (cond == Secure.SUCCESS) {
            sender.onSignUp(id, pw, mail, birth);              // 로그인 요청
        } else if (cond == Secure.NOT_ENOUGH_LETTER) {
            Snackbar.with(getActivity())
                    .text(R.string.sign_up_not_enough_letter)
                    .showAnimation(true)
                    .show(getActivity());
        } else if (cond == Secure.NO_SPECIAL_LETTER) {
            Snackbar.with(getActivity())
                    .text(R.string.sign_up_no_special_letter)
                    .showAnimation(true)
                    .show(getActivity());
        }
    }
}