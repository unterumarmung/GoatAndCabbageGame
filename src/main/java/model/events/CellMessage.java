package model.events;

import events.MessageData;
import model.Cell;
import model.GameObject;
import org.jetbrains.annotations.NotNull;

public class CellMessage extends MessageData {
    public CellMessage(@NotNull Type type, @NotNull Cell cell, @NotNull GameObject object) {
        this.type = type;
        this.cell = cell;
        this.object = object;
    }

    public enum Type {
        OBJECT_ENTERED,
        OBJECT_LEAVED
    }

    public final @NotNull Type type;
    public final @NotNull Cell cell;
    public final @NotNull GameObject object;
}
