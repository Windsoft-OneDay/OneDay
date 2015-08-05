package com.windsoft.oneday;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
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
                } else if (command.equals(Global.KEY_GET_PROFILE)) {
                    String id = intent.getStringExtra(Global.KEY_USER_ID);
                    socketIO.getProfile(id);
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
