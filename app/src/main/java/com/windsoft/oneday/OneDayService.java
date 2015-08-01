package com.windsoft.oneday;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class OneDayService extends Service {

    private static final String TAG = "OneDayService";

    public OneDayService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String command = intent.getStringExtra(Global.KEY_COMMAND);
            if (command != null) {
                if (command.equals(Global.KEY_LOGIN)) {                 // 로그인 요청
                    String id = intent.getStringExtra(Global.KEY_LOGIN_ID);     // 아이디
                    int cond = intent.getIntExtra(Global.KEY_LOGIN_TYPE, -1);   // 로그인 유형
                    if (cond != -1) {
                        commandLogin(id, cond);                 // 로그인 처리
                    }
                }
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * TODO: 로그인 요청 처리
     * @param id : 아이디
     * @param cond : 로그인 유형
     *             0 = 페이스북
     *             1 = 네이버
     *             2 = 구글
     *             3 = OneDay
     * */
    private void commandLogin(String id, int cond) {
        Log.d(TAG, "로그인 요청");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
