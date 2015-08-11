package com.windsoft.oneday.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ironFactory on 2015-08-04.
 */
public class NoticeModel implements Serializable {

    private String profileImage;
    private String id;
    private String name;
    private Date date;
    private String content;
    private int goodNum;
    private int badNum;
    private int commentNum;
    private boolean isCheckedGood;
    private boolean isCheckedBad;

    private ArrayList<CommentModel> commentList;
    private ArrayList<String> imageList;


    public NoticeModel(String id, String profileImage, String name, Date date, String content, int goodNum, int badNum, int commentNum, boolean isCheckedGood, boolean isCheckedBad, ArrayList<CommentModel> commentList, ArrayList<String> imageList) {
        this.id = id;
        this.profileImage = profileImage;
        this.name = name;
        this.date = date;
        this.content = content;
        this.goodNum = goodNum;
        this.badNum = badNum;
        this.commentNum = commentNum;
        this.isCheckedGood = isCheckedGood;
        this.isCheckedBad = isCheckedBad;
        this.commentList = commentList;
        this.imageList = imageList;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public ArrayList<CommentModel> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<CommentModel> commentList) {
        this.commentList = commentList;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }
}
