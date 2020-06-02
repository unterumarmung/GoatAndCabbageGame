package xyz.unterumarmung.view;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.model.objects.*;
import xyz.unterumarmung.view.providers.ImageProvider;
import xyz.unterumarmung.view.widgets.CellWidget;
import xyz.unterumarmung.view.widgets.GameObjectWidget;
import xyz.unterumarmung.view.widgets.GoatWidget;
import xyz.unterumarmung.view.widgets.SimpleObjectWidget;

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
        return getGameObjectWidget(cabbage, gameObject -> new SimpleObjectWidget(imageProviders.get(cabbage.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull Wall wall) {
        return getGameObjectWidget(wall, gameObject -> new SimpleObjectWidget(imageProviders.get(wall.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull Goat goat) {
        return getGameObjectWidget(goat, gameObject -> new GoatWidget(goat, imageProviders.get(goat.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull SimpleBox simpleBox) {
        return getGameObjectWidget(simpleBox, gameObject -> new SimpleObjectWidget(imageProviders.get(simpleBox.getClass())));
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
                } else if (object instanceof SimpleBox) {
                    cellWidget.addObject(getWidget((SimpleBox) object));
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
