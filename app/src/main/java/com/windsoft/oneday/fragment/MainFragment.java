package com.windsoft.oneday.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
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
    private String name;
    private String image;
    private String keyword = null;
    private int count;

    public MainFragment() {
    }


    public static MainFragment newInstance(String id, String name, String image) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Global.KEY_USER_ID, id);
        bundle.putString(Global.KEY_USER_NAME, name);
        bundle.putString(Global.KEY_USER_IMAGE, image);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        id = bundle.getString(Global.KEY_USER_ID);
        name = bundle.getString(Global.KEY_USER_NAME);
        image = bundle.getString(Global.KEY_USER_IMAGE);
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
        adapter = new MainAdapter(getActivity(), noticeList, id, name, image);
        recyclerView.setAdapter(adapter);
        recyclerView.setDefaultOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {           // swipe refresh
            @Override
            public void onRefresh() {
                count = 0;
                if (keyword == null)
                    readNotice(count);
                else
                    readNotice(keyword, count);
            }
        });
        count = 0;
        readNotice(count);
        recyclerView.enableLoadmore();
        recyclerView.reenableLoadmore();
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int i, int i1) {
                if (keyword == null)
                    readNotice(count);
                else
                    readNotice(keyword, count);
            }
        });
    }


    public void setKeyword(String keyword) {
        Intent intent = new Intent(getActivity(), OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_READ_NOTICE);
        intent.putExtra(Global.KEY_USER_ID, id);
        intent.putExtra(Global.KEY_COUNT, 0);
        intent.putExtra(Global.KEY_KEY_WORD, keyword);
        getActivity().startService(intent);
        this.keyword = keyword;
    }


    public void setData(ArrayList<NoticeModel> noticeList) {
        this.noticeList = noticeList;
        sortByGood();
//        adapter.setItem(noticeList);
    }


    public void addData(ArrayList<NoticeModel> noticeList) {
        this.noticeList.addAll(noticeList);
        adapter.addItem(noticeList);
    }


    public void readNotice(int count) {
        adapter.readNotice(count);
        this.count = count + 1;
    }


    public void readNotice(String keyword, int count) {
        adapter.readNotice(keyword, count);
        this.count = count + 1;
    }


    public void setCount(int count) {
        this.count = count;
    }


    public void failGood(boolean flag, int position) {
        adapter.failGood(flag, position);
    }


    public void failBad(boolean flag, int position) {
        adapter.failBad(flag, position);
    }


    public void addComment(String comment, String noticeId) {
        adapter.addComment(comment, noticeId);
    }


    public void setName(String name) {
        this.name = name;
        adapter.setName(name);
    }


    // 좋아요 순 정렬
    public void sortByGood() {
        final int SIZE = noticeList.size();
        NoticeModel indexNotice;
        ArrayList<Boolean> sorted = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            sorted.add(false);
        }

        for (int i = 0; i < SIZE - 1; i++) {
            int pivot = -1;
            int right = SIZE - 1;

            for (int j = 0; j < SIZE - 1; j++) {
                if (pivot != -1 && sorted.get(j)) {
                    right = j - 1;
                }

                if (pivot == -1 && !sorted.get(j)) {
                    pivot = j;
                }
            }

            int left = pivot + 1;
            if (left > right)
                left = right;

            int pivotGood = noticeList.get(pivot).getGoodNum();
            int leftGood = noticeList.get(left).getGoodNum();
            int rightGood = noticeList.get(right).getGoodNum();

            while (left != right) {
                if (pivotGood < leftGood) {
                    left++;
                } else {
                    if (pivotGood < rightGood) {
                        indexNotice = noticeList.get(left);
                        noticeList.set(left, noticeList.get(right));
                        noticeList.set(right, indexNotice);
                        left++;
                    } else {
                        right--;
                    }
                }
                leftGood = noticeList.get(left).getGoodNum();
                rightGood = noticeList.get(right).getGoodNum();
            }

            if (pivotGood < leftGood) {
                if (left != SIZE - 1) {
                    noticeList.add(left + 1, noticeList.get(pivot));
                    noticeList.remove(pivot);
                    sorted.set(left + 1, true);
                } else {
                    noticeList.add(left, noticeList.get(pivot));
                    noticeList.remove(pivot);
                    sorted.set(left, true);
                }
            } else {
                noticeList.add(left, noticeList.get(pivot));
                noticeList.remove(pivot);
                sorted.set(left, true);
            }
        }
        adapter.setItem(noticeList);
    }


    // 시간 순 정렬
    public void sortByTime() {
        final int SIZE = noticeList.size();
        NoticeModel indexNotice;
        ArrayList<Boolean> sorted = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            sorted.add(false);
        }

        for (int i = 0; i < SIZE - 1; i++) {
            int pivot = -1;
            int right = SIZE - 1;

            for (int j = 0; j < SIZE - 1; j++) {
                if (pivot != -1 && sorted.get(j)) {
                    right = j - 1;
                }

                if (pivot == -1 && !sorted.get(j)) {
                    pivot = j;
                }
            }

            int left = pivot + 1;
            if (left > right)
                left = right;

            long pivotGood = noticeList.get(pivot).getDate().getTime();
            long leftGood = noticeList.get(left).getDate().getTime();
            long rightGood = noticeList.get(right).getDate().getTime();

            while (left != right) {
                if (pivotGood < leftGood) {
                    left++;
                } else {
                    if (pivotGood < rightGood) {
                        indexNotice = noticeList.get(left);
                        noticeList.set(left, noticeList.get(right));
                        noticeList.set(right, indexNotice);
                        left++;
                    } else {
                        right--;
                    }
                }
                leftGood = noticeList.get(left).getDate().getTime();
                rightGood = noticeList.get(right).getDate().getTime();
            }

            if (pivotGood < leftGood) {
                if (left != SIZE - 1) {
                    noticeList.add(left + 1, noticeList.get(pivot));
                    noticeList.remove(pivot);
                    sorted.set(left + 1, true);
                } else {
                    noticeList.add(left, noticeList.get(pivot));
                    noticeList.remove(pivot);
                    sorted.set(left, true);
                }
            } else {
                noticeList.add(left, noticeList.get(pivot));
                noticeList.remove(pivot);
                sorted.set(left, true);
            }
        }
        adapter.setItem(noticeList);
    }
}
