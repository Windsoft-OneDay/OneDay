package com.windsoft.oneday.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.windsoft.oneday.R;
import com.windsoft.oneday.model.NoticeModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ironFactory on 2015-08-05.
 */
public class ProfileAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ProfileAdapter";
    private static final int HEADER = 0;
    private static final int SUB_HEADER = 1;
    private static final int BODY = 2;


    private ArrayList<NoticeModel> noticeList;
    private Bitmap image;
    private String name;

    public ProfileAdapter(ArrayList<NoticeModel> noticeList, Bitmap image, String name) {
        this.noticeList = noticeList;
        this.image = image;
        this.name = name;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        else if (position == 1)
            return SUB_HEADER;
        return BODY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_profile, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == SUB_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_header_profile, parent, false);
            return new SubHeaderViewHolder(view);
        } else if (viewType == BODY) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_profile, parent, false);
            return new MainAdapter.ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == HEADER) {                           // 헤더
            HeaderViewHolder viewHolder = ((HeaderViewHolder) holder);
            viewHolder.image.setImageBitmap(image);
            viewHolder.name.setText(name);
        } else if (position == SUB_HEADER) {                // 서브 헤더

        } else {                                            // 바디
            final int POSITION = position + 2;
            final NoticeModel notice = noticeList.get(POSITION);
            final MainAdapter.ViewHolder viewHolder = ((MainAdapter.ViewHolder) holder);
            long time = System.currentTimeMillis() - notice.getDate().getTime();
            viewHolder.profileImage.setImageBitmap(MainAdapter.decodeImage(notice.getProfileImage()));
            viewHolder.name.setText(notice.getName());
            viewHolder.time.setText(getTime(time));
            viewHolder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Custom Alert Dialog
                }
            });
            viewHolder.content.setText(notice.getContent());
            viewHolder.goodNum.setText(notice.getGoodNum());
            viewHolder.badNum.setText(notice.getBadNum());
            viewHolder.commentNum.setText(notice.getCommentNum());

            setGoodColor(viewHolder, POSITION);
            viewHolder.goodLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setGoodColor(viewHolder, POSITION);
                }
            });

            setBadColor(viewHolder, POSITION);
            viewHolder.badLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setBadColor(viewHolder, POSITION);
                }
            });
        }
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


    private void setGoodColor(MainAdapter.ViewHolder holder, int position) {
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


    private void setBadColor(MainAdapter.ViewHolder holder, int position) {
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
    public int getItemCount() {
        return noticeList.size() + 2;
    }


    public void setData(ArrayList<NoticeModel> noticeList, Bitmap image, String name) {
        this.noticeList = noticeList;
        this.image = image;
        this.name = name;
        notifyDataSetChanged();
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.header_profile_image);
            name = (TextView) itemView.findViewById(R.id.header_profile_name);
        }
    }


    public class SubHeaderViewHolder extends RecyclerView.ViewHolder {

        public SubHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
