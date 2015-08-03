package com.windsoft.oneday.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.widget.NumberPicker;

import com.windsoft.oneday.R;

/**
 * Created by kim on 2015-08-03.
 */
public class SignupFragment extends Activity {
    NumberPicker year;
    NumberPicker month;
    NumberPicker day;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_up);

        year = (NumberPicker) findViewById(R.id.fragment_sign_up_year);
        month = (NumberPicker) findViewById(R.id.fragment_sign_up_month);
        day = (NumberPicker) findViewById(R.id.fragment_sign_up_day);

        year.setMinValue(1950);
        year.setMaxValue(2015);
        year.setWrapSelectorWheel(false);

        month.setMinValue(1);
        month.setMaxValue(12);
        month.setWrapSelectorWheel(false);

    }
}
