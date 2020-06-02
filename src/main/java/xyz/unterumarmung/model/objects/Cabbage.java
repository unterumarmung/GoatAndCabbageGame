package xyz.unterumarmung.model.objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.Cell;

import java.util.Objects;

public class Cabbage implements GameObject {
    private final Cell position;

    public Cabbage(@NotNull Cell position) {
        this.position = position;
        this.position.addObject(this);
    }

    @Contract(pure = true)
    @Override
    public Cell cell() {
        return position;
    }

    @Contract(pure = true)
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
