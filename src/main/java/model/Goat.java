package model;

import org.jetbrains.annotations.NotNull;
import utils.Direction;

public class Goat implements GameObject {
    private final int STEP_COST = 1;

    private Cell _position;
    private final @NotNull StepCounter _stepCounter;

    public Goat(@NotNull StepCounter stepCounter) {
        _stepCounter = stepCounter;
    }

    public void move(@NotNull Direction direction) {
        var newPosition = canMoveTo(direction);
        if (newPosition == null)
            return;

        setPosition(newPosition);
        _stepCounter.decrease(STEP_COST);
    }

    void setPosition(Cell cell) {
        if (_position != null)
            _position.removeObject(this);
        if (cell != null)
            cell.addObject(this);
        _position = cell;
    }

    private Cell canMoveTo(@NotNull Direction direction) {
        var neighbor = _position.neighborCell(direction);
        if (neighbor == null
                || neighbor.objects().stream().anyMatch(GameObject::isSolid)
                || !hasEnoughSteps())
            return null;

        return neighbor;
    }

    public boolean hasEnoughSteps() {
        return _stepCounter.steps() - STEP_COST >= 0;
    }

    public Cell position() {
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
