package com.windsoft.oneday.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.windsoft.oneday.R;

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

    private static final int TAKE_CAMERA = 0;
    private static final int TAKE_GALLERY = 1;

    public WriteFragment() {
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


    private void addPhoto(Bitmap bitmap) {
        int height = getActivity().getResources().getDimensionPixelSize(R.dimen.image_height);
        int margin = getActivity().getResources().getDimensionPixelSize(R.dimen.base_margin);
        int btnWidth = getActivity().getResources().getDimensionPixelSize(R.dimen.btn_small);
        int btnHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.btn_small);


        RelativeLayout layout = new RelativeLayout(getActivity());
        layout.setGravity(RelativeLayout.CENTER_VERTICAL | RelativeLayout.CENTER_HORIZONTAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height
        );
        layout.setLayoutParams(params);

        ImageView imageView = new ImageView(getActivity());
        params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                height
        );
        params.setMargins(margin, margin, margin, margin);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bitmap);
        layout.addView(imageView);

        final ImageButton imageButton = new ImageButton(getActivity());
        params = new RelativeLayout.LayoutParams(
                btnWidth,
                btnHeight
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        imageButton.setLayoutParams(params);
        imageButton.setBackgroundResource(R.drawable.splash);
        imageButton.setTag(layout);
        layout.addView(imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout relativeLayout = (RelativeLayout) imageButton.getTag();
                pictureContainer.removeView(relativeLayout);
            }
        });

        pictureContainer.addView(layout);
    }
}
