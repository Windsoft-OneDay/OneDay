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
import com.windsoft.oneday.Global;
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

    private MainAdapter adapter;

    private String id;
    private int count;

    public MainFragment() {
    }


    public static MainFragment newInstance(String id) {
        MainFragment fragment = new MainFragment();
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
        adapter = new MainAdapter(getActivity(), noticeList, id);
        recyclerView.setAdapter(adapter);
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {           // swipe refresh
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh count = " + count);
                count = 0;
                readNotice(count);
            }
        });
        count = 0;
        readNotice(count);
        recyclerView.enableLoadmore();
        recyclerView.reenableLoadmore();
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                Log.d(TAG, "loadMore count = " + count);
                readNotice(count);
            }
        });
    }


    public void setData(ArrayList<NoticeModel> noticeList) {
        adapter.setItem(noticeList);
    }


    public void addData(ArrayList<NoticeModel> noticeList) {
        adapter.addItem(noticeList);
    }


    public void readNotice(int count) {
        adapter.readNotice(count);
        this.count = count + 1;
    }


    public void setCount(int count) {
        this.count = count;
    }
}
