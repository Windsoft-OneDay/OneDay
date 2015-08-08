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
    private OnSetNameHandler sender;

    public SetNameDialog(Context context) {
        super(context);
        sender = (OnSetNameHandler) context;
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
        title = (TextView) findViewById(R.id.custom_dialog_nickname_title);
        name = (EditText) findViewById(R.id.custom_dialog_nickname_nickname);
        submit = (Button) findViewById(R.id.custom_dialog_nickname_submit);

        setListener();
        setTitle("닉네임을 입력하세요");
        setCancelable(false);
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


    private void setTitle(String title) {
        this.title.setText(title);
    }
}
