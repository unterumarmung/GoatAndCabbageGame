package view.widgets;

import org.jetbrains.annotations.NotNull;
import view.providers.ImageProvider;
import view.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CellWidget extends JPanel {
    static final Dimension CELL_DIMENSION = new Dimension(120, 120);
    private final List<GameObjectWidget> objects = new ArrayList<>(2);
    private final @NotNull ImageProvider backgroundProvider;

    public CellWidget(@NotNull ImageProvider backgroundProvider) {
        super();
        this.backgroundProvider = backgroundProvider;
        setPreferredSize(CELL_DIMENSION);
    }

    public void addObject(@NotNull GameObjectWidget gameObjectWidget) {
        objects.add(gameObjectWidget);
        repaint();
    }

    public void removeObject(@NotNull GameObjectWidget gameObjectWidget) {
        objects.remove(gameObjectWidget);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ImageUtils.resizeImage(backgroundProvider.image(), CELL_DIMENSION), 0, 0, null);
        for (var object : objects) {
            object.paintComponent(g);
        }
    }
}
