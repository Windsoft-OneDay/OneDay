package com.windsoft.oneday.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;


public class FindIdFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "FindIdFragment";

    private EditText nameInput;
    private EditText mailInput;
    private Button submit;

    private boolean isFind = false;
    private OnFindIdHandler sender;

    public FindIdFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sender = (OnFindIdHandler) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_find_id, container, false);

        init(rootView);

        return rootView;
    }


    private void init(View rootView) {
        nameInput = (EditText) rootView.findViewById(R.id.fragment_find_id_name);
        mailInput = (EditText) rootView.findViewById(R.id.fragment_find_id_mail);
        submit = (Button) rootView.findViewById(R.id.fragment_find_id_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFind) {
                    String name = nameInput.getText().toString();
                    String mail = mailInput.getText().toString();

                    if (name.length() > 0 && mail.length() > 0) {
                        Intent intent = new Intent(getActivity(), OneDayService.class);
                        intent.putExtra(Global.KEY_COMMAND, Global.KEY_FIND_ID);
                        intent.putExtra(Global.KEY_USER_NAME, name);
                        intent.putExtra(Global.KEY_USER_MAIL, mail);
                        getActivity().startService(intent);
                    }
                } else {
                    sender.onIntentLogin();
                }
            }
        });
    }


    public void setId(String id) {
        submit.setText(id);
        isFind = true;
    }


    public interface OnFindIdHandler {
        void onIntentLogin();
    }
}
