package xyz.unterumarmung.view.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {
    public static BufferedImage resizeImage(BufferedImage image, Dimension dimension) {
        var tempImage = image.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
        var resizedImage = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);

        var resizedImageGraphics = resizedImage.createGraphics();
        resizedImageGraphics.drawImage(tempImage, 0, 0, null);
        resizedImageGraphics.dispose();

        return resizedImage;
    }
}
