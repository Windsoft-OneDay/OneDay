package com.windsoft.oneday.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.windsoft.oneday.R;

/**
 * Created by ironFactory on 2015-08-04.
 */
public class WriteFragment extends Fragment {

    public WriteFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_write, container, false);

        init(rootView);

        return rootView;
    }


    private void init(View rootView) {

    }
}
