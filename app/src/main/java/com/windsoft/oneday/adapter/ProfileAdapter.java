package com.windsoft.oneday.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
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
import com.windsoft.oneday.SetNameDialog;
import com.windsoft.oneday.activity.MainActivity;
import com.windsoft.oneday.model.NoticeModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ironFactory on 2015-08-05.
 */
public class ProfileAdapter extends RecyclerView.Adapter implements SetNameDialog.OnSetNameHandler {

    private static final String TAG = "ProfileAdapter";
    private static final int HEADER = 0;
    private static final int BODY = 1;
    private final int MIN3 = 1000 * 60 * 3;


    private Context context;
    private ArrayList<NoticeModel> noticeList;
    private String image;
    private String name;
    private String id;

    public ProfileAdapter(Context context, ArrayList<NoticeModel> noticeList, String image, String name, String id) {
        this.noticeList = noticeList;
        this.context = context;
        this.image = image;
        this.name = name;
        this.id = id;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        return BODY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == BODY) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notice, parent, false);
            return new ViewHolder(view);
        }
        return null;
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


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == HEADER) {                           // 헤더
            HeaderViewHolder viewHolder = ((HeaderViewHolder) holder);
            if (image == null || image.equals("null")) {
                viewHolder.image.setImageResource(R.drawable.base_profile);
            } else {
                viewHolder.image.setImageBitmap(Global.decodeImage(image));
            }
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setProfileImage();
                }
            });

            viewHolder.name.setText(name);
            viewHolder.updateName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateName();
                }
            });
        } else {                                            // 바디
            final ViewHolder viewHolder = ((ViewHolder) holder);
            final int POSITION = position - 1;
            final NoticeModel notice = noticeList.get(POSITION);
            viewHolder.imageContainer.removeAllViews();
            if (notice.getProfileImage() == null) {
                viewHolder.profileImage.setImageResource(R.drawable.base_profile);
            } else {
                Bitmap profileImage = Global.decodeImage(notice.getProfileImage());
                viewHolder.profileImage.setImageBitmap(profileImage);
            }

            final long time = getTime(notice.getDate(), notice.getGoodNum(), notice.getBadNum());
            viewHolder.name.setText(notice.getName());
            viewHolder.time.setText(getTime(time));
            viewHolder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Custom Alert Dialog
                }
            });
            viewHolder.content.setText(notice.getContent());
            viewHolder.goodNum.setText(String.valueOf(notice.getGoodNum()));
            viewHolder.badNum.setText(String.valueOf(notice.getBadNum()));
            viewHolder.commentNum.setText(String.valueOf(notice.getCommentNum()));

            final TimeTask task = new TimeTask(viewHolder.time);                                 // 시간 초 스레드 생성
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, time);

            setGood(viewHolder, POSITION);
            viewHolder.goodLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!noticeList.get(POSITION).isCheckedBad()) {
                        noticeList.get(POSITION).setIsCheckedGood(!noticeList.get(POSITION).isCheckedGood());                // 반대로 바꿈
                        setGood(viewHolder, POSITION);
                        goodCheck(noticeList.get(POSITION).isCheckedGood(), notice.getNoticeId(), POSITION, viewHolder, task, time);
                    }
                }
            });

            setBad(viewHolder, POSITION);
            viewHolder.badLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!noticeList.get(POSITION).isCheckedGood()) {
                        noticeList.get(POSITION).setIsCheckedBad(!noticeList.get(POSITION).isCheckedBad());
                        setBad(viewHolder, POSITION);
                        badCheck(noticeList.get(POSITION).isCheckedBad(), notice.getNoticeId(), POSITION, viewHolder, task, time);
                    }
                }
            });

            viewHolder.commentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setComment(noticeList.get(POSITION));
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
                viewHolder.imageContainer.addView(imageView);
            }
        }
    }


    public void setImage(String image) {
        notifyDataSetChanged();
        this.image = image;
    }


    private void setProfileImage() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_GET_PHOTO);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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



    private void updateName() {
        SetNameDialog dialog = new SetNameDialog(context);
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return noticeList.size() + 1;
    }


    public void setData(ArrayList<NoticeModel> noticeList, String image, String name) {
        this.noticeList = noticeList;
        this.image = image;
        this.name = name;
        notifyDataSetChanged();
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageButton image;
        ImageButton updateName;
        TextView name;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            image = (ImageButton) itemView.findViewById(R.id.item_profile_image);
            updateName = (ImageButton) itemView.findViewById(R.id.item_profile_update_name);
            name = (TextView) itemView.findViewById(R.id.item_profile_name);
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


    @Override
    public void onSetName(String name) {
        Intent intent = new Intent(context, OneDayService.class);
        intent.putExtra(Global.KEY_COMMAND, Global.KEY_SET_NAME);
        intent.putExtra(Global.KEY_USER_NAME, name);
        intent.putExtra(Global.KEY_USER_ID, id);
        context.startService(intent);
    }
}
