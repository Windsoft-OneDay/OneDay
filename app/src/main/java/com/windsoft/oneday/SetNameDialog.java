package com.windsoft.oneday;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ironFactory on 2015-08-08.
 */
public class SetNameDialog extends Dialog {

    private TextView title;
    private EditText name;
    private Button submit;
    private boolean ableDismiss;
    private OnSetNameHandler sender;

    public SetNameDialog(Context context, boolean ableDismiss) {
        super(context);
        sender = (OnSetNameHandler) context;
        this.ableDismiss = ableDismiss;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.8f;
        getWindow().setAttributes(params);

        setContentView(R.layout.custom_dialog_nickname);

        init();
    }


    private void init() {
        name = (EditText) findViewById(R.id.custom_dialog_nickname_nickname);
        submit = (Button) findViewById(R.id.custom_dialog_nickname_submit);

        setListener();
        setTitle("닉네임을 입력하세요");
        setCancelable(ableDismiss);
    }


    private void setListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString();
                if (nameStr.length() != 0)
                    sender.onSetName(nameStr);
            }
        });
    }


    public interface OnSetNameHandler {
        void onSetName(String name);
    }
}
