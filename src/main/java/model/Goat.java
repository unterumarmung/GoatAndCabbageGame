package model;

import org.jetbrains.annotations.NotNull;
import utils.Direction;

public class Goat implements GameObject {
    static final int STEP_COST = 1;
    private final @NotNull StepCounter _stepCounter;
    private Cell _position;

    public Goat(@NotNull StepCounter stepCounter) {
        _stepCounter = stepCounter;
    }

    public void move(@NotNull Direction direction) {
        if (!canMoveTo(direction))
            return;

        setPosition(_position.neighborCell(direction));
        _stepCounter.decrease(STEP_COST);
    }

    void setPosition(Cell cell) {
        if (_position != null)
            _position.removeObject(this);
        if (cell != null)
            cell.addObject(this);
        _position = cell;
    }

    boolean canMoveTo(@NotNull Direction direction) {
        var neighbor = _position.neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().noneMatch(GameObject::isSolid)
                && hasEnoughSteps();
    }

    public boolean hasEnoughSteps() {
        return _stepCounter.steps() - STEP_COST >= 0;
    }

    public Cell cell() {
        return _position;
    }

    public @NotNull StepCounter stepCounter() {
        return _stepCounter;
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
