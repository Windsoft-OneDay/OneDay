package com.windsoft.oneday.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by ironFactory on 2015-08-04.
 */
public class NoticeModel {

    private Bitmap profileImage;
    private String name;
    private long time;
    private String content;
    private int goodNum;
    private int badNum;
    private int commentNum;
    private boolean isCheckedGood;
    private boolean isCheckedBad;
    private boolean isCommented;

    private ArrayList<String> commentName;
    private ArrayList<Bitmap> commentProfileImage;
    private ArrayList<String> commentContent;

    public NoticeModel(Bitmap profileImage, String name, long time, String content, int goodNum, int badNum, int commentNum, ArrayList<String> commentName, ArrayList<Bitmap> commentProfileImage, ArrayList<String> commentContent, boolean isCheckedGood, boolean isCheckedBad, boolean isCommented) {
        this.profileImage = profileImage;
        this.name = name;
        this.time = time;
        this.content = content;
        this.goodNum = goodNum;
        this.badNum = badNum;
        this.commentNum = commentNum;
        this.commentName = commentName;
        this.commentProfileImage = commentProfileImage;
        this.commentContent = commentContent;
        this.isCheckedGood = isCheckedGood;
        this.isCheckedBad = isCheckedBad;
        this.isCommented = isCommented;
    }

    public boolean isCheckedGood() {
        return isCheckedGood;
    }

    public void setIsCheckedGood(boolean isCheckedGood) {
        this.isCheckedGood = isCheckedGood;
    }

    public boolean isCheckedBad() {
        return isCheckedBad;
    }

    public void setIsCheckedBad(boolean isCheckedBad) {
        this.isCheckedBad = isCheckedBad;
    }

    public boolean isCommented() {
        return isCommented;
    }

    public void setIsCommented(boolean isCommented) {
        this.isCommented = isCommented;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public int getBadNum() {
        return badNum;
    }

    public void setBadNum(int badNum) {
        this.badNum = badNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public ArrayList<String> getCommentName() {
        return commentName;
    }

    public void setCommentName(ArrayList<String> commentName) {
        this.commentName = commentName;
    }

    public ArrayList<Bitmap> getCommentProfileImage() {
        return commentProfileImage;
    }

    public void setCommentProfileImage(ArrayList<Bitmap> commentProfileImage) {
        this.commentProfileImage = commentProfileImage;
    }

    public ArrayList<String> getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(ArrayList<String> commentContent) {
        this.commentContent = commentContent;
    }
}
