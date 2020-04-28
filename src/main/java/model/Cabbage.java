package model;

import org.jetbrains.annotations.NotNull;

public class Cabbage implements GameObject {
    private final Cell position;

    public Cabbage(@NotNull Cell position) {
        this.position = position;
        this.position.addObject(this);
    }

    @Override
    public Cell cell() {
        return position;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
