package xyz.unterumarmung.view.utils;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class ImageUtils {
    public static @NotNull BufferedImage resizeImage(@NotNull BufferedImage image, @NotNull Dimension dimension) {
        var tempImage = image.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
        var resizedImage = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);

        var resizedImageGraphics = resizedImage.createGraphics();
        resizedImageGraphics.drawImage(tempImage, 0, 0, null);
        resizedImageGraphics.dispose();

        return resizedImage;
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public static @NotNull BufferedImage rotateImageByDegrees(@NotNull BufferedImage image, int angle) {
        final double radians = toRadians(angle);
        final double sin = abs(sin(radians));
        final double cos = abs(cos(radians));
        final int imageWidth = image.getWidth();
        final int imageHeight = image.getHeight();
        final int newImageWidth = (int) floor(imageWidth * cos + imageHeight * sin);
        final int newImageHeight = (int) floor(imageHeight * cos + imageWidth * sin);

        final var rotatedImage = new BufferedImage(newImageWidth, newImageHeight, BufferedImage.TYPE_INT_ARGB);
        final var rotatedImageGraphics = rotatedImage.createGraphics();
        final var affineTransform = new AffineTransform();
        affineTransform.translate((newImageWidth - imageWidth) / 2, (newImageHeight - imageHeight) / 2);

        int x = imageWidth / 2;
        int y = imageHeight / 2;

        affineTransform.rotate(radians, x, y);
        rotatedImageGraphics.setTransform(affineTransform);
        rotatedImageGraphics.drawImage(image, 0, 0, null);
        rotatedImageGraphics.setColor(Color.RED);
        rotatedImageGraphics.drawRect(0, 0, newImageWidth - 1, newImageHeight - 1);
        rotatedImageGraphics.dispose();

        return rotatedImage;
    }

    public static BufferedImage rotateImageClockwise90(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();

        BufferedImage dest = new BufferedImage(width, height, src.getType());

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
        graphics2D.drawRenderedImage(src, null);

        return dest;
    }
}
