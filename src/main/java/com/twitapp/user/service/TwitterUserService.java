package com.twitapp.user.service;

import com.twitapp.user.data.TwitterUser;
import twitter4j.TwitterException;

import java.io.IOException;
import java.util.List;

public interface TwitterUserService {
    /**
     * @param login Setted login of user. Pass is not required.
     * @param width quantity of cells for images in width
     * @param height quantity of cells for images in height
     * @param diffSize if true then get full list of friends, original images and sets ImageSize to each TwitterUser.
     *                 Otherwise, get limited list of friends, bigger images and doesn't set ImageSize.
     * @return List of users dto.
     * @throws twitter4j.TwitterException in case of incorrect login and in case of exceeding rate limit.
     */
    List<TwitterUser> getListOfUsersByLogin(String login, int width, int height, boolean diffSize) throws TwitterException;

    /**
     * Make collage by quantity of cells. And write an image to a tomcat directory ../webapps/project-data/images/collage.png
     * @param users List of friends of person whose login we entered
     * @param width quantity of cells for images in width
     * @param height quantity of cells for images in height
     * @param diffSize if true then generates a collage of images with different size(depends on ImageSize variable).
     *                 Otherwise, generates a collage with similar sized images.
     * @throws java.io.IOException
     */
    void generateCollage(List<TwitterUser> users, int width, int height, boolean diffSize) throws IOException;
}
