package model;

import org.jetbrains.annotations.NotNull;

public class Cabbage implements GameObject {
    private final Cell _position;

    public Cabbage(@NotNull Cell position) {
        _position = position;
        _position.addObject(this);
    }

    @Override
    public Cell position() {
        return _position;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
