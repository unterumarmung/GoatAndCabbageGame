package model;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cabbage cabbage = (Cabbage) o;
        return position.equals(cabbage.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash("Cabbage");
    }
}
