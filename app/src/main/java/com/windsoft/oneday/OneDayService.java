package com.windsoft.oneday;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class OneDayService extends Service {

    private static final String TAG = "OneDayService";

    private static SocketIO socketIO;


    public static void createInstance(Context context) {
        Log.d(TAG, "서비스 생성");
        if (socketIO == null)
            socketIO = new SocketIO(context);
    }

    public OneDayService() {
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (intent != null) {
            String command = intent.getStringExtra(Global.KEY_COMMAND);
            if (command != null) {
                if (command.equals(Global.KEY_LOGIN)) {                 // 로그인 요청
                    String id = intent.getStringExtra(Global.KEY_LOGIN_ID);     // 아이디
                    String pw = intent.getStringExtra(Global.KEY_LOGIN_PW);     // 아이디
                    int cond = intent.getIntExtra(Global.KEY_LOGIN_TYPE, -1);   // 로그인 유형
                    if (cond != -1)
                        socketIO.login(id, pw, cond);                    // 로그인 처리

                } else if (command.equals(Global.KEY_SIGN_UP)) {                // 회원가입
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    String pw = intent.getStringExtra(Global.KEY_USER_PW);
                    String mail = intent.getStringExtra(Global.KEY_USER_MAIL);
                    long birth = intent.getLongExtra(Global.KEY_USER_BIRTH, 0);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(birth);

                    if (id != null && pw != null)
                        socketIO.signUp(id, pw, mail, calendar.getTime());
                } else if (command.equals(Global.KEY_GET_PROFILE)) {                // 프로필 요청
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    socketIO.getProfile(id);
                } else if (command.equals(Global.KEY_POST_NOTICE)) {                // 글쓰기
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    String name = intent.getStringExtra(Global.KEY_USER_NAME);
                    String content = intent.getStringExtra(Global.KEY_CONTENT);
                    String userImage = intent.getStringExtra(Global.KEY_USER_IMAGE);
                    ArrayList<String> imageList = new ArrayList<>();

                    String image;
                    int i = 0;
                    while ((image = intent.getStringExtra(Global.KEY_IMAGE + i)) != null) {
                        i++;
                        imageList.add(image);
                    }
                    Log.e(TAG,"받음");

                    socketIO.postNotice(id, content, imageList, name, userImage);
                } else if (command.equals(Global.KEY_SET_NAME)) {                   // 닉네임 설정
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    String name = intent.getStringExtra(Global.KEY_USER_NAME);
                    socketIO.setName(id, name);
                } else if (command.equals(Global.KEY_READ_NOTICE)) {                // 글 읽기 요청
                    int count = intent.getIntExtra(Global.KEY_COUNT, -1);
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    String keyword = intent.getStringExtra(Global.KEY_KEY_WORD);
                    Log.d(TAG, "keyword = " + keyword);
                    if (count != -1) {
                        if (keyword == null)
                            socketIO.readNotice(count, id);
                        else
                            socketIO.readNotice(count, id, keyword);
                    }
                } else if (command.equals(Global.KEY_GOOD)) {
                    boolean flag = intent.getBooleanExtra(Global.KEY_FLAG, false);
                    String userId = intent.getStringExtra(Global.KEY_USER_ID);
                    String noticeId = intent.getStringExtra(Global.KEY_NOTICE_ID);
                    int position = intent.getIntExtra(Global.KEY_POSITION, -1);
                    if (position != -1)
                        socketIO.goodCheck(flag, userId, noticeId, position);
                } else if (command.equals(Global.KEY_BAD)) {
                    boolean flag = intent.getBooleanExtra(Global.KEY_FLAG, false);
                    String userId = intent.getStringExtra(Global.KEY_USER_ID);
                    String noticeId = intent.getStringExtra(Global.KEY_NOTICE_ID);
                    int position = intent.getIntExtra(Global.KEY_POSITION, -1);
                    if (position != -1)
                        socketIO.badCheck(flag, userId, noticeId, position);
                } else if (command.equals(Global.KEY_COMMENT)) {
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    String noticeId = intent.getStringExtra(Global.KEY_NOTICE_ID);
                    String comment = intent.getStringExtra(Global.KEY_COMMENT);
                    String name = intent.getStringExtra(Global.KEY_USER_NAME);
                    int position = intent.getIntExtra(Global.KEY_POSITION, -1);
                    socketIO.comment(id, noticeId, comment, name, position);
                } else if (command.equals(Global.KEY_SET_PHOTO)) {
                    String image = intent.getStringExtra(Global.KEY_USER_IMAGE);
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    socketIO.setImage(image, id);
                } else if (command.equals(Global.KEY_SIGN_OUT)) {
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    socketIO.signOut(id);
                } else if (command.equals(Global.KEY_FIND_ID)) {
                    String name = intent.getStringExtra(Global.KEY_USER_NAME);
                    String mail = intent.getStringExtra(Global.KEY_USER_MAIL);
                    socketIO.findId(name, mail);
                } else if (command.equals(Global.KEY_FIND_PW)) {
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    String name = intent.getStringExtra(Global.KEY_USER_NAME);
                    String mail = intent.getStringExtra(Global.KEY_USER_MAIL);
                    socketIO.findPw(id, name, mail);
                } else if (command.equals(Global.KEY_SET_PW)) {
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    String pw = intent.getStringExtra(Global.KEY_USER_PW);
                    socketIO.setPw(id, pw);
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
