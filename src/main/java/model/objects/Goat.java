package model.objects;

import model.Cell;
import model.exceptions.NoEnoughStepsException;
import org.jetbrains.annotations.NotNull;
import utils.Direction;

import java.util.Objects;

public class Goat implements SolidObject {
    static final int STEP_COST = 1;
    private Cell cell;
    private int steps;

    public Goat(int initialSteps, Cell initialCell) {
        steps = initialSteps;
        cell = initialCell;
        setCell(cell);
    }

    public void move(@NotNull Direction direction) {
        if (!canMoveTo(direction))
            return;

        setCell(cell.neighborCell(direction));
        decreaseSteps();
    }

    void setCell(Cell cell) {
        if (this.cell != null)
            this.cell.removeObject(this);
        if (cell != null)
            cell.addObject(this);
        this.cell = cell;
    }

    boolean canMoveTo(@NotNull Direction direction) {
        var neighbor = cell.neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().noneMatch(GameObject::isSolid)
                && hasEnoughSteps();
    }

    public boolean hasEnoughSteps() {
        return steps() - STEP_COST >= 0;
    }

    public Cell cell() {
        return cell;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goat goat = (Goat) o;
        return steps == goat.steps &&
                cell.equals(goat.cell);
    }

    @Override
    public int hashCode() {
        return Objects.hash("Goat");
    }
}
