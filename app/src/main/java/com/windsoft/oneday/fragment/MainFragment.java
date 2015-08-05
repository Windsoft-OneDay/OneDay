package com.windsoft.oneday.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.windsoft.oneday.R;
import com.windsoft.oneday.adapter.MainAdapter;
import com.windsoft.oneday.model.NoticeModel;

import java.util.ArrayList;

/**
 * Created by ironFactory on 2015-08-04.
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private UltimateRecyclerView recyclerView;

    private ArrayList<NoticeModel> noticeList;

    public MainFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        init(rootView);

        return rootView;
    }


    private void init(View rootView) {
        recyclerView = (UltimateRecyclerView) rootView.findViewById(R.id.fragment_main_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        noticeList = new ArrayList<>();
        MainAdapter adapter = new MainAdapter(getActivity(), noticeList);
        recyclerView.setAdapter(adapter);
        recyclerView.enableLoadmore();
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {           // swipe refresh
            @Override
            public void onRefresh() {
                Log.d(TAG, "Swipe Refresh");
            }
        });
    }
}