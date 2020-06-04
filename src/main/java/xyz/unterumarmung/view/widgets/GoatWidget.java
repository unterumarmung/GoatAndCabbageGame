package xyz.unterumarmung.view.widgets;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.objects.Goat;
import xyz.unterumarmung.view.providers.ImageProvider;

import java.awt.*;

import static xyz.unterumarmung.view.utils.ImageUtils.resizeImage;

public class GoatWidget extends GameObjectWidget {
    private final @NotNull Goat goat;
    private final @NotNull ImageProvider imageProvider;

    public GoatWidget(@NotNull Goat goat, @NotNull ImageProvider imageProvider) {
        this.goat = goat;
        this.imageProvider = imageProvider;
//        addKeyListener(new KeyController());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var delta = CellWidget.CELL_DIMENSION.width / 5;
        var middle = CellWidget.CELL_DIMENSION.width / 2;
        var resizedImage = resizeImage(imageProvider.image(), resizeDimension());

        g.setFont(new Font("Arial", Font.PLAIN, delta));
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(goat.steps()), middle, delta);
        g.drawImage(resizedImage, delta, delta, null);
    }

    @NotNull
    private Dimension resizeDimension() {
        var delta = CellWidget.CELL_DIMENSION.width / 5;
        return new Dimension(CellWidget.CELL_DIMENSION.width - delta, CellWidget.CELL_DIMENSION.height - delta);
    }

//    private class KeyController implements KeyListener {
//
//        @Override
//        public void keyTyped(KeyEvent e) {
//        }
//
//        @Override
//        public void keyPressed(KeyEvent e) {
//            System.out.println("I'm here!");
//            if (isDirectionKeyCode(e.getKeyCode())) {
//                goat.move(keyCodeToDirection(e.getKeyCode()));
//                System.out.println("Goat moves to " + keyCodeToDirection(e.getKeyCode()));
//                repaint();
//            }
//        }
//
//        @Override
//        public void keyReleased(KeyEvent e) {
//        }
//    }
}
