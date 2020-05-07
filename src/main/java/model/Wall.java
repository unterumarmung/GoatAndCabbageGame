package model;

import org.jetbrains.annotations.NotNull;

public class Wall implements GameObject {
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
    public boolean isSolid() {
        return true;
    }
}
