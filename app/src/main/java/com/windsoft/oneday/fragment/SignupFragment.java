package com.windsoft.oneday.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.windsoft.oneday.R;

/**
 * Created by kim on 2015-08-03.
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
}
