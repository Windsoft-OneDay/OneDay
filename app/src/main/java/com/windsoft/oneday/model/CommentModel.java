package com.windsoft.oneday.model;

import java.io.Serializable;

/**
 * Created by ironFactory on 2015-08-10.
 */
public class CommentModel implements Serializable {

    private String id;
    private String name;
    private String image;
    private String comment;

    public CommentModel(String id, String name, String image, String comment) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.comment = comment;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
