package com.windsoft.oneday.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.windsoft.oneday.ImageBase64;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;
import com.windsoft.oneday.SetNameDialog;
import com.windsoft.oneday.UpdateNoticeDialog;
import com.windsoft.oneday.fragment.CommentFragment;
import com.windsoft.oneday.fragment.MainFragment;
import com.windsoft.oneday.fragment.ProfileFragment;
import com.windsoft.oneday.fragment.SettingFragment;
import com.windsoft.oneday.fragment.WriteFragment;
import com.windsoft.oneday.model.NoticeModel;

import java.util.ArrayList;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements SetNameDialog.OnSetNameHandler, SettingFragment.OnSettingHandler, UpdateNoticeDialog.OnUpdateNoticeHandler, WriteFragment.OnWriteHandler {

    private final String TAG = "MainActivity";
    private static final int TAKE_GALLERY = 10;

    private ViewPager viewPager;

    private MainFragment mainFragment;
    private WriteFragment writeFragment;
    private ProfileFragment profileFragment;
    private SettingFragment settingFragment;
    private CommentFragment commentFragment;

    private Toolbar toolbar;
    private Button submit;
    private Button cancel;
    private TextView title;
    private SearchView searchView;
    private SetNameDialog dialog;
    private MaterialTabHost tab;

    private String id;
    private String name;
    private String image;


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
        image = intent.getStringExtra(Global.KEY_USER_IMAGE);

        if (name == null || name.length() == 0 || name.equals("null"))
            setName();
    }


    private void setName() {
        dialog = new SetNameDialog(this, false);
        dialog.show();
    }


    private void init() {
        mainFragment = MainFragment.newInstance(id, name, image);
        writeFragment = WriteFragment.newInstance(id, name, image);
        profileFragment = ProfileFragment.newInstance(id,name,image);
        settingFragment = SettingFragment.newInstance(id);

        submit = (Button) findViewById(R.id.activity_main_submit);
        cancel = (Button) findViewById(R.id.activity_main_cancel);
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


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFragment.removeAll();
                viewPager.setCurrentItem(0);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainFragment.setKeyword(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0)
                    mainFragment.setKeyword(null);
                return false;
            }
        });
    }


    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }


    private void setViewPager() {
        tab = (MaterialTabHost) findViewById(R.id.activity_main_materialTabHost);
        Drawable[] imageList = {
                getResources().getDrawable(R.drawable.newspeed_icon),
                getResources().getDrawable(R.drawable.write_icon),
                getResources().getDrawable(R.drawable.user_icon),
                getResources().getDrawable(R.drawable.setting_icon)
        };
        for (int i = 0; i < 4; i++) {
            Drawable image = imageList[i];
            MaterialTab curTab = tab.newTab()
                    .setIcon(image)
                    .setTabListener(new MaterialTabListener() {
                        @Override
                        public void onTabSelected(MaterialTab materialTab) {
                            int position = materialTab.getPosition();
                            viewPager.setCurrentItem(position);
                            setToolbarComponent(position);
                            materialTab.setIconColor(getResources().getColor(R.color.light_main));
                        }

                        @Override
                        public void onTabReselected(MaterialTab materialTab) {
                            materialTab.setIconColor(getResources().getColor(R.color.light_main));
                        }

                        @Override
                        public void onTabUnselected(MaterialTab materialTab) {
                            materialTab.setIconColor(getResources().getColor(R.color.gray));
                        }
                    });
            tab.addTab(curTab);
            tab.setIconColor(getResources().getColor(R.color.light_main));

        }

        viewPager = (ViewPager) findViewById(R.id.activity_main_pager);
        viewPager.setOffscreenPageLimit(4);
        OneDayAdapter adapter = new OneDayAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                tab.setSelectedNavigationItem(position);
                setToolbarComponent(position);
            }
        });
    }


    private void setToolbarComponent(int position) {
        tab.setIconColor(getResources().getColor(R.color.gray));
        tab.getCurrentTab().setIconColor(getResources().getColor(R.color.main));
        if (position == 0) {                        // 뉴스피드 탭
            searchView.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        } else if (position == 1) {                 // 글쓰기 탭
            searchView.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            title.setText("글 쓰기");
        } else if (position == 2) {                 // 프로필 탭
            searchView.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText("프로필");
        } else if (position == 3) {                 // 설정 탭
            searchView.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText("설정");
        }
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
                if (command.equals(Global.KEY_SET_NAME)) {
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    String name = intent.getStringExtra(Global.KEY_USER_NAME);
                    if (code != -1)
                        processSetName(code, name);
                } else if (command.equals(Global.KEY_POST_NOTICE)) {
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    if (code != -1)
                        processPost(code);
                } else if (command.equals(Global.KEY_READ_NOTICE)) {
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    int count = intent.getIntExtra(Global.KEY_COUNT, -1);
                    ArrayList<NoticeModel> noticeList = (ArrayList<NoticeModel>) intent.getSerializableExtra(Global.KEY_NOTICE);

                    if (code != -1 || count != -1)
                        processReadNotice(code, noticeList, count);
                } else if (command.equals(Global.KEY_GOOD)) {
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    boolean flag = intent.getBooleanExtra(Global.KEY_FLAG, false);
                    int position = intent.getIntExtra(Global.KEY_POSITION, -1);
                    if (code != -1 && position != -1)
                        processGood(code, flag, position);
                } else if (command.equals(Global.KEY_BAD)) {
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    boolean flag = intent.getBooleanExtra(Global.KEY_FLAG, false);
                    int position = intent.getIntExtra(Global.KEY_POSITION, -1);
                    if (code != -1 && position != -1)
                        processBad(code, flag, position);
                } else if (command.equals(Global.KEY_COMMENT)) {                                // 댓글 달기 응답
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    String comment = intent.getStringExtra(Global.KEY_COMMENT);
                    int position = intent.getIntExtra(Global.KEY_POSITION, -1);
                    String noticeId = intent.getStringExtra(Global.KEY_NOTICE_ID);
                    if (code != -1)
                        processComment(code, comment, position, noticeId);
                } else if (command.equals(Global.KEY_SHOW_COMMENT)) {                           // 댓글 창 보이기
                    NoticeModel notice = (NoticeModel) intent.getSerializableExtra(Global.KEY_NOTICE);
                    processShowComment(notice);
                } else if (command.equals(Global.KEY_GET_PROFILE)) {
                    ArrayList<NoticeModel> noticeList = (ArrayList<NoticeModel>) intent.getSerializableExtra(Global.KEY_NOTICE);
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    if (code != -1)
                        processGetProfile(code, noticeList);
                } else if (command.equals(Global.KEY_GET_PHOTO)) {

                } else if (command.equals(Global.KEY_SET_PHOTO)) {
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    if (code != -1)
                        processSetPhoto(code);
                } else if (command.equals(Global.KEY_SIGN_OUT)) {
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    if (code != -1)
                        processSingOut(code);
                } else if (command.equals(Global.KEY_UPDATE_NOTICE)) {
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    if (code != -1)
                        processUpdateNotice(code);
                } else if (command.equals(Global.KEY_REMOVE_NOTICE)) {
                    int code = intent.getIntExtra(Global.KEY_CODE, -1);
                    if (code != -1)
                        processRemoveNotice(code);
                }
            }
        }
    }


    private void processUpdateNotice(int code) {
        if (code != Global.CODE_SUCCESS) {
            Snackbar.with(this)
                    .text(R.string.fail_again)
                    .show(this);
        } else {
            Snackbar.with(this)
                    .text(R.string.success)
                    .show(this);
        }
    }


    private void processRemoveNotice(int code) {
        if (code != Global.CODE_SUCCESS) {
            Snackbar.with(this)
                    .text(R.string.fail_again)
                    .show(this);
        } else {
            Snackbar.with(this)
                    .text(R.string.success)
                    .show(this);
        }
    }


    private void processSingOut(int code) {
        if (code != Global.CODE_SUCCESS) {
            Snackbar.with(this)
                    .text(R.string.fail_again)
                    .show(this);
        } else {
            finish();
        }
    }


    private void processSetPhoto(int code) {
        if (code != Global.CODE_SUCCESS) {
            Snackbar.with(this)
                    .text(R.string.fail_again)
                    .show(this);
        } else {
            Snackbar.with(this)
                    .text(R.string.success)
                    .show(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "requestCode = " + requestCode);
        if (requestCode == WriteFragment.TAKE_GALLERY || requestCode == WriteFragment.TAKE_CAMERA)
            writeFragment.onActivityResult(requestCode, resultCode, data);

        else if (requestCode == TAKE_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (Exception e) {
                    Log.e(TAG, "갤러리 에러");
                }

                String image = ImageBase64.encodeTobase64(bitmap);
                Intent intent = new Intent(this, OneDayService.class);
                intent.putExtra(Global.KEY_COMMAND, Global.KEY_SET_PHOTO);
                intent.putExtra(Global.KEY_USER_IMAGE, image);
                intent.putExtra(Global.KEY_USER_ID, id);
                startService(intent);
                profileFragment.setImage(image);
            }
        }
    }


    private void processGetProfile(int code, ArrayList<NoticeModel> noticeList) {
        Log.d(TAG, "getProfile = code = " + code);
        if (code != Global.CODE_SUCCESS) {
            Snackbar.with(getApplicationContext())
                    .text(R.string.fail_again)
                    .show(this);
        } else {
            profileFragment.setData(noticeList);
        }
    }


    private void processShowComment(NoticeModel notice) {
        commentFragment = CommentFragment.createInstance(notice, id, name, image);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_container, commentFragment)
                .addToBackStack(null)
                .commit();
    }


    private void processComment(int code, String comment, int position, String noticeId) {
        if (code != Global.CODE_SUCCESS) {
            Snackbar.with(this)
                    .text(R.string.fail_again)
                    .show(this);

            commentFragment.failComment(comment, position);
        } else {
            mainFragment.addComment(comment, noticeId);
        }

        Log.d(TAG, "comment code = " + code);
    }


    private void processGood(int code, boolean flag, int position) {
        if (code != Global.CODE_SUCCESS) {                      // 실패 시
            Snackbar.with(this)
                    .text(R.string.fail_again)
                    .show(this);

            mainFragment.failGood(flag, position);
        }
    }


    private void processBad(int code, boolean flag, int position) {
        if (code != Global.CODE_SUCCESS) {                      // 실패 시
            Snackbar.with(this)
                    .text(R.string.fail_again)
                    .show(this);

            mainFragment.failBad(flag, position);
        }
    }


    private void processReadNotice(int code, ArrayList<NoticeModel> noticeList, int count) {
        if (code == Global.CODE_READ_NOTCIE_FAIL) {
            Snackbar.with(this)
                    .text(R.string.read_err)
                    .show(this);
        } else if (code == Global.CODE_NOT_ENOUGH_NOTICE) {
            Snackbar.with(this)
                    .text(R.string.read_not_enough)
                    .show(this);

            mainFragment.setCount(count - 1);
        } else if (code == Global.CODE_SUCCESS) {
            if (count == 0)
                mainFragment.setData(noticeList);
            else if (count > 0)
                mainFragment.addData(noticeList);
        }
    }


    /**
     * TODO: 글쓰기 응답
     * @param code : 응답 코드
     * */
    private void processPost(int code) {
        if (code == Global.CODE_POST_ERR || code == Global.CODE_USER_ADD_NOTICE) {                         // 글쓰기 실패 || 사용자 DB에 글 추가 에러
            Snackbar.with(getApplicationContext())
                    .text(R.string.post_err)
                    .show(this);
        } else if (code == Global.CODE_SUCCESS) {                                           // 성공
            writeFragment.removeAll();
            Snackbar.with(getApplicationContext())
                    .text(R.string.post)
                    .show(this);
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
        } else if (code == Global.CODE_SET_NAME_FAIL) {
            Snackbar.with(getApplicationContext())
                    .text(R.string.set_name_fail)
                    .show(this);
        } else if (code == Global.CODE_SUCCESS) {               // 성공 했다면
            dialog.dismiss();
            mainFragment.readNotice(0);
            this.name = name;

            if (commentFragment != null)
                commentFragment.setName(name);

            if (mainFragment != null)
                mainFragment.setName(name);

            if (writeFragment != null)
                writeFragment.setName(name);
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


    @Override
    public void sortByGood() {
        mainFragment.sortByGood();
    }

    @Override
    public void sortByTime() {
        mainFragment.sortByTime();
    }


    @Override
    public void update(String noticeId, String content, ArrayList<String> image) {
        writeFragment.update(noticeId, content, image);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void remove(String noticeId) {
        Intent intent = new Intent(this, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_REMOVE_NOTICE);
        intent.putExtra(Global.KEY_NOTICE_ID, noticeId);
        startService(intent);
    }


    @Override
    public void addPhotoFromGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, WriteFragment.TAKE_GALLERY);
    }

    @Override
    public void addPhotoFromCamera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, WriteFragment.TAKE_CAMERA);
    }

    @Override
    public void post() {
        mainFragment.readNotice(0);
        profileFragment.getProfile();
    }
}
