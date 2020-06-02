package xyz.unterumarmung.view.widgets;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.objects.MagneticBox.Alignment;
import static xyz.unterumarmung.model.objects.MagneticBox.Alignment.*;
import xyz.unterumarmung.view.providers.ImageProvider;
import xyz.unterumarmung.view.utils.ImageUtils;

import java.awt.image.BufferedImage;

public class MagneticBoxWidget extends SimpleObjectWidget {
    private static final Alignment DEFAULT_ALIGNMENT = VERTICAL_NORTH_HORIZONTAL_SOUTH;
    private final @NotNull BufferedImage image;

    public MagneticBoxWidget(@NotNull ImageProvider imageProvider, @NotNull Alignment alignment) {
        super(imageProvider);
        if (alignment != DEFAULT_ALIGNMENT) {
            image = ImageUtils.rotateImageByDegrees(imageProvider.image(), 90);
        } else {
            image = imageProvider.image();
        }
    }

    @Override
    protected @NotNull BufferedImage image() {
        return image;
    }
}
