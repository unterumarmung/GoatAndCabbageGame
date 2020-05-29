package model.objects;

import events.MessageSender;
import events.MessageSource;
import model.Cell;
import model.events.GoatMessage;
import model.exceptions.NoEnoughStepsException;
import org.jetbrains.annotations.NotNull;
import utils.Direction;
import utils.Pair;
import utils.collections.ReadOnlyList;

import static utils.collections.ReadOnlyList.empty;
import static utils.collections.ReadOnlyList.of;

public class Goat extends MovableHookable implements SolidObject, MovableObject, MessageSource {
    static final int STEP_COST = 1;
    private int steps;
    private Pair<Box, Direction> hookedBox;
    private final @NotNull MessageSender messageSender;

    public Goat(int initialSteps, Cell initialCell, @NotNull MessageSender messageSender) {
        super(initialCell);
        steps = initialSteps;
        this.messageSender = messageSender;
    }

    public boolean hookBox(@NotNull Direction direction) {
        var possibleBox = cell().neighborCell(direction).objects().stream().filter(gameObject -> gameObject instanceof Box).findFirst();
        if (possibleBox.isPresent()) {
            hookedBox = new Pair<>((Box) possibleBox.get(), direction);
            return true;
        }
        return false;
    }

    public void unhookBox() {
        hookedBox = null;
    }

    @Override
    public boolean move(@NotNull Direction direction) {
        var cellFrom = cell();
        var moved = super.move(direction);
        if (moved) {
            decreaseSteps();
            messageSender.emitMessage(this, new GoatMessage(cellFrom, cell()));
        }
        return moved;
    }

    @Override
    protected boolean canMoveToIndependent(@NotNull Direction direction) {
        var neighbor = cell().neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().noneMatch(GameObject::isSolid)
                && hasEnoughSteps();
    }

    @Override
    protected boolean canReplaceIndependent(@NotNull GameObject gameObject, @NotNull Direction direction) {
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
    public @NotNull ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects() {
        if (hookedBox == null)
            return empty();
        else
            return of(hookedBox.castFirst());
    }
}
