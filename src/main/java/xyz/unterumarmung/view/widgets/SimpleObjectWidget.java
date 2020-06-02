package xyz.unterumarmung.view.widgets;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.view.providers.ImageProvider;
import xyz.unterumarmung.view.utils.ImageUtils;

import java.awt.*;

public class SimpleObjectWidget extends GameObjectWidget {
    private static final int CELL_DIMENSION_DELTA = 0;
    private final @NotNull ImageProvider imageProvider;

    public SimpleObjectWidget(@NotNull ImageProvider imageProvider) {
        super();
        this.imageProvider = imageProvider;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        var resizeDimension = new Dimension(CellWidget.CELL_DIMENSION.width - CELL_DIMENSION_DELTA, CellWidget.CELL_DIMENSION.height - CELL_DIMENSION_DELTA);
        var resizedImage = ImageUtils.resizeImage(imageProvider.image(), resizeDimension);
        g.drawImage(resizedImage, CELL_DIMENSION_DELTA / 2, CELL_DIMENSION_DELTA / 2, null);
    }
}
