package com.twitapp.user.data;

import com.twitapp.dict.ImageSize;

public class TwitterUser {

    private String imageURL;
    private String bigImageUrl;
    private ImageSize imageSize;
    private int tweets;

    public TwitterUser(String imageURL, String bigImageUrl, int tweets) {
        this.imageURL = imageURL;
        this.bigImageUrl = bigImageUrl;
        this.tweets = tweets;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getBigImageUrl() {
        return bigImageUrl;
    }

    public void setBigImageUrl(String bigImageUrl) {
        this.bigImageUrl = bigImageUrl;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }

    public int getTweets() {
        return tweets;
    }

    public void setTweets(int tweets) {
        this.tweets = tweets;
    }
}
