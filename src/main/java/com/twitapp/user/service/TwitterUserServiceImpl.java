package com.twitapp.user.service;

import com.twitapp.dict.ImageSize;
import com.twitapp.user.data.TwitterUser;
import com.twitapp.utils.DrawImageUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;


@Service
public class TwitterUserServiceImpl implements TwitterUserService {

    /** Max quantity of an array of ids. */
    private static final int IDS_PER_REQUEST = 100;

    /** Possible width and height of images. SMALL_IMAGE_SIZE used in generating like a step for image. */
    private static final int SMALL_IMAGE_SIZE = 73;
    private static final int MEDIUM_IMAGE_SIZE = 146;
    private static final int BIG_IMAGE_SIZE = 219;

    @Override
    public List<TwitterUser> getListOfUsersByLogin(String login, int width, int height, boolean diffSize) throws TwitterException {
        List<TwitterUser> listOfUsers = new ArrayList<>();
        Twitter twitter = getTwitter();
        User user = twitter.showUser(login);
        if(user != null) {
            IDs ids = twitter.getFriendsIDs(user.getId(), -1);
            ResponseList<User> friends = getUsers(ids, twitter, height);
            int friendsListSize;
            if(diffSize){
                friendsListSize = friends.size();
            } else {
                int size = width * height;
                friendsListSize = size < friends.size() && friends.size() != 0 ? size : friends.size();
            }
            for (int i = 0; i < friendsListSize; i++) {
                TwitterUser tu = new TwitterUser(diffSize ? friends.get(i).getOriginalProfileImageURL() : friends.get(i).getBiggerProfileImageURL(),
                        friends.get(i).getStatusesCount());
                listOfUsers.add(tu);
            }
            if(friends.size() > 1 && diffSize) {
                fillUsersImageSize(listOfUsers);
            }
            Collections.shuffle(listOfUsers);
        }
        return listOfUsers;
    }

    /** Authentication of twitter application. Using properties from resources. */
    private Twitter getTwitter(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        try {
            Resource resource = new ClassPathResource("../spring.properties");
            Properties prop = PropertiesLoaderUtils.loadProperties(resource);
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(prop.getProperty("consumerKey"))
                    .setOAuthConsumerSecret(prop.getProperty("consumerSecret"))
                    .setOAuthAccessToken(prop.getProperty("accessToken"))
                    .setOAuthAccessTokenSecret(prop.getProperty("accessTokenSecret"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    /**
     * getUsers() method resolve a problem with limitation of Twitter API, chunking an array of ids into smaller pieces.
     * Each sub array consists of 100 ids. And then array after array we get a full list of friends.
     * @param ids list of user friend's ids
     * @return ResponseList with all friends of the user
     */
    private ResponseList<User> getUsers(IDs ids, Twitter twitter, int height) throws TwitterException {
        ResponseList<User> friends = null;
        long[] arrayIds = ids.getIDs();
        int arrayRows = arrayIds.length / IDS_PER_REQUEST;
        long[][] jaggedIds = new long[arrayRows][IDS_PER_REQUEST];

        if (arrayIds.length >= IDS_PER_REQUEST) {
            for (int row = 0, start = 0, end = IDS_PER_REQUEST; end <= arrayIds.length; row++) {
                for (int count = 0; count < IDS_PER_REQUEST; count++) {
                    int element = start + count;
                    jaggedIds[row][count] = arrayIds[element];
                }
                start += IDS_PER_REQUEST;
                end += IDS_PER_REQUEST;
            }

            arrayRows = height < arrayRows ? height : arrayRows;
            for (int row = 0; row < arrayRows; row++) {
                if (friends == null) {
                    friends = twitter.lookupUsers(jaggedIds[row]);
                } else {
                    friends.addAll(twitter.lookupUsers(jaggedIds[row]));
                }
            }
        } else {
            friends = twitter.lookupUsers(arrayIds);
        }
        return friends;
    }

    private void fillUsersImageSize(List<TwitterUser> listOfUsers){
        int maxTweets = Collections.max(listOfUsers, new Comparator<TwitterUser>() {
            @Override
            public int compare(TwitterUser o1, TwitterUser o2) {
                return o1.getTweets() - o2.getTweets();
            }
        }).getTweets();
        int averageTweets = 0;
        for(TwitterUser tu : listOfUsers){
            averageTweets += tu.getTweets();
        }
        averageTweets = averageTweets / listOfUsers.size();

        for(TwitterUser tu : listOfUsers){
            tu.setImageSize(getSize(averageTweets, maxTweets, tu.getTweets()));
        }

    }

    private ImageSize getSize(int averageTweets, int maxTweets, int thisTweets){
        float koef = averageTweets / maxTweets;
        koef = koef + 0.4f < 1.0f ? koef + 0.4f : koef;
        if(thisTweets < (averageTweets - averageTweets * koef)){
            return ImageSize.SMALL;
        } else if(thisTweets > (averageTweets) && thisTweets < (maxTweets - maxTweets * koef)){
            return ImageSize.MEDIUM;
        } else {
            return ImageSize.BIG;
        }
    }

    @Override
    public void generateCollage(List<TwitterUser> users, int width, int height, boolean diffSize) throws IOException {
        if(users.size() != 0) {
            Integer bgWidthPx = SMALL_IMAGE_SIZE * width;
            Integer bgHeightPx = SMALL_IMAGE_SIZE * height;
            Integer cellsWidth;
            Integer cellsHeight;
            // In case when we get a request for big collage, but we don't have so much elements, we should trim image to size of user's friends
            if (users.size() < width * height && !diffSize) {
                cellsWidth = width >= users.size() ? users.size() : width;
                cellsHeight = users.size() / width + 1;
            } else {
                cellsWidth = width;
                cellsHeight = height;
            }

            BufferedImage collage = new BufferedImage(bgWidthPx, bgHeightPx, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = collage.createGraphics();

            Integer index = 0;
            Integer offsetWidth = 0;
            Integer offsetHeight = 0;

            if(!diffSize){
                m: for (int h = 0; h < cellsHeight; h++) {
                    for (int w = 0; w < cellsWidth; w++) {
                        BufferedImage img;
                        try {
                            img = ImageIO.read(new URL(users.get(index++).getImageURL()));
                        } catch (IIOException e) {
                            img = new BufferedImage(SMALL_IMAGE_SIZE, SMALL_IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
                        }
                        g2.drawImage(img, null, offsetWidth, offsetHeight);
                        offsetWidth += img.getWidth();

                        if (w == cellsWidth - 1) {
                            offsetWidth = 0;
                            offsetHeight += img.getHeight();
                        }
                        if (index == users.size()) {
                            break m;
                        }
                    }
                }
            } else {
                int [][] sheet = new int[cellsHeight][cellsWidth];
                m: for(Integer h = 0; h < cellsHeight; h++){
                    for(Integer w = 0; w < cellsWidth; w++){
                        if (index == users.size() - 1) {
                            break m;
                        }
                        if(sheet[h][w] == 1) {
                            if (w == cellsWidth - 1) {
                                offsetWidth = 0;
                                offsetHeight += SMALL_IMAGE_SIZE;
                            } else {
                                offsetWidth += SMALL_IMAGE_SIZE;
                            }
                            continue;
                        }
                        // Is image BIG
                        if(ImageSize.BIG.equals(users.get(index).getImageSize())){
                            if(DrawImageUtils.checkBig(cellsWidth, cellsHeight, sheet, h, w)){
                                DrawImageUtils.createBig(sheet, h, w, users, index, BIG_IMAGE_SIZE, offsetWidth, offsetHeight, g2);
                                offsetWidth += SMALL_IMAGE_SIZE;
                            // try to fit MEDIUM size
                            } else if(DrawImageUtils.checkMedium(cellsWidth, cellsHeight, sheet, h, w)){
                                DrawImageUtils.createMedium(sheet, h, w, users, index, MEDIUM_IMAGE_SIZE, offsetWidth, offsetHeight, g2);
                                offsetWidth += SMALL_IMAGE_SIZE;
                            // try to fit SMALL size
                            } else if(DrawImageUtils.checkSmall(sheet, h, w)){
                                DrawImageUtils.createSmall(sheet, h, w, users, index, SMALL_IMAGE_SIZE, offsetWidth, offsetHeight, g2);
                                offsetWidth += SMALL_IMAGE_SIZE;

                                if (w == cellsWidth - 1) {
                                    offsetWidth = 0;
                                    offsetHeight += SMALL_IMAGE_SIZE;
                                }
                            }
                        // Is image MEDIUM
                        } else if(ImageSize.MEDIUM.equals(users.get(index).getImageSize())){
                            if(DrawImageUtils.checkMedium(cellsWidth, cellsHeight, sheet, h, w)){
                                DrawImageUtils.createMedium(sheet, h, w, users, index, MEDIUM_IMAGE_SIZE, offsetWidth, offsetHeight, g2);
                                offsetWidth += SMALL_IMAGE_SIZE;
                            // try to fit SMALL size
                            } else if(DrawImageUtils.checkSmall(sheet, h, w)){
                                DrawImageUtils.createSmall(sheet, h, w, users, index, SMALL_IMAGE_SIZE, offsetWidth, offsetHeight, g2);
                                offsetWidth += SMALL_IMAGE_SIZE;

                                if (w == cellsWidth - 1) {
                                    offsetWidth = 0;
                                    offsetHeight += SMALL_IMAGE_SIZE;
                                }
                            }
                        // Is image SMALL
                        } else if(ImageSize.SMALL.equals(users.get(index).getImageSize())){
                            if(DrawImageUtils.checkSmall(sheet, h, w)){
                                DrawImageUtils.createSmall(sheet, h, w, users, index, SMALL_IMAGE_SIZE, offsetWidth, offsetHeight, g2);
                                offsetWidth += SMALL_IMAGE_SIZE;

                                if (w == cellsWidth - 1) {
                                    offsetWidth = 0;
                                    offsetHeight += SMALL_IMAGE_SIZE;
                                }
                            } else {
                                if (w == cellsWidth - 1) {
                                    offsetWidth = 0;
                                    offsetHeight += SMALL_IMAGE_SIZE;
                                } else {
                                    offsetWidth += SMALL_IMAGE_SIZE;
                                }
                                continue;
                            }
                        }
                        index++;
                    }
                }
            }
            g2.dispose();

            new File("..\\webapps\\project-data").mkdir();
            new File("..\\webapps\\project-data\\images").mkdir();
            ImageIO.write(collage, "png", new FileImageOutputStream(new File("..\\webapps\\project-data\\images\\collage.png")));
        }
    }
}
