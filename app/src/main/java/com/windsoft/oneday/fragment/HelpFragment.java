package com.windsoft.oneday.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.windsoft.oneday.R;


/**
 * Created by ironFactory on 2015-09-15.
 */
public class HelpFragment extends Fragment {

    private static final String TAG = "HelpFragment";

    private FrameLayout layout;
    private int cond = 0;
    private onHelpHandler sender;

    public HelpFragment() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sender = (onHelpHandler) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        layout = (FrameLayout) rootView.findViewById(R.id.fragment_help_container);
        layout.setBackgroundResource(R.drawable.sorry);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cond == 0) {
                    layout.setBackgroundResource(R.drawable.sorry);
                    cond++;
                } else {
                    sender.onHelpFinish();
                }
            }
        });

        return rootView;
    }


    public interface onHelpHandler {
        void onHelpFinish();
    }
}
