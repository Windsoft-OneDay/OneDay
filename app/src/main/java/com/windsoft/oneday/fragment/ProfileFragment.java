package com.windsoft.oneday.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;
import com.windsoft.oneday.adapter.ProfileAdapter;
import com.windsoft.oneday.model.NoticeModel;

import java.util.ArrayList;

/**
 * Created by ironFactory on 2015-08-04.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private RecyclerView recyclerView;

    private String id;

    private ProfileAdapter adapter;

    public ProfileFragment() {
    }


    public static ProfileFragment newInstance(String id) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Global.KEY_USER_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        id = bundle.getString(Global.KEY_USER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        init(rootView);

        return rootView;
    }


    private void init(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_profile_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

//        getProfile();
    }


    /**
     * TODO: 데이터 설정
     * */
    public void setData(ArrayList<NoticeModel> noticeList, Bitmap image, String name) {
        if (adapter == null) {
            adapter = new ProfileAdapter(noticeList, image, name);
            recyclerView.setAdapter(adapter);
        }
        adapter.setData(noticeList, image, name);
    }


    /**
     * TODO: 프로필 요청하기
     * */
    public void getProfile() {
        Intent intent = new Intent(getActivity(), OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_GET_PROFILE);
        intent.putExtra(Global.KEY_USER_ID, id);
        getActivity().startService(intent);
    }
}
