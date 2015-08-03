package com.windsoft.oneday.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.windsoft.oneday.R;
import com.windsoft.oneday.fragment.MainFragment;
import com.windsoft.oneday.fragment.ProfileFragment;
import com.windsoft.oneday.fragment.SettingFragment;
import com.windsoft.oneday.fragment.WriteFragment;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private ViewPager viewPager;

    private MainFragment mainFragment;
    private WriteFragment writeFragment;
    private ProfileFragment profileFragment;
    private SettingFragment settingFragment;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    private void init() {
        mainFragment = new MainFragment();
        writeFragment = new WriteFragment();
        profileFragment = new ProfileFragment();
        settingFragment = new SettingFragment();

        setViewPager();
        setToolbar();
    }


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }


    private void setViewPager() {
        final MaterialTabHost tab = (MaterialTabHost) findViewById(R.id.activity_main_materialTabHost);
        for (int i = 0; i < 4; i++) {
            Drawable image = getResources().getDrawable(R.drawable.splash);
            tab.addTab(tab.newTab()
                    .setIcon(image)
                    .setTabListener(new MaterialTabListener() {
                        @Override
                        public void onTabSelected(MaterialTab materialTab) {
                            viewPager.setCurrentItem(materialTab.getPosition());
                        }

                        @Override
                        public void onTabReselected(MaterialTab materialTab) {

                        }

                        @Override
                        public void onTabUnselected(MaterialTab materialTab) {

                        }
                    }));
        }

        viewPager = (ViewPager) findViewById(R.id.activity_main_pager);
        OneDayAdapter adapter = new OneDayAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                tab.setSelectedNavigationItem(position);
            }
        });
    }



    private class OneDayAdapter extends FragmentPagerAdapter {

        public OneDayAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mainFragment;

                case 1:
                    return writeFragment;

                case 2:
                    return profileFragment;

                case 3:
                    return settingFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

}
