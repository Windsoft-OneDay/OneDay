package com.windsoft.oneday;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.windsoft.oneday.activity.LoginActivity;

import org.json.JSONObject;

/**
 * Created by ironFactory on 2015-08-03.
 */
public class SocketIO {

    private static final String URL = "http://windsoft-oneday.herokuapp.com";
    private static final String TAG = "SocketIO";

    private static final String COND = "cond";

    private int cond;

    private static Socket socket;
    private Context context;

    public SocketIO(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        try {
            socket = IO.socket(URL);
        } catch (Exception e) {
            Log.e(TAG, "init 에러 = " + e.getMessage());
        }

        if (socket != null) {
            socketConnect();
        }
    }


    public static Socket getSocket() {
        return socket;
    }


    private void socketConnect() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
            socket.close();
        }

        cond = Global.NULL;

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {            // 서버와 연결 됬을 때
            @Override
            public void call(Object... args) {
                Log.d(TAG, "연결 응답");
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra(Global.KEY_COMMAND, Global.VALUE_CONNECT);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }).on(Global.KEY_LOGIN, new Emitter.Listener() {                    // 로그인 응답 왔을 때
            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject) args[0];
                    int cond = obj.getInt(COND);
                    String id = null;
                    String pw = null;
                    if (cond == Global.SUCCESS) {
                        id = obj.getString(Global.KEY_USER_ID);
                        pw = obj.getString(Global.KEY_USER_PW);
                    }
                    processLoginRes(cond, id, pw);
                } catch (Exception e) {
                    Log.e(TAG, "로그인 응답 오류 = " + e.getMessage());
                }
            }
        });

        socket.open();
        socket.connect();
    }


    /**
     * TODO: 로그인 응답 처리
     * @param cond : 응답요건
     *             NULL(0) = 아이디/비밀번호 틀림
     *             SUCCESS(1) = 성공
     * @param id : 자동로그인용 id
     * @param pw : 자동로그인용 pw
     * */
    private void processLoginRes(int cond, String id, String pw) {
        Log.d(TAG, "로그인 응답");
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_LOGIN);
        intent.putExtra(Global.KEY_COND, cond);
        intent.putExtra(Global.KEY_USER_ID, id);
        intent.putExtra(Global.KEY_USER_PW, pw);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * TODO: 로그인 요청 시 실행
     * @param id : 아이디
     * @param pw : 비밀번호
     * @param cond : 로그인 타입
     * */
    public void processLoginReq(String id, String pw, int cond) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_USER_ID, id);
            if (cond == Global.ONE_DAY) {
                pw = Secure.Sha256Encrypt(pw);
                Log.d(TAG, "pw = " + pw);
                obj.put(Global.KEY_USER_PW, pw);
            }
            socket.emit(Global.KEY_LOGIN, obj);
        } catch (Exception e) {
        }
    }
}
