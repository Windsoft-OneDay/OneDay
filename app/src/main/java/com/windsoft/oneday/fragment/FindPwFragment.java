package com.windsoft.oneday.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nispok.snackbar.Snackbar;
import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;
import com.windsoft.oneday.Secure;


public class FindPwFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "FindPwFragment";

    private static final int INFO = 0;
    private static final int INPUT = 1;
    private int cond = INFO;

    private EditText idInput;
    private EditText mailInput;
    private EditText nameInput;
    private EditText pwInput;
    private EditText confirmInput;
    private LinearLayout infoContainer;
    private LinearLayout inputContainer;
    private Button submit;

    private String id;

    public FindPwFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_pw, container, false);

        init(rootView);

        return rootView;
    }


    private void init(View rootView) {
        idInput = (EditText) rootView.findViewById(R.id.fragment_find_pw_id);
        nameInput = (EditText) rootView.findViewById(R.id.fragment_find_pw_name);
        mailInput = (EditText) rootView.findViewById(R.id.fragment_find_pw_mail);
        pwInput = (EditText) rootView.findViewById(R.id.fragment_find_pw_input);
        confirmInput = (EditText) rootView.findViewById(R.id.fragment_find_pw_confirm_input);

        infoContainer = (LinearLayout) rootView.findViewById(R.id.fragment_find_pw_info_container);
        inputContainer = (LinearLayout) rootView.findViewById(R.id.fragment_find_pw_input_container);

        submit = (Button) rootView.findViewById(R.id.fragment_find_pw_submit);
        setListener();
    }


    private void setListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cond == INFO) {
                    id = idInput.getText().toString();
                    String name = nameInput.getText().toString();
                    String mail = mailInput.getText().toString();

                    if (id.length() > 0 && name.length() > 0 && mail.length() > 0) {
                        Intent intent = new Intent(getActivity(), OneDayService.class);
                        intent.putExtra(Global.KEY_COMMAND, Global.KEY_FIND_PW);
                        intent.putExtra(Global.KEY_USER_ID, id);
                        intent.putExtra(Global.KEY_USER_NAME, name);
                        intent.putExtra(Global.KEY_USER_MAIL, mail);
                        getActivity().startService(intent);
                    }
                } else if (cond == INPUT) {
                    String pw = pwInput.getText().toString();
                    String confirm = confirmInput.getText().toString();

                    if (pw.length() > 0 && pw.equals(confirm)) {
                        int cond = Secure.checkPasswordSecureLevel(pw);
                        if (cond == Secure.SUCCESS) {
                            Intent intent = new Intent(getActivity(), OneDayService.class);
                            intent.putExtra(Global.KEY_COMMAND, Global.KEY_SET_PW);
                            intent.putExtra(Global.KEY_USER_ID, id);
                            intent.putExtra(Global.KEY_USER_PW, Secure.Sha256Encrypt(pw));
                            getActivity().startService(intent);
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
            }
        });
    }


    public void setPw() {
        infoContainer.setVisibility(View.INVISIBLE);
        inputContainer.setVisibility(View.VISIBLE);

        cond = INPUT;
        submit.setText(R.string.submit);
    }
}
