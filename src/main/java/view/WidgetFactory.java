package view;

import model.*;
import org.jetbrains.annotations.NotNull;
import view.providers.ImageProvider;
import view.widgets.CellWidget;
import view.widgets.GameObjectWidget;
import view.widgets.GoatWidget;
import view.widgets.StaticObjectWidget;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WidgetFactory {
    private final @NotNull Map<GameObject, GameObjectWidget> gameObjectWidgets = new HashMap<>();
    private final @NotNull Map<Cell, CellWidget> cellWidgets = new HashMap<>();
    private final @NotNull Map<Class, ImageProvider> imageProviders;

    public WidgetFactory(@NotNull Map<Class, ImageProvider> imageProviders) {
        this.imageProviders = imageProviders;
    }

    public GameObjectWidget getWidget(@NotNull Cabbage cabbage) {
        return getGameObjectWidget(cabbage, gameObject -> new StaticObjectWidget(imageProviders.get(cabbage.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull Wall wall) {
        return getGameObjectWidget(wall, gameObject -> new StaticObjectWidget(imageProviders.get(wall.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull Goat goat) {
        return getGameObjectWidget(goat, gameObject -> new GoatWidget(goat, imageProviders.get(goat.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull GameObject gameObject) {
        return gameObjectWidgets.get(gameObject);
    }

    public CellWidget getWidget(@NotNull Cell cell) {
        cellWidgets.computeIfAbsent(cell, cell1 -> {
           var cellWidget = new CellWidget(imageProviders.get(cell.getClass()));
           for (var object : cell.objects()) {
               if (object instanceof Cabbage) {
                   cellWidget.addObject(getWidget((Cabbage) object));
               } else if (object instanceof Goat) {
                   cellWidget.addObject(getWidget((Goat) object));
               } else if (object instanceof Wall) {
                   cellWidget.addObject(getWidget((Wall) object));
               } else {
                   throw new IllegalArgumentException("Object creation is not supported" + object);
               }
           }
           return cellWidget;
        });
        return cellWidgets.get(cell);
    }

    private GameObjectWidget getGameObjectWidget(@NotNull GameObject gameObject, Function<GameObject, GameObjectWidget> mappingFunction) {
        gameObjectWidgets.computeIfAbsent(gameObject, mappingFunction);
        return gameObjectWidgets.get(gameObject);
    }
}
