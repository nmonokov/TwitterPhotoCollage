package com.twitapp.utils;

import com.twitapp.dict.ImageSize;
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

    public static boolean check(ImageSize imageSize, Integer cellsWidth, Integer cellsHeight, int[][] sheet, Integer h, Integer w){
        if (imageSize.isSmall()) {
            return sheet[h][w] == 0;
        }
        int cellSize = imageSize.ordinal();
        if ((cellsWidth - 1) - w >= cellSize && (cellsHeight - 1) - h >= cellSize) {
            for (int i = h; i <= h + cellSize; i++) {
                for (int k = w; k <= w + cellSize; k++) {
                    if (sheet[i][k] != 0) return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public static void create(ImageSize imageSize, int[][] sheet, Integer h, Integer w, List<TwitterUser> users, Integer index,
                              Integer offsetWidth, Integer offsetHeight, Graphics2D g2) throws IOException {
        URL url;
        Integer pxSize = imageSize.getSize();
        if (imageSize.isSmall()) {
            url = new URL(users.get(index).getImageURL());
            sheet[h][w] = 1;
        } else {
            url = new URL(users.get(index).getBigImageUrl());
            int cellSize = imageSize.ordinal();
            for (int i = h; i <= h + cellSize; i++) {
                for (int k = w; k <= w + cellSize; k++) {
                    sheet[i][k] = 1;
                }
            }
        }
        BufferedImage img;
        try {
            img = Scalr.resize(ImageIO.read(url), Scalr.Mode.FIT_TO_WIDTH, pxSize, 0);
        } catch (IIOException | IllegalArgumentException e) {
            img = new BufferedImage(pxSize, pxSize, BufferedImage.TYPE_INT_ARGB);
        }
        g2.drawImage(img, null, offsetWidth, offsetHeight);
    }
}
