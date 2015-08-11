package com.windsoft.oneday.login;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by dongkyu Lee on 2015-06-17.
 * TODO: 네이버 로그인 클래스
 * manifest에 launcher activity에 <action android:name="kr.or.hil.humaninlove.action.MAIN" /> 추가
 *
 * <activity android:name="com.nhn.android.naverlogin.ui.OAuthLoginActivity"
 * android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
 * <activity android:name="com.nhn.android.naverlogin.ui.OAuthLoginInAppBrowserActivity"
 * android:label="OAuth2.0 In-app"/> 추가
 */
public class NaverLogin {

    private static final String TAG = "NaverLogin";
    private static final String ID = "zxDpT97J49YFbKlAMesk";
    private static final String KEY = "zt3mRyAc_a";
    private static final String CLIENT_NAME = "OneDay";

    private static final String INTENT = "com.windsoft.oneday.START";

    private OAuthLogin naverModule;
    private OAuthLoginHandler handler;
    private OAuthLoginButton loginButton;

    private String refreshToken = null;
    private String accessToken = null;

    private Activity activity;

    private OnNaverLoginHandler sender;

    public NaverLogin(Activity activity) {
        this.activity = activity;
        sender = (OnNaverLoginHandler) activity;

        init();
    }


    public OAuthLoginButton getLoginButton() {
        return loginButton;
    }


    private void init() {
        loginButton = new OAuthLoginButton(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        loginButton.setLayoutParams(params);

        naverModule = OAuthLogin.getInstance();
        naverModule.init(activity, ID, KEY, CLIENT_NAME, INTENT);
        naverModule.refreshAccessToken(activity);

        handler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    accessToken = naverModule.getAccessToken(activity);
                    refreshToken = naverModule.getRefreshToken(activity);

                    Log.d(TAG, "accessToken = " + accessToken);
                    Log.d(TAG, "refreshToken = " + refreshToken);

                    getData();
                } else {
                    String errorCode = naverModule.getLastErrorCode(activity).getCode();
                    Log.e(TAG, "에러코드 = " + errorCode);
                }
            }
        };

        login();
    }


    public void login() {
        if (loginButton != null) {
            loginButton.setOAuthLoginHandler(handler);
        }
    }


    public void logout() {
        naverModule.logoutAndDeleteToken(activity);
    }


    public void getData() {
        new Thread (new Runnable()  {
            @Override
            public void run() {
                if (accessToken != null) {
                    String url = "https://apis.naver.com/nidlogin/nid/getUserProfile.xml";
                    String data = naverModule.requestApi(activity, accessToken, url);

                    Log.d(TAG, "data = " + data);

                    try {
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser parser = factory.newSAXParser();
                        XMLReader reader = parser.getXMLReader();
                        SaxHandler handler = new SaxHandler();
                        reader.setContentHandler(handler);

                        InputStream is = new ByteArrayInputStream(data.getBytes("utf-8"));
                        reader.parse(new InputSource(is));

                        String email = handler.email.toString();

                        Log.d(TAG, "email = " + email);

                        sender.onNaverLogin(email);

                    } catch (Exception e) {

                    }
                }
            }
        }).start();
    }


    public interface OnNaverLoginHandler{
        void onNaverLogin(String email);
    }


    private class SaxHandler extends DefaultHandler {
        boolean initem = false;
        StringBuilder email = new StringBuilder();

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
        }


        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (localName.equals("email")) {
                initem = true;
            }

            super.startElement(uri, localName, qName, attributes);
        }


        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
        }


        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (initem) {
                email.append(ch, start, length);
                initem = false;
            }

            super.characters(ch, start, length);
        }
    }

}
