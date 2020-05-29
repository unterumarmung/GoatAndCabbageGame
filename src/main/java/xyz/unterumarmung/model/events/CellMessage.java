package xyz.unterumarmung.model.events;

import xyz.unterumarmung.events.MessageData;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.model.objects.GameObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CellMessage extends MessageData {
    public final @NotNull Type type;
    public final @NotNull Cell cell;
    public final @NotNull GameObject object;

    public CellMessage(@NotNull Type type, @NotNull Cell cell, @NotNull GameObject object) {
        this.type = type;
        this.cell = cell;
        this.object = object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellMessage that = (CellMessage) o;
        return type == that.type &&
                cell.equals(that.cell) &&
                object.equals(that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, cell, object);
    }

    public enum Type {
        OBJECT_ENTERED,
        OBJECT_LEAVED
    }
}
