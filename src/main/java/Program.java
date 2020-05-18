import events.MessageBridge;
import model.*;
import view.GamePanel;
import view.WidgetFactory;
import view.providers.FileImageProvider;
import view.providers.ImageProvider;
import view.widgets.FieldWidget;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;


public class Program {

    public static void main(String[] args) {
        var messageBridge = new MessageBridge();
        var fieldFactory = new SimpleFieldFactory(messageBridge);
        var widgetFactory = new WidgetFactory(imageProviders());

        var game = new Game(fieldFactory, messageBridge, messageBridge);
        game.start();
        var fieldWidget = new FieldWidget(game.gameField(), widgetFactory, messageBridge);
        SwingUtilities.invokeLater(() -> new GamePanel(game, fieldWidget, messageBridge));
    }

    private static Map<Class, ImageProvider> imageProviders() {
        var map = new HashMap<Class, ImageProvider>();
        map.put(Goat.class, new FileImageProvider("goat.png"));
        map.put(Wall.class, new FileImageProvider("wall.png"));
        map.put(Cabbage.class, new FileImageProvider("cabbage.png"));
        map.put(Cell.class, new FileImageProvider("ground.png"));
        return map;
    }
}
