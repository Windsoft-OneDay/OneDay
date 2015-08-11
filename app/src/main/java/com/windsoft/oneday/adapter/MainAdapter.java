package com.windsoft.oneday.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.windsoft.oneday.Global;
import com.windsoft.oneday.OneDayService;
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
    private String id;

    public MainAdapter(Context context, ArrayList<NoticeModel> noticeList, String id) {
        this.context = context;
        this.noticeList = noticeList;
        this.id = id;
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


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NoticeModel notice = noticeList.get(position);
        holder.imageContainer.removeAllViews();

        if (notice.getProfileImage() == null) {

        } else {
            Bitmap profileImage = Global.decodeImage(notice.getProfileImage());
            holder.profileImage.setImageBitmap(profileImage);
        }

        long time = System.currentTimeMillis() - notice.getDate().getTime();
        holder.name.setText(notice.getName());
        holder.time.setText(getTime(time));
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Custom Alert Dialog
            }
        });
        holder.content.setText(notice.getContent());
        holder.goodNum.setText(String.valueOf(notice.getGoodNum()));
        holder.badNum.setText(String.valueOf(notice.getBadNum()));
        holder.commentNum.setText(String.valueOf(notice.getCommentNum()));

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

        Log.d(TAG, "position = " + position);
        for (int i = 0; i < notice.getImageList().size(); i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageView.setLayoutParams(params);

            Bitmap bitmap = Global.decodeImage(notice.getImageList().get(i));
            imageView.setImageBitmap(bitmap);
            holder.imageContainer.addView(imageView);
        }
    }


    public void addItem(NoticeModel notice) {
        noticeList.add(notice);
        notifyDataSetChanged();
    }


    public void addItem(ArrayList<NoticeModel> noticeList) {
        for (NoticeModel notice :
                noticeList) {
            this.noticeList.add(notice);
        }
        notifyDataSetChanged();
    }


    public void setItem(ArrayList<NoticeModel> noticeList) {
        notifyDataSetChanged();
        Log.d(TAG, "size = " + noticeList.size());
        this.noticeList = noticeList;
    }


    @Override
    public int getItemCount() {
        return noticeList.size();
    }


    public void readNotice(int count) {
        Intent intent = new Intent(context, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_READ_NOTICE);
        intent.putExtra(Global.KEY_COUNT, count);
        intent.putExtra(Global.KEY_USER_ID, id);
        context.startService(intent);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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
        LinearLayout imageContainer;
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
            imageContainer = (LinearLayout) itemView.findViewById(R.id.card_notice_image_container);
        }
    }
}
