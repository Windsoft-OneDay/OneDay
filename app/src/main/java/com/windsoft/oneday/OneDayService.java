package com.windsoft.oneday;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

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
                    if (cond != -1) {
                        socketIO.login(id, pw, cond);                    // 로그인 처리
                    }
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
