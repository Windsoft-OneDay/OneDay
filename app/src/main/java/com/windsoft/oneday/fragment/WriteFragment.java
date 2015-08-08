package com.windsoft.oneday.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nispok.snackbar.Snackbar;
import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;

import java.util.ArrayList;

/**
 * Created by ironFactory on 2015-08-04.
 */
public class WriteFragment extends Fragment {

    private static final String TAG = "WriteFragment";

    private EditText contentInput;                          // 글
    private LinearLayout pictureContainer;                  // 골라진 사진 보이는 레이아웃
    private LinearLayout takePictureContainer;              // 사진 찍기 버튼
    private LinearLayout choicePictureContainer;            // 사진 고르기 버튼

    private String content;
    private ArrayList<Bitmap> imageList = new ArrayList<>();

    private static final int TAKE_CAMERA = 0;
    private static final int TAKE_GALLERY = 1;

    private String id;
    private String name;

    public WriteFragment() {
    }


    public static WriteFragment newInstance(String id, String name) {
        WriteFragment fragment = new WriteFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Global.KEY_USER_ID, id);
        bundle.putString(Global.KEY_USER_NAME, name);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        id = bundle.getString(Global.KEY_USER_ID);
        name = bundle.getString(Global.KEY_USER_NAME);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_write, container, false);

        init(rootView);

        return rootView;
    }


    private void init(View rootView) {
        contentInput = (EditText) rootView.findViewById(R.id.fragment_write_content);
        pictureContainer = (LinearLayout) rootView.findViewById(R.id.fragment_write_image_container);
        takePictureContainer = (LinearLayout) rootView.findViewById(R.id.fragment_write_take_picture_layout);
        choicePictureContainer = (LinearLayout) rootView.findViewById(R.id.fragment_write_choice_picture_layout);

        setListener();
    }


    private void setListener() {
        takePictureContainer.setOnClickListener(new View.OnClickListener() {                    // 사진 찍기 버튼 클릭
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_CAMERA);
            }
        });

        choicePictureContainer.setOnClickListener(new View.OnClickListener() {                  // 사진 고르기 버튼 클릭
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, TAKE_GALLERY);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_CAMERA || requestCode == TAKE_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                Log.d(TAG, "uri = " + uri);

                Bitmap bitmap = getBitmap(uri);
                addPhoto(bitmap);
            }
        }
    }


    private Bitmap getBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        } catch (Exception e) {
            Log.e(TAG, "getBitmap() 에러 = " + e.getMessage());
        }
        return bitmap;
    }


    private void addPhoto(Bitmap image) {
        int width = getActivity().getResources().getDimensionPixelSize(R.dimen.image_width);
        int height = getActivity().getResources().getDimensionPixelSize(R.dimen.image_height);
        int margin = getActivity().getResources().getDimensionPixelSize(R.dimen.base_margin);
        int btnWidth = getActivity().getResources().getDimensionPixelSize(R.dimen.btn_small);
        int btnHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.btn_small);

        final Bitmap bitmap = Bitmap.createScaledBitmap(image, width, height, false);
        imageList.add(bitmap);

        final FrameLayout layout = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        layout.setLayoutParams(params);
        layout.setPadding(0, margin, 0, margin);

        ImageView imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        params = new FrameLayout.LayoutParams(
                width,
                height
        );
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bitmap);
        layout.addView(imageView);

        final ImageButton imageButton = new ImageButton(getActivity());
        params = new FrameLayout.LayoutParams(
                btnWidth,
                btnHeight
        );
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        imageButton.setLayoutParams(params);
        imageButton.setBackgroundResource(R.drawable.splash);
        layout.addView(imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureContainer.removeView(layout);
                imageList.remove(bitmap);
            }
        });

        pictureContainer.addView(layout);
    }


    public void post() {
        content = contentInput.getText().toString();

        if (content.length() == 0 && imageList.size() == 0) {                   // 아무것도 입력하지 않았을 때
            Snackbar.with(getActivity())
                    .text(R.string.fragment_write_no_msg)
                    .show(getActivity());
        } else {
            Intent intent = new Intent(getActivity(), OneDayService.class);
            intent.putExtra(Global.KEY_USER_ID, id);
            intent.putExtra(Global.KEY_USER_NAME, name);
            intent.putExtra(Global.KEY_COMMAND, Global.KEY_POST_NOTICE);
            intent.putExtra(Global.KEY_CONTENT, content);
            intent.putParcelableArrayListExtra(Global.KEY_IMAGE, imageList);
            getActivity().startService(intent);
        }
    }
}
