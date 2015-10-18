package com.twitapp.utils;

import com.twitapp.user.data.TwitterUser;
import org.imgscalr.Scalr;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DrawImageUtils {

    public static boolean checkSmall(int[][] sheet, Integer h, Integer w){
        return sheet[h][w] == 0;
    }

    public static void createSmall(int[][] sheet, Integer h, Integer w, List<TwitterUser> users, Integer index,
                                   Integer pxSize, Integer offsetWidth, Integer offsetHeight, Graphics2D g2) throws IOException {
        sheet[h][w] = 1;
        BufferedImage img;
        try {
            img = Scalr.resize(ImageIO.read(new URL(users.get(index).getImageURL())), Scalr.Mode.FIT_TO_WIDTH, pxSize, 0);
        } catch (IIOException e) {
            img = new BufferedImage(pxSize, pxSize, BufferedImage.TYPE_INT_ARGB);
        }
        g2.drawImage(img, null, offsetWidth, offsetHeight);
    }

    public static boolean checkMedium(Integer cellsWidth, Integer cellsHeight, int[][] sheet, Integer h, Integer w){
        return (cellsWidth - 1) - w >= 1 && (cellsHeight - 1) - h >= 1 && sheet[h][w] == 0 && sheet[h][w+1] == 0 && sheet[h+1][w] == 0 && sheet[h+1][w+1] == 0;
    }

    public static void createMedium(int[][] sheet, Integer h, Integer w, List<TwitterUser> users, Integer index,
                                    Integer pxSize, Integer offsetWidth, Integer offsetHeight, Graphics2D g2) throws IOException {
        sheet[h][w] = 1;
        sheet[h][w+1] = 1;
        sheet[h+1][w] = 1;
        sheet[h+1][w+1] = 1;
        BufferedImage img;
        try {
            img = Scalr.resize(ImageIO.read(new URL(users.get(index).getImageURL())), Scalr.Mode.FIT_TO_WIDTH, pxSize, 0);
        } catch (IIOException e) {
            img = new BufferedImage(pxSize, pxSize, BufferedImage.TYPE_INT_ARGB);
        }
        g2.drawImage(img, null, offsetWidth, offsetHeight);
    }

    public static boolean checkBig(Integer cellsWidth, Integer cellsHeight, int[][] sheet, Integer h, Integer w){
        return (cellsWidth - 1) - w >= 2 && (cellsHeight - 1) - h >= 2 && sheet[h][w] == 0 && sheet[h][w+1] == 0 && sheet[h][w+2] == 0
                && sheet[h+1][w] == 0 && sheet[h+2][w] == 0
                && sheet[h+1][w+1] == 0 && sheet[h+1][w+2] == 0
                && sheet[h+2][w+1] == 0 && sheet[h+2][w+2] == 0;
    }

    public static void createBig(int[][] sheet, Integer h, Integer w, List<TwitterUser> users, Integer index,
                                 Integer pxSize, Integer offsetWidth, Integer offsetHeight, Graphics2D g2) throws IOException {
        sheet[h][w] = 1;
        sheet[h][w+1] = 1;
        sheet[h][w+2] = 1;
        sheet[h+1][w] = 1;
        sheet[h+2][w] = 1;
        sheet[h+1][w+1] = 1;
        sheet[h+1][w+2] = 1;
        sheet[h+2][w+1] = 1;
        sheet[h+2][w+2] = 1;

        BufferedImage img;
        try {
            img = Scalr.resize(ImageIO.read(new URL(users.get(index).getImageURL())), Scalr.Mode.FIT_TO_WIDTH, pxSize, 0);
        } catch (IIOException e) {
            img = new BufferedImage(pxSize, pxSize, BufferedImage.TYPE_INT_ARGB);
        }
        g2.drawImage(img, null, offsetWidth, offsetHeight);
    }
}
