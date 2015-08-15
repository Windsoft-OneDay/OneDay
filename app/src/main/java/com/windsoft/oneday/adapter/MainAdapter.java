package com.windsoft.oneday.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.windsoft.oneday.activity.MainActivity;
import com.windsoft.oneday.model.CommentModel;
import com.windsoft.oneday.model.NoticeModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ironFactory on 2015-08-04.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final String TAG = "MainAdapter";
    private final int MIN3 = 1000 * 60 * 3;

    private ArrayList<NoticeModel> noticeList;
    private Context context;
    private String id;
    private String name;
    private String image;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ViewHolder holder = (ViewHolder) msg.obj;
            Bundle bundle = msg.getData();
            String time = bundle.getString("time");
            holder.time.setText(time);
        }
    };


    public void setName(String name) {
        this.name = name;
    }


    public MainAdapter(Context context, ArrayList<NoticeModel> noticeList, String id, String name, String image) {
        this.context = context;
        this.noticeList = noticeList;
        this.id = id;
        this.name = name;
        this.image = image;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notice, parent, false);
        return new ViewHolder(view);
    }


    private String getTime(long time) {
        final int DAY = 1000 * 60 * 60 * 24;
        final int HOUR = 1000 * 60 * 60;
        final int MIN = 1000 * 60;
        final int SEC = 1000;

        int day = (int) time / DAY;
        time = time - (day * DAY);
        int hour = (int) time / HOUR;
        time = time - (hour * HOUR);
        int min = (int) time / MIN;
        time = time - (min * MIN);
        int sec = (int) time / SEC;

        StringBuffer sb = new StringBuffer();

        if (day != 0)
            sb.append(day + "일 ");
        if (hour != 0)
            sb.append(hour + "시간 ");
        if (min != 0)
            sb.append(min + "분 ");
        sb.append(sec + "초");

        return sb.toString();
    }


    private void setGood(ViewHolder holder, int position) {
        NoticeModel notice = noticeList.get(position);
        boolean isCheckedGood = notice.isCheckedGood();
        int color;

        if (isCheckedGood) {                                // 좋아요
            color = context.getResources().getColor(R.color.main);
            holder.goodBtn.setImageResource(R.drawable.good_icon_main_color);
        } else {                                            // 좋아요 취소
            color = context.getResources().getColor(R.color.gray);
            holder.goodBtn.setImageResource(R.drawable.good_icon);
        }
        holder.good.setTextColor(color);
    }


    private void setBad(ViewHolder holder, int position) {
        NoticeModel notice = noticeList.get(position);
        boolean isCheckedBad = notice.isCheckedBad();
        int color;

        if (isCheckedBad) {                                // 싫어요
            color = context.getResources().getColor(R.color.main);
            holder.badBtn.setImageResource(R.drawable.bad_icon_main_color);
        } else {                                            // 싫어요 취소
            color = context.getResources().getColor(R.color.gray);
            holder.badBtn.setImageResource(R.drawable.bad_icon);
        }
        holder.bad.setTextColor(color);
    }


    private void setComment(NoticeModel notice) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_SHOW_COMMENT);
        intent.putExtra(Global.KEY_NOTICE, notice);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NoticeModel notice = noticeList.get(position);
        holder.imageContainer.removeAllViews();
        if (notice.getProfileImage() == null) {
            holder.profileImage.setImageResource(R.drawable.base_profile);
        } else {
            Bitmap profileImage = Global.decodeImage(notice.getProfileImage());
            holder.profileImage.setImageBitmap(profileImage);
        }

        final long time = getTime(notice.getDate(), notice.getGoodNum(), notice.getBadNum());
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

        final TimeTask task = new TimeTask(holder.time);                                 // 시간 초 스레드 생성
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, time);

        setGood(holder, position);
        holder.goodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noticeList.get(position).isCheckedBad()) {
                    noticeList.get(position).setIsCheckedGood(!noticeList.get(position).isCheckedGood());                // 반대로 바꿈
                    setGood(holder, position);
                    goodCheck(noticeList.get(position).isCheckedGood(), notice.getNoticeId(), position, holder, task, time);
                }
            }
        });

        setBad(holder, position);
        holder.badLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!noticeList.get(position).isCheckedGood()) {
                    noticeList.get(position).setIsCheckedBad(!noticeList.get(position).isCheckedBad());
                    setBad(holder, position);
                    badCheck(noticeList.get(position).isCheckedBad(), notice.getNoticeId(), position, holder, task, time);
                }
            }
        });

        holder.commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setComment(noticeList.get(position));
            }
        });

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


    public class TimeTask extends AsyncTask<Long, String, String> {

        private TextView textView;


        public TimeTask(TextView textView) {
            this.textView = textView;
        }


        @Override
        protected String doInBackground(Long... params) {
            long time = params[0];

            try {
                while (time > 0 && !isCancelled()) {
                    publishProgress(getTime(time));
                    time -= 1000;
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
            }

            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            textView.setText(values[0]);
        }
    }


    private long getTime(Date date, int goodNum, int badNum) {
        final int GOOD = 1000 * 60 * 3;                     // 3분 더하기
        final int BAD = - 1000 * 60 * 3;                     // 3분 빼기

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);


        Calendar curCalendar = Calendar.getInstance();
        curCalendar.setTimeInMillis(System.currentTimeMillis());

        return (calendar.getTimeInMillis() - System.currentTimeMillis()) + (goodNum * GOOD) + (badNum * BAD);
    }


    /**
     * TODO: 좋아요 버튼 클릭
     * @param flag : 좋아요, 좋아요 취소
     * */
    private void goodCheck(boolean flag, String noticeId, int position, ViewHolder holder, TimeTask task, long time) {
        if (flag) {                             // 좋아요
            int curGoodNum = noticeList.get(position).getGoodNum();
            noticeList.get(position).setGoodNum(curGoodNum + 1);
            holder.goodNum.setText(String.valueOf(curGoodNum + 1));
            time = time + MIN3;
        } else {                                // 좋아요 취소
            int curGoodNum = noticeList.get(position).getGoodNum();
            noticeList.get(position).setGoodNum(curGoodNum - 1);
            holder.goodNum.setText(String.valueOf(curGoodNum - 1));
        }
        Intent intent = new Intent(context, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_GOOD);
        intent.putExtra(Global.KEY_FLAG, flag);
        intent.putExtra(Global.KEY_USER_ID, id);
        intent.putExtra(Global.KEY_POSITION, position);
        intent.putExtra(Global.KEY_NOTICE_ID, noticeId);
        context.startService(intent);
    }


    /**
     * TODO: 싫어요 버튼 클릭
     * @param flag : 싫어요, 싫어요 취소
     * */
    private void badCheck(boolean flag, String noticeId, int position, ViewHolder holder, TimeTask task, long time) {
        if (flag) {                             // 싫어요
            int curBadNum = noticeList.get(position).getBadNum();
            noticeList.get(position).setBadNum(curBadNum + 1);
            holder.badNum.setText(String.valueOf(curBadNum + 1));
            time = time - MIN3;
        } else {                                // 싫어요 취소
            int curBadNum = noticeList.get(position).getBadNum();
            noticeList.get(position).setBadNum(curBadNum - 1);
            holder.badNum.setText(String.valueOf(curBadNum - 1));
        }

        Intent intent = new Intent(context, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_BAD);
        intent.putExtra(Global.KEY_FLAG, flag);
        intent.putExtra(Global.KEY_USER_ID, id);
        intent.putExtra(Global.KEY_NOTICE_ID, noticeId);
        intent.putExtra(Global.KEY_POSITION, position);
        context.startService(intent);
    }


    public void failGood(boolean flag, int position) {
        noticeList.get(position).setIsCheckedGood(!flag);
        noticeList.get(position).setGoodNum(noticeList.get(position).getGoodNum() - 1);
        notifyDataSetChanged();
    }


    public void failBad(boolean flag, int position) {
        noticeList.get(position).setIsCheckedBad(!flag);
        noticeList.get(position).setBadNum(noticeList.get(position).getBadNum() - 1);
        notifyDataSetChanged();
    }


    public void addItem(NoticeModel notice) {
        noticeList.add(notice);
        notifyDataSetChanged();
    }


    public void addItem(ArrayList<NoticeModel> noticeList) {
        this.noticeList.addAll(noticeList);
        notifyDataSetChanged();
    }


    public void setItem(ArrayList<NoticeModel> noticeList) {
        notifyDataSetChanged();
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


    public void readNotice(String keyword, int count) {
        Intent intent = new Intent(context, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_READ_NOTICE);
        intent.putExtra(Global.KEY_COUNT, count);
        intent.putExtra(Global.KEY_USER_ID, id);
        intent.putExtra(Global.KEY_KEY_WORD, keyword);
        context.startService(intent);
    }


    public void addComment(String comment, String noticeId) {
        notifyDataSetChanged();
        Log.d(TAG, "noticeId = " + noticeId);
        for (int i = 0; i < noticeList.size(); i++) {
            if (noticeList.get(i).getNoticeId().equals(noticeId)) {
                noticeList.get(i).getCommentList().add(new CommentModel(id, name, image, comment));
                noticeList.get(i).setCommentNum(noticeList.get(i).getCommentNum() + 1);
                break;
            }
        }
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
        ImageView goodBtn;
        ImageView badBtn;
        ImageView commentBtn;

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
            goodBtn = (ImageView) itemView.findViewById(R.id.card_notice_good_btn);
            badBtn = (ImageView) itemView.findViewById(R.id.card_notice_bad_btn);
            commentBtn = (ImageView) itemView.findViewById(R.id.card_notice_comment_btn);
            imageContainer = (LinearLayout) itemView.findViewById(R.id.card_notice_image_container);
        }
    }
}
