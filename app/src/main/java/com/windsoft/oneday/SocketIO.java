package com.windsoft.oneday;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.windsoft.oneday.activity.LoginActivity;
import com.windsoft.oneday.activity.MainActivity;
import com.windsoft.oneday.model.NoticeModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ironFactory on 2015-08-03.
 */
public class SocketIO {

    private static final String URL = "http://windsoft-oneday.herokuapp.com";
    private static final String TAG = "SocketIO";

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

        cond = 0;

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
                    int code = obj.getInt(Global.KEY_CODE);
                    String id = null;
                    String name = null;
                    if (code == Global.CODE_SUCCESS) {
                        id = obj.getString(Global.KEY_USER_ID);
                        try {
                            name = obj.getString(Global.KEY_USER_NAME);
                        } catch (Exception e) {
                            Log.e(TAG, "이름 없음");
                        }
                    }
                    processLoginRes(code, id, name);
                } catch (Exception e) {
                    Log.e(TAG, "로그인 응답 오류 = " + e.getMessage());
                }
            }
        }).on(Global.KEY_SIGN_UP, new Emitter.Listener() {                      // 로그인 응답
            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject) args[0];
                    int cond = obj.getInt(Global.KEY_CODE);
                    processSignUp(cond);
                } catch (Exception e) {
                    Log.e(TAG, "회원가입 에러 = " + e.getMessage());
                }
            }
        }).on(Global.KEY_READ_NOTICE, new Emitter.Listener() {                  // 게시글 읽어오기 응답
            @Override
            public void call(Object... args) {

            }
        }).on(Global.KEY_GET_PROFILE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject) args[0];
                    Log.d(TAG, "notice list = " + obj.get(Global.KEY_NOTICE));
                    ArrayList<NoticeModel> noticeList = (ArrayList<NoticeModel>) obj.get(Global.KEY_NOTICE);
                } catch (Exception e) {
                    Log.e(TAG, "프로필 받아오기 오류 = " + e.getMessage());
                }
            }
        }).on(Global.KEY_POST_NOTICE, new Emitter.Listener() {                  // 글쓰기 응답
            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject) args[0];
                    int code = obj.getInt(Global.KEY_CODE);
                    Log.d(TAG," 글쓰기 응답 ");
                } catch (Exception e) {
                    Log.e(TAG, "글쓰기 응답 오류 = " + e.getMessage());
                }
            }
        }).on(Global.KEY_SET_NAME, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject) args[0];
                    int code = obj.getInt(Global.KEY_CODE);
                    String name = obj.getString(Global.KEY_USER_NAME);
                    processSetName(code, name);
                    Log.d(TAG," 닉네임 설정 응답 ");
                } catch (Exception e) {
                    Log.e(TAG, "닉네임 설정 응답 오류 = " + e.getMessage());
                }
            }
        });

        socket.open();
        socket.connect();
    }


    /**
     * TODO: 닉네임 설정 응답
     * @param code : 응답코드
     * @param name : 닉네임
     * */
    private void processSetName(int code, String name) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_SET_NAME);
        intent.putExtra(Global.KEY_CODE, code);
        intent.putExtra(Global.KEY_USER_NAME, name);
        context.startActivity(intent);
    }


    /**
     * TODO: 회원가입 응답
     * @param code : 회원가입 성공 여부
     * */
    private void processSignUp(int code) {
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_SIGN_UP);
        intent.putExtra(Global.KEY_CODE, code);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * TODO: 로그인 응답 처리
     * @param code : 응답요건
     *             NULL(0) = 아이디/비밀번호 틀림
     *             SUCCESS(1) = 성공
     * @param id : 자동로그인용 id
     * */
    private void processLoginRes(int code, String id, String name) {
        Log.d(TAG, "로그인 응답");
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_LOGIN);
        intent.putExtra(Global.KEY_CODE, code);
        intent.putExtra(Global.KEY_USER_ID, id);
        intent.putExtra(Global.KEY_USER_NAME, name);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * TODO: 로그인 요청 시 실행
     * @param id : 아이디
     * @param pw : 비밀번호
     * @param code : 로그인 타입
     * */
    public void login(String id, String pw, int code) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_USER_ID, id);
            if (code == Global.ONE_DAY) {
                pw = Secure.Sha256Encrypt(pw);
                Log.d(TAG, "pw = " + pw);
                obj.put(Global.KEY_USER_PW, pw);
            }
            socket.emit(Global.KEY_LOGIN, obj);
        } catch (Exception e) {
        }
    }


    /**
     * TODO: 회원가입
     * @param id : 아이디
     * @param pw : 비밀번호
     * */
    public void signUp(String id, String pw, String mail, Date birth) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_USER_ID, id);
            pw = Secure.Sha256Encrypt(pw);              // 암호화
            obj.put(Global.KEY_USER_PW, pw);
            obj.put(Global.KEY_USER_MAIL, mail);
            obj.put(Global.KEY_USER_BIRTH, birth);
            socket.emit(Global.KEY_SIGN_UP, obj);
        } catch (Exception e) {
            Log.e(TAG, "signUp 에러 = " + e.getMessage());
        }
    }


    /**
     * TODO: 프로필 요청
     * @param id : 아이디
     * */
    public void getProfile(String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_USER_ID, id);
            socket.emit(Global.KEY_GET_PROFILE, obj);
        } catch (Exception e) {

        }
    }


    /**
     * TODO: 게시글 읽어오기
     * @param count : 몇 번째 리스트를 가져올건지 결정
     * */
    public void readNotice(int count) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_COUNT, count);
            socket.emit(Global.KEY_READ_NOTICE, obj);
        } catch (Exception e) {
            Log.e(TAG, "readNotice 에러 = " + e.getMessage());
        }
    }


    /**
     * TODO: 게시글 읽어오기
     * @param count : 몇 번째 리스트를 가져올건지 결정
     * @param keyWord : 검색 키워드
     * */
    public void readNotice(int count, String keyWord) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_COUNT, count);
            obj.put(Global.KEY_KEY_WORD, keyWord);
            socket.emit(Global.KEY_READ_NOTICE, obj);
        } catch (Exception e) {
            Log.e(TAG, "readNotice 에러 = " + e.getMessage());
        }
    }


    /**
     * TODO: 글쓰기
     * @param id : 아이디
     * @param content : 내용
     * @param imageList : 이미지
     * */
    public void postNotice(String id, String content, ArrayList<Bitmap> imageList, String name) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_CONTENT, content);
            obj.put(Global.KEY_IMAGE, imageList);
            obj.put(Global.KEY_USER_ID, id);
            obj.put(Global.KEY_USER_NAME, name);
            socket.emit(Global.KEY_POST_NOTICE, obj);
        } catch (Exception e) {

        }
    }


    /**
     * TODO: 닉네임 설정
     * @param id : 아이디
     * @param name : 닉네임
     * */
    public void setName(String id, String name) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_USER_NAME, name);
            obj.put(Global.KEY_USER_ID, id);
            socket.emit(Global.KEY_SET_NAME, obj);
        } catch (Exception e) {
        }
    }
}
