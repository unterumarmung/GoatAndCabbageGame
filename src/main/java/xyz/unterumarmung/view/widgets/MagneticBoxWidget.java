package xyz.unterumarmung.view.widgets;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.objects.MagneticBox.Alignment;
import xyz.unterumarmung.view.providers.ImageProvider;

import java.awt.image.BufferedImage;

import static xyz.unterumarmung.model.objects.MagneticBox.Alignment.VERTICAL_NORTH_HORIZONTAL_SOUTH;
import static xyz.unterumarmung.view.utils.ImageUtils.rotateImageClockwise90;

public class MagneticBoxWidget extends SimpleObjectWidget {
    private static final Alignment DEFAULT_ALIGNMENT = VERTICAL_NORTH_HORIZONTAL_SOUTH;
    private final @NotNull BufferedImage image;

    public MagneticBoxWidget(@NotNull ImageProvider imageProvider, @NotNull Alignment alignment) {
        super(imageProvider);
        if (alignment != DEFAULT_ALIGNMENT) {
            image = rotateImageClockwise90(imageProvider.image());
        } else {
            image = imageProvider.image();
        }
    }

    @Override
    protected @NotNull BufferedImage image() {
        return image;
    }
}
