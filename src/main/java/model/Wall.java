package model;

import org.jetbrains.annotations.NotNull;

public class Wall implements GameObject {
    private final Cell _cell;

    public Wall(@NotNull Cell cell) {
        _cell = cell;
        _cell.addObject(this);
    }

    @Override
    public Cell position() {
        return _cell;
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
