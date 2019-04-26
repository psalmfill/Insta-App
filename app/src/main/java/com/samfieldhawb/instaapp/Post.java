package com.samfieldhawb.instaapp;

public class Post {
    private String mTitle;
    private String mDesc;
    private String mImage;

    public Post(String title, String desc, String image) {
        mTitle = title;
        mDesc = desc;
        mImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }
}
