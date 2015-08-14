package com.windsoft.oneday.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
import com.windsoft.oneday.R;
import com.windsoft.oneday.model.CommentModel;

import java.util.ArrayList;

/**
 * Created by ironFactory on 2015-08-12.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private static final String TAG = "commentAdapter";
    private static final int BODY = 0;
    private static final int FOOTER = 1;

    private Context context;
    private ArrayList<CommentModel> commentList;
    private String id;
    private String name;
    private String image;
    private String noticeId;

    public CommentAdapter(Context context, ArrayList<CommentModel> commentList, String id, String noticeId, String name, String image) {
        this.commentList = commentList;
        this.context = context;
        this.id = id;
        this.noticeId = noticeId;
        this.name = name;
        this.image = image;
    }


    public void setName(String name) {
        this.name = name;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CommentModel comment = commentList.get(position);
        if (comment.getImage() == null || comment.getImage().equals("null")) {
            holder.profileImage.setImageResource(R.drawable.base_profile);
        } else {
            Bitmap profileImage = Global.decodeImage(comment.getImage());
            holder.profileImage.setImageBitmap(profileImage);
        }

        holder.content.setText(comment.getComment());
        holder.name.setText(comment.getName());
    }


    public void addItem(String input) {
        notifyDataSetChanged();
        commentList.add(new CommentModel(id, name, image, input));
    }


    public void processComment(String input) {
        Intent intent = new Intent(context, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_COMMENT);
        intent.putExtra(Global.KEY_COMMENT, input);
        intent.putExtra(Global.KEY_USER_ID, id);
        intent.putExtra(Global.KEY_USER_NAME, name);
        intent.putExtra(Global.KEY_NOTICE_ID, noticeId);
        intent.putExtra(Global.KEY_POSITION, commentList.size());
        intent.putExtra(Global.KEY_COMMENT_POSITION, commentList.size());
        context.startService(intent);
    }


    public void failComment(String comment, int position) {
        notifyDataSetChanged();
        commentList.remove(position);
    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView name;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);

            profileImage = (ImageView) itemView.findViewById(R.id.item_comment_profile_photo);
            name = (TextView) itemView.findViewById(R.id.item_comment_name);
            content = (TextView) itemView.findViewById(R.id.item_comment_content);
        }
    }
}
