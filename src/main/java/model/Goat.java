package model;

import model.exceptions.NoEnoughStepsException;
import org.jetbrains.annotations.NotNull;
import utils.Direction;

import java.util.Objects;

public class Goat implements GameObject {
    static final int STEP_COST = 1;
    private Cell position;
    private int steps;

    public Goat(int initialSteps) {
        steps = initialSteps;
    }

    public void move(@NotNull Direction direction) {
        if (!canMoveTo(direction))
            return;

        decreaseSteps();
        setPosition(position.neighborCell(direction));
    }

    void setPosition(Cell cell) {
        if (position != null)
            position.removeObject(this);
        if (cell != null)
            cell.addObject(this);
        position = cell;
    }

    boolean canMoveTo(@NotNull Direction direction) {
        var neighbor = position.neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().noneMatch(GameObject::isSolid)
                && hasEnoughSteps();
    }

    public boolean hasEnoughSteps() {
        return steps() - STEP_COST >= 0;
    }

    public Cell cell() {
        return position;
    }

    private void assertHasSteps() {
        if (!hasEnoughSteps())
            throw new NoEnoughStepsException();
    }

    void decreaseSteps() {
        assertHasSteps();
        steps -= STEP_COST;
    }

    public int steps() {
        return steps;
    }

    @Override
    public boolean isSolid() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goat goat = (Goat) o;
        return steps == goat.steps &&
                position.equals(goat.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash("Goat");
    }
}
