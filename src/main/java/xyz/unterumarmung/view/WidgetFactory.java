package xyz.unterumarmung.view;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.model.objects.*;
import xyz.unterumarmung.view.providers.ImageProvider;
import xyz.unterumarmung.view.widgets.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WidgetFactory {
    private final @NotNull Map<GameObject, GameObjectWidget> gameObjectWidgets = new HashMap<>();
    private final @NotNull Map<Cell, CellWidget> cellWidgets = new HashMap<>();
    private final @NotNull Map<Class, ImageProvider> imageProviders;

    public WidgetFactory(@NotNull final Map<Class, ImageProvider> imageProviders) {
        this.imageProviders = imageProviders;
    }

    public GameObjectWidget getWidget(@NotNull final Cabbage cabbage) {
        return getGameObjectWidget(cabbage, gameObject -> new SimpleObjectWidget(imageProviders.get(cabbage.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull final Wall wall) {
        return getGameObjectWidget(wall, gameObject -> new SimpleObjectWidget(imageProviders.get(wall.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull final Goat goat) {
        return getGameObjectWidget(goat, gameObject -> new GoatWidget(goat, imageProviders.get(goat.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull final SimpleBox simpleBox) {
        return getGameObjectWidget(simpleBox, gameObject -> new SimpleObjectWidget(imageProviders.get(simpleBox.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull final MetalBox metalBox) {
        return getGameObjectWidget(metalBox, gameObject -> new SimpleObjectWidget(imageProviders.get(metalBox.getClass())));
    }

    public GameObjectWidget getWidget(@NotNull final MagneticBox magneticBox) {
        return getGameObjectWidget(magneticBox, gameObject -> new MagneticBoxWidget(imageProviders.get(magneticBox.getClass()), magneticBox.alignment()));
    }

    public GameObjectWidget getWidget(@NotNull final GameObject gameObject) {
        return gameObjectWidgets.get(gameObject);
    }

    public CellWidget getWidget(@NotNull final Cell cell) {
        cellWidgets.computeIfAbsent(cell, cell1 -> {
            final var cellWidget = new CellWidget(imageProviders.get(cell.getClass()));
            for (final var object : cell.objects()) {
                if (object instanceof Cabbage) {
                    cellWidget.addObject(getWidget((Cabbage) object));
                } else if (object instanceof Goat) {
                    cellWidget.addObject(getWidget((Goat) object));
                } else if (object instanceof Wall) {
                    cellWidget.addObject(getWidget((Wall) object));
                } else if (object instanceof SimpleBox) {
                    cellWidget.addObject(getWidget((SimpleBox) object));
                } else if (object instanceof MetalBox) {
                    cellWidget.addObject(getWidget((MetalBox) object));
                } else if (object instanceof MagneticBox) {
                    cellWidget.addObject(getWidget((MagneticBox) object));
                } else {
                    throw new IllegalArgumentException("Object creation is not supported" + object);
                }
            }
            return cellWidget;
        });
        return cellWidgets.get(cell);
    }

    private GameObjectWidget getGameObjectWidget(@NotNull final GameObject gameObject, final Function<GameObject, GameObjectWidget> mappingFunction) {
        gameObjectWidgets.computeIfAbsent(gameObject, mappingFunction);
        return gameObjectWidgets.get(gameObject);
    }
}
