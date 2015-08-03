package com.windsoft.oneday.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.windsoft.oneday.R;
import com.windsoft.oneday.model.NoticeModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ironFactory on 2015-08-04.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private static final String TAG = "MainAdapter";

    private ArrayList<NoticeModel> noticeList;
    private Context context;

    public MainAdapter(Context context, ArrayList<NoticeModel> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notice, parent, false);
        return new ViewHolder(view);
    }


    private String getTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        StringBuffer sb = new StringBuffer();

        if (day != 0)
            sb.append(day + "일 ");
        if (hour != 0)
            sb.append(hour + "시간 ");
        if (min != 0)
            sb.append(min + "분 ");
        if (sec != 0)
            sb.append(sec + "초");

        return sb.toString();
    }


    private void setGoodColor(ViewHolder holder, int position) {
        boolean isCheckedGood = noticeList.get(position).isCheckedGood();
        if (isCheckedGood) {            // 좋아요 눌러져 있다면
            holder.good.setTextColor(Color.BLACK);
            holder.goodBtn.setImageResource(R.drawable.splash);
        } else {
            holder.good.setTextColor(Color.RED);
            holder.goodBtn.setImageResource(R.drawable.splash);
        }

        noticeList.get(position).setIsCheckedGood(!noticeList.get(position).isCheckedGood());                // 반대로 바꿈
    }


    private void setBadColor(ViewHolder holder, int position) {
        boolean isCheckedBad = noticeList.get(position).isCheckedBad();
        if (isCheckedBad) {            // 좋아요 눌러져 있다면
            holder.bad.setTextColor(Color.BLACK);
            holder.badBtn.setImageResource(R.drawable.splash);
        } else {
            holder.bad.setTextColor(Color.RED);
            holder.badBtn.setImageResource(R.drawable.splash);
        }

        noticeList.get(position).setIsCheckedBad(!noticeList.get(position).isCheckedGood());                // 반대로 바꿈
    }


    private void setCommentColor(ViewHolder holder, int position) {
        boolean isCommented = noticeList.get(position).isCommented();
        if (isCommented) {            // 좋아요 눌러져 있다면
            holder.comment.setTextColor(Color.BLACK);
            holder.commentBtn.setImageResource(R.drawable.splash);
        } else {
            holder.comment.setTextColor(Color.RED);
            holder.commentBtn.setImageResource(R.drawable.splash);
        }

        noticeList.get(position).setIsCommented(!noticeList.get(position).isCommented());                // 반대로 바꿈
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NoticeModel notice = noticeList.get(position);
        holder.profileImage.setImageBitmap(notice.getProfileImage());
        holder.name.setText(notice.getName());
        holder.time.setText(getTime(notice.getTime()));
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Custom Alert Dialog
            }
        });
        holder.content.setText(notice.getContent());
        holder.goodNum.setText(notice.getGoodNum());
        holder.badNum.setText(notice.getBadNum());
        holder.commentNum.setText(notice.getCommentNum());

        setGoodColor(holder, position);
        holder.goodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGoodColor(holder, position);
            }
        });

        setBadColor(holder, position);
        holder.badLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBadColor(holder, position);
            }
        });

        setCommentColor(holder, position);
        holder.commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCommentColor(holder, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class ViewHolder extends UltimateRecyclerviewViewHolder {

        ImageView profileImage;
        TextView name;
        TextView time;
        ImageButton menu;
        TextView content;
        TextView goodNum;
        TextView badNum;
        TextView commentNum;
        LinearLayout goodLayout;
        LinearLayout badLayout;
        LinearLayout commentLayout;
        TextView good;
        TextView bad;
        TextView comment;
        ImageButton goodBtn;
        ImageButton badBtn;
        ImageButton commentBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            profileImage = (ImageView) itemView.findViewById(R.id.card_notice_profile_image);
            name = (TextView) itemView.findViewById(R.id.card_notice_profile_name);
            time = (TextView) itemView.findViewById(R.id.card_notice_profile_time);
            menu = (ImageButton) itemView.findViewById(R.id.card_notice_profile_menu);
            content = (TextView) itemView.findViewById(R.id.card_notice_content);
            goodNum = (TextView) itemView.findViewById(R.id.card_notice_good_num);
            badNum = (TextView) itemView.findViewById(R.id.card_notice_bad_num);
            commentNum = (TextView) itemView.findViewById(R.id.card_notice_comment_num);
            goodLayout = (LinearLayout) itemView.findViewById(R.id.card_notice_good_layout);
            badLayout = (LinearLayout) itemView.findViewById(R.id.card_notice_bad_layout);
            commentLayout = (LinearLayout) itemView.findViewById(R.id.card_notice_comment_layout);
            good = (TextView) itemView.findViewById(R.id.card_notice_good_text);
            bad = (TextView) itemView.findViewById(R.id.card_notice_bad_text);
            comment = (TextView) itemView.findViewById(R.id.card_notice_comment_text);
            goodBtn = (ImageButton) itemView.findViewById(R.id.card_notice_good_btn);
            badBtn = (ImageButton) itemView.findViewById(R.id.card_notice_bad_btn);
            commentBtn = (ImageButton) itemView.findViewById(R.id.card_notice_comment_btn);

        }
    }
}
