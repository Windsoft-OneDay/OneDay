package com.windsoft.oneday.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.windsoft.oneday.Global;
import com.windsoft.oneday.R;
import com.windsoft.oneday.adapter.CommentAdapter;
import com.windsoft.oneday.model.NoticeModel;

/**
 * Created by ironFactory on 2015-08-13.
 */
public class CommentFragment extends Fragment {

    private CommentAdapter commentAdapter;
    private RecyclerView commentList;

    private static NoticeModel notice;
    private String id;
    private String name;
    private String image;

    private EditText commentInput;
    private ImageButton submit;

    public CommentFragment() {
    }


    public static CommentFragment createInstance(NoticeModel notice, String id, String name, String image) {
        CommentFragment fragment = new CommentFragment();
        CommentFragment.notice = notice;
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
        View rootView = inflater.inflate(R.layout.fragment_comment, container, false);
        init(rootView);
        return rootView;
    }


    private void init(View rootView) {
        commentList = (RecyclerView) rootView.findViewById(R.id.fragment_comment_recycler);
        commentAdapter = new CommentAdapter(getActivity(), notice.getCommentList(), id, notice.getNoticeId(), name, image);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        commentList.setLayoutManager(manager);
        commentList.setAdapter(commentAdapter);

        commentInput = (EditText) rootView.findViewById(R.id.fragment_comment_input);
        submit = (ImageButton) rootView.findViewById(R.id.fragment_comment_input_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = commentInput.getText().toString();
                if (input.length() > 0) {
                    commentAdapter.processComment(input);
                    commentAdapter.addItem(input);
                    commentInput.setText("");
                }
            }
        });
    }


    public void failComment(String comment, int position) {
        commentAdapter.failComment(comment, position);
    }


    public void setName(String name) {
        this.name = name;
        commentAdapter.setName(name);
    }
}
