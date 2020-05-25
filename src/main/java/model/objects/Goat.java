package model.objects;

import model.Cell;
import model.exceptions.NoEnoughStepsException;
import org.jetbrains.annotations.NotNull;
import utils.Direction;
import utils.Pair;
import utils.collections.ReadOnlyList;

import java.util.Objects;

import static utils.collections.ReadOnlyList.*;

public class Goat extends MovableHookable implements SolidObject, MovableObject {
    static final int STEP_COST = 1;
    private int steps;
    private Pair<Box, Direction> hookedBox;

    public Goat(int initialSteps, Cell initialCell) {
        super(initialCell);
        steps = initialSteps;
    }

    public boolean hookBox(@NotNull Direction direction) {
        var possibleBox = cell().neighborCell(direction).objects().stream().filter(gameObject -> gameObject instanceof Box).findFirst();
        if (possibleBox.isPresent())
        {
            hookedBox = new Pair<>((Box) possibleBox.get(), direction);
            return true;
        }
        return false;
    }

    @Override
    public boolean move(@NotNull Direction direction) {
        var moved = super.move(direction);
        if (moved)
            decreaseSteps();
        return moved;
    }

    @Override
    protected boolean canMoveToIndependent(Direction direction) {
        var neighbor = cell().neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().noneMatch(GameObject::isSolid)
                && hasEnoughSteps();
    }

    @Override
    public boolean canReplace(@NotNull GameObject gameObject, @NotNull Direction direction) {
        var neighbor = cell().neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().filter(o -> o != gameObject).noneMatch(GameObject::isSolid);
    }

    public boolean hasEnoughSteps() {
        return steps() - STEP_COST >= 0;
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
        return this == o;
    }

    @Override
    public int hashCode() {
        return Objects.hash("Goat");
    }

    @Override
    public ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects() {
        if (hookedBox == null)
            return empty();
        else
            return of(hookedBox.castFirst());
    }
}
