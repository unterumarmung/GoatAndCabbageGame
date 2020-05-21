package model.objects;

import model.Cell;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Wall implements SolidObject {
    private final Cell cell;

    public Wall(@NotNull Cell cell) {
        this.cell = cell;
        this.cell.addObject(this);
    }

    @Override
    public Cell cell() {
        return cell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wall wall = (Wall) o;
        return cell.equals(wall.cell);
    }

    @Override
    public int hashCode() {
        return Objects.hash("Wall");
    }
}
