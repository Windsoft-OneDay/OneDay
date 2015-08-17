package com.windsoft.oneday;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.windsoft.oneday.model.NoticeModel;

import java.util.ArrayList;

/**
 * Created by ironFactory on 2015-08-16.
 */
public class UpdateNoticeDialog extends Dialog {

    private OnUpdateNoticeHandler sender;
    private NoticeModel notice;

    public UpdateNoticeDialog(Context context, NoticeModel notice) {
        super(context);
        sender = (OnUpdateNoticeHandler) context;
        this.notice = notice;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.8f;
        getWindow().setAttributes(params);
        setContentView(R.layout.custom_dialog_update_notice);

        init();
    }


    private void init() {
        RelativeLayout updateContainer = (RelativeLayout) findViewById(R.id.custom_dialog_update_notice_update);
        RelativeLayout removeContainer = (RelativeLayout) findViewById(R.id.custom_dialog_update_notice_remove);

        updateContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sender.update(notice.getNoticeId(), notice.getContent(), notice.getImageList());
                dismiss();
            }
        });


        removeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sender.remove(notice.getNoticeId());
                dismiss();
            }
        });
    }


    public interface OnUpdateNoticeHandler {
        void update(String noticeId, String content, ArrayList<String> image);
        void remove(String noticeId);
    }
}
