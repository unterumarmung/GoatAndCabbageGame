package xyz.unterumarmung;

import xyz.unterumarmung.events.MessageBridge;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.model.objects.*;
import xyz.unterumarmung.serialization.LevelLoader;
import xyz.unterumarmung.view.GamePanel;
import xyz.unterumarmung.view.LevelChooser;
import xyz.unterumarmung.view.WidgetFactory;
import xyz.unterumarmung.view.providers.FileImageProvider;
import xyz.unterumarmung.view.providers.ImageProvider;
import xyz.unterumarmung.view.widgets.FieldWidget;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;


public class Program {

    public static void main(String[] args) {
        final var messageBridge = new MessageBridge();
        final var levelLoader = new LevelLoader("levels", messageBridge, messageBridge);
        final var imageProviders = imageProviders();
        final var levelChooser = new LevelChooser(levelLoader, imageProviders.get(Goat.class));
        final var level = levelChooser.levelByUser();
        if (level == null)
            return;
        final var game = level.game();
        game.start();
        final var widgetFactory = new WidgetFactory(imageProviders);
        final var fieldWidget = new FieldWidget(game.gameField(), widgetFactory, messageBridge);
        SwingUtilities.invokeLater(() -> new GamePanel(game, fieldWidget, messageBridge));
    }

    private static Map<Class, ImageProvider> imageProviders() {
        final var map = new HashMap<Class, ImageProvider>();
        map.put(Goat.class, new FileImageProvider("goat.png"));
        map.put(Wall.class, new FileImageProvider("wall.png"));
        map.put(Cabbage.class, new FileImageProvider("cabbage.png"));
        map.put(Cell.class, new FileImageProvider("ground.png"));
        map.put(SimpleBox.class, new FileImageProvider("simple_box.png"));
        map.put(MetalBox.class, new FileImageProvider("metal_box.png"));
        map.put(MagneticBox.class, new FileImageProvider("magnetic_box.png"));
        return map;
    }
}
