package com.windsoft.oneday.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.windsoft.oneday.R;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mViewPager = (ViewPager) findViewById(R.id.activity_main_pager);
    }


    private class OneDayAdapter extends FragmentPagerAdapter {

        public OneDayAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    break;

                case 1:

                    break;

                case 2:

                    break;

                case 3:

                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
