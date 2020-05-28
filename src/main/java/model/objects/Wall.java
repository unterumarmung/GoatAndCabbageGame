package model.objects;

import model.Cell;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Wall implements SolidObject {
    private final Cell cell;

    public Wall(@NotNull Cell cell) {
        this.cell = cell;
        this.cell.addObject(this);
    }

    @Contract(pure = true)
    @Override
    public Cell cell() {
        return cell;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return hash();
    }
}
