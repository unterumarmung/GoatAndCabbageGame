package model.objects;

import model.Cell;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return hash();
    }
}
