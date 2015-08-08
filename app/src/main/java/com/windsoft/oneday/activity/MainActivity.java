package com.windsoft.oneday.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;
import com.windsoft.oneday.SetNameDialog;
import com.windsoft.oneday.fragment.MainFragment;
import com.windsoft.oneday.fragment.ProfileFragment;
import com.windsoft.oneday.fragment.SettingFragment;
import com.windsoft.oneday.fragment.WriteFragment;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements SetNameDialog.OnSetNameHandler {

    private final String TAG = "MainActivity";

    private ViewPager viewPager;

    private MainFragment mainFragment;
    private WriteFragment writeFragment;
    private ProfileFragment profileFragment;
    private SettingFragment settingFragment;

    private Toolbar toolbar;
    private Button submit;
    private TextView title;
    private SearchView searchView;
    private SetNameDialog dialog;

    private String id;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getArgument();
        init();
    }


    private void getArgument() {
        Intent intent = getIntent();
        id = intent.getStringExtra(Global.KEY_USER_ID);
        name = intent.getStringExtra(Global.KEY_USER_NAME);

        if (name == null || name.length() == 0)
            setName();
        Log.d(TAG, "name = " + name);
    }


    private void setName() {
        dialog = new SetNameDialog(this);
        dialog.show();
    }


    private void init() {
        mainFragment = MainFragment.newInstance(id);
        writeFragment = WriteFragment.newInstance(id, name);
        profileFragment = ProfileFragment.newInstance(id);
        settingFragment = SettingFragment.newInstance(id);

        submit = (Button) findViewById(R.id.activity_main_submit);
        title = (TextView) findViewById(R.id.activity_main_title);
        searchView = (SearchView) findViewById(R.id.activity_main_search);
        setViewPager();
        setToolbar();
        setListener();
    }


    private void setListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFragment.post();
            }
        });
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
                            int position = materialTab.getPosition();
                            viewPager.setCurrentItem(position);
                            if (position == 0) {                        // 뉴스피드 탭
                                searchView.setVisibility(View.VISIBLE);
                                submit.setVisibility(View.GONE);
                                title.setVisibility(View.GONE);
                            } else if (position == 1) {                 // 글쓰기 탭
                                searchView.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
                                title.setVisibility(View.VISIBLE);
                                title.setText("글 쓰기");
                            } else if (position == 2) {                 // 프로필 탭
                                searchView.setVisibility(View.GONE);
                                submit.setVisibility(View.GONE);
                                title.setVisibility(View.VISIBLE);
                                title.setText("프로필");
                            } else if (position == 3) {                 // 설정 탭
                                searchView.setVisibility(View.GONE);
                                submit.setVisibility(View.GONE);
                                title.setVisibility(View.VISIBLE);
                                title.setText("설정");
                            }
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
                if (position == 0) {                        // 뉴스피드 탭
                    searchView.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    title.setVisibility(View.GONE);
                } else if (position == 1) {                 // 글쓰기 탭
                    searchView.setVisibility(View.GONE);
                    submit.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("글 쓰기");
                } else if (position == 2) {                 // 프로필 탭
                    searchView.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("프로필");
                } else if (position == 3) {                 // 설정 탭
                    searchView.setVisibility(View.GONE);
                    submit.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);
                    title.setText("설정");
                }
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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String command = intent.getStringExtra(Global.KEY_COMMAND);
            if (command != null) {
                if (command.equals(Global.KEY_SIGN_UP)) {                       // 회원가입 응답
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    if (code != -1)
                        processSignUp(code);
                } else if (command.equals(Global.KEY_SET_NAME)) {
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    String name = intent.getStringExtra(Global.KEY_USER_NAME);
                    if (code != -1)
                        processSetName(code, name);
                }
            }
        }
    }


    /**
     * TODO: 닉네임 설정 응답
     * @param code : 응답 코드
     * @param name : 닉네임
     * */
    private void processSetName(int code, String name) {
        if (code == Global.CODE_NAME_ALREADY) {                 // 닉네임이 이미 사용 중이라면
            Snackbar.with(getApplicationContext())
                    .text(R.string.set_name_already)
                    .show(this);
        } else if (code == Global.CODE_SUCCESS) {               // 성공 했다면
            dialog.dismiss();
        }
    }


    /**
     * TODO: 회원가입 응답
     * @param cond : 응답 코드
     * */
    private void processSignUp(int cond) {
        if (cond == Global.CODE_ID_ALREADY) {                           // 아이디 이미 사용 시
            Snackbar.with(getApplicationContext())
                    .text(R.string.sign_up_id_already)
                    .show(this);
        } else if (cond == Global.CODE_SIGN_UP_FAIL) {                  // 회원가입 실패 시
            Snackbar.with(getApplicationContext())
                    .text(R.string.sign_up_fail)
                    .show(this);
        }
    }


    @Override
    public void onSetName(String name) {
        Intent intent = new Intent(MainActivity.this, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_SET_NAME);
        intent.putExtra(Global.KEY_USER_NAME, name);
        intent.putExtra(Global.KEY_USER_ID, id);
        startService(intent);
    }
}
