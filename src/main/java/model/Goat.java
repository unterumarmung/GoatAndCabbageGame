package model;

import org.jetbrains.annotations.NotNull;
import utils.Direction;

public class Goat implements GameObject {
    static final int STEP_COST = 1;
    private final @NotNull StepCounter stepCounter;
    private Cell position;

    public Goat(@NotNull StepCounter stepCounter) {
        this.stepCounter = stepCounter;
    }

    public void move(@NotNull Direction direction) {
        if (!canMoveTo(direction))
            return;

        stepCounter.decrease(STEP_COST);
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
        return stepCounter.steps() - STEP_COST >= 0;
    }

    public Cell cell() {
        return position;
    }

    public @NotNull StepCounter stepCounter() {
        return stepCounter;
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
