package com.windsoft.oneday.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.windsoft.oneday.R;

/**
 * Created by ironFactory on 2015-08-04.
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private UltimateRecyclerView recyclerView;

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
    }
}
