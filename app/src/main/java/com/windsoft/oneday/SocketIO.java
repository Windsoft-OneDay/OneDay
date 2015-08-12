package com.windsoft.oneday;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.windsoft.oneday.activity.LoginActivity;
import com.windsoft.oneday.activity.MainActivity;
import com.windsoft.oneday.model.CommentModel;
import com.windsoft.oneday.model.NoticeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ironFactory on 2015-08-03.
 */
public class SocketIO {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_IMAGE = "user_img";
    public static final String KEY_USER_NAME = "name";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_DATE = "date";
    public static final String KEY_IMAGE = "img";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_GOOD = "good";
    public static final String KEY_BAD = "bad";
    public static final String KEY_NOTICE_ID = "notice_id";

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
                    Log.d(TAG, "obj = " + obj);
                    int code = obj.getInt(Global.KEY_CODE);
                    String id = null;
                    String name = null;
                    String image = null;
                    if (code == Global.CODE_SUCCESS) {
                        id = obj.getString(Global.KEY_USER_ID);
                        image = obj.getString(Global.KEY_USER_IMAGE);
                        name = obj.getString(Global.KEY_USER_NAME);
                    }
                    processLoginRes(code, id, name, image);
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
                try {
                    JSONObject obj = (JSONObject) args[0];
                    int code = obj.getInt(Global.KEY_CODE);
                    String id = null;
                    JSONArray array = null;

                    if (code == Global.CODE_SUCCESS) {
                        id = obj.getString(Global.KEY_USER_ID);
                        array = obj.getJSONArray(Global.KEY_NOTICE);
                    }
                    int count = obj.getInt(Global.KEY_COUNT);
                    processReadNotice(code, array, id, count);
                } catch (Exception e) {
                    Log.e(TAG, "글 받아오기 오류 = " + e.getMessage());
                }
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
                    processPost(code);
                    Log.d(TAG, " 글쓰기 응답 ");
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
                    String name = null;
                    if (code == Global.CODE_SUCCESS)
                        name = obj.getString(Global.KEY_USER_NAME);
                    processSetName(code, name);
                    Log.d(TAG, " 닉네임 설정 응답 ");
                } catch (Exception e) {
                    Log.e(TAG, "닉네임 설정 응답 오류 = " + e.getMessage());
                }
            }
        }).on(Global.KEY_GOOD, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject) args[0];
                    int code = obj.getInt(Global.KEY_CODE);
                    boolean flag = obj.getBoolean(Global.KEY_FLAG);
                    int position = obj.getInt(Global.KEY_POSITION);
                    processGood(code, flag, position);
                    Log.d(TAG, "좋아요 응답");
                } catch (Exception e) {
                    Log.e(TAG, "좋아요 오류 = " + e.getMessage());
                }
            }
        }).on(Global.KEY_BAD, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject obj = (JSONObject) args[0];
                    int code = obj.getInt(Global.KEY_CODE);
                    boolean flag = obj.getBoolean(Global.KEY_FLAG);
                    int position = obj.getInt(Global.KEY_POSITION);
                    processBad(code, flag, position);
                    Log.d(TAG, "싫어요 응답");
                } catch (Exception e) {
                    Log.e(TAG, "싫어요 오류 = " + e.getMessage());
                }
            }
        });

        socket.open();
        socket.connect();
    }


    private void processGood(int code, boolean flag, int position) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_GOOD);
        intent.putExtra(Global.KEY_CODE, code);
        intent.putExtra(Global.KEY_FLAG, flag);
        intent.putExtra(Global.KEY_POSITION, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void processBad(int code, boolean flag, int position) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_BAD);
        intent.putExtra(Global.KEY_CODE, code);
        intent.putExtra(Global.KEY_FLAG, flag);
        intent.putExtra(Global.KEY_POSITION, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private void processReadNotice(int code, JSONArray array, String id, int count) {
        Log.d(TAG, "글 읽기 응답");
        ArrayList<NoticeModel> noticeList = new ArrayList<>();

        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = (JSONObject) array.get(i);

                    NoticeModel model = parsingNotice(obj, id);
                    noticeList.add(model);
                } catch (Exception e) {
                    Log.e(TAG, "notice 파싱 에러 = " + e.getMessage());
                }
            }
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_READ_NOTICE);
        intent.putExtra(Global.KEY_CODE, code);
        intent.putExtra(Global.KEY_NOTICE, noticeList);
        intent.putExtra(Global.KEY_COUNT, count);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private NoticeModel parsingNotice(JSONObject object, String id) throws Exception{
        String userImage = (String) object.get(KEY_USER_IMAGE);
        if (userImage.equals("null")) {
            Log.d(TAG, "userImage = null");
            userImage = null;
        }
        String userId = (String) object.get(KEY_USER_ID);
        String userName = (String) object.get(KEY_USER_NAME);
        String content = (String) object.get(KEY_CONTENT);
        String dateStr = (String) object.get(KEY_DATE);
        String noticeId = (String) object.get(KEY_NOTICE_ID);
        Date date = getDate(dateStr);

        JSONArray imageArray = object.getJSONArray(KEY_IMAGE);
        ArrayList<String> imageList = new ArrayList<>();
        for (int i = 0; i < imageArray.length(); i++) {
            imageList.add((String) imageArray.get(i));
        }
        ArrayList<CommentModel> commentList = new ArrayList<>();
        JSONArray commentArray = (JSONArray) object.get(KEY_COMMENT);

        JSONObject goodObj = object.getJSONObject(KEY_GOOD);
        JSONObject badObj = object.getJSONObject(KEY_BAD);

        JSONArray goodArray = goodObj.getJSONArray(KEY_USER_ID);
        JSONArray badArray = badObj.getJSONArray(KEY_USER_ID);

        int goodInt = goodArray.length();
        int badInt = badArray.length();

        boolean isGooded = false;
        boolean isBaded = false;

        for (int i = 0; i < goodInt; i++) {
            String curId = goodArray.getString(i);
            if (id.equals(curId)) {
                isGooded = true;
                break;
            }
        }

        for (int i = 0; i < badInt; i++) {
            String curId = badArray.getString(i);
            if (id.equals(curId)) {
                isBaded = true;
                break;
            }
        }

        for (int i = 0; i < commentArray.length(); i++) {
            JSONObject commentObj = commentArray.getJSONObject(i);
            commentList.add(parsingComment(commentObj));
        }


        NoticeModel model = new NoticeModel(userId, userImage, userName, date, content, goodInt, badInt, commentList.size(), isGooded, isBaded, commentList, imageList, noticeId);
        return model;
    }


    private Date getDate(String str) {
        float time = context.getResources().getInteger(R.integer.time);

        int year = Integer.parseInt(str.substring(0, 4));
        int month = Integer.parseInt(str.substring(5, 7)) - 1;
        int day = Integer.parseInt(str.substring(8, 10));
        int hour = Integer.parseInt(str.substring(11, 13)) + (int) time;
        int min = Integer.parseInt(str.substring(14, 16));
        int sec = Integer.parseInt(str.substring(17, 19));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, sec);
        return calendar.getTime();
    }


    private CommentModel parsingComment(JSONObject object) throws Exception{
        String id = (String) object.get(KEY_USER_ID);
        String name = (String) object.get(KEY_USER_NAME);
        String content = (String) object.get(KEY_CONTENT);
        String image = (String) object.get(KEY_USER_IMAGE);

        CommentModel model = new CommentModel(id, name, image, content);
        return model;
    }


    /**
     * TODO: 글쓰기 응답
     * @param code : 응답 코드
     * */
    private void processPost(int code) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_POST_NOTICE);
        intent.putExtra(Global.KEY_CODE, code);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * TODO: 회원가입 응답
     * @param code : 회원가입 성공 여부
     * */
    private void processSignUp(int code) {
        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
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
    private void processLoginRes(int code, String id, String name, String image) {
        Log.d(TAG, "로그인 응답");
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_LOGIN);
        intent.putExtra(Global.KEY_CODE, code);
        intent.putExtra(Global.KEY_USER_ID, id);
        intent.putExtra(Global.KEY_USER_NAME, name);
        intent.putExtra(Global.KEY_USER_IMAGE, image);
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
    public void readNotice(int count, String id) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_COUNT, count);
            obj.put(Global.KEY_USER_ID, id);
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
    public void readNotice(int count, String id, String keyWord) {
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
    public void postNotice(String id, String content, ArrayList<String> imageList, String name, String userImage) {
        try {
            Log.d(TAG, "postNotice()");
            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            for (int i = 0; i < imageList.size(); i++) {
                array.put(imageList.get(i));
            }
            obj.put(Global.KEY_CONTENT, content);
            obj.put(Global.KEY_IMAGE, array);
            obj.put(Global.KEY_USER_ID, id);
            obj.put(Global.KEY_USER_IMAGE, userImage);
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


    public void goodCheck(boolean flag, String userId, String noticeId, int position) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_FLAG, flag);
            obj.put(Global.KEY_USER_ID, userId);
            obj.put(Global.KEY_NOTICE_ID, noticeId);
            obj.put(Global.KEY_POSITION, position);
            socket.emit(Global.KEY_GOOD, obj);
        } catch (Exception e) {
        }
    }


    public void badCheck(boolean flag, String userId, String noticeId, int position) {
        try {
            JSONObject obj = new JSONObject();
            obj.put(Global.KEY_FLAG, flag);
            obj.put(Global.KEY_USER_ID, userId);
            obj.put(Global.KEY_NOTICE_ID, noticeId);
            obj.put(Global.KEY_POSITION, position);
            socket.emit(Global.KEY_BAD, obj);
        } catch (Exception e) {
        }
    }
}
