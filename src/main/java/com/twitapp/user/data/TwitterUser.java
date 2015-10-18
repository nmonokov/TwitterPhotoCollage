package com.twitapp.user.data;

import com.twitapp.dict.ImageSize;

public class TwitterUser {
    private String imageURL;
    private ImageSize imageSize;
    private int tweets;

    public TwitterUser(String imageURL, int tweets) {
        this.imageURL = imageURL;
        this.tweets = tweets;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
