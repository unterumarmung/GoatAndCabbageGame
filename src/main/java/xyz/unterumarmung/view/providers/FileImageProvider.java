package xyz.unterumarmung.view.providers;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileImageProvider implements ImageProvider {
    private static final String FILENAME_PREFIX = "resources/";
    private final @NotNull String filename;
    private BufferedImage image;

    public FileImageProvider(@NotNull final String filename) {
        this.filename = filename;
    }

    @Override
    public BufferedImage image() {
        if (image != null)
            return image;

        try {
            image = ImageIO.read(new File(FILENAME_PREFIX + filename));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return image;
    }
}
