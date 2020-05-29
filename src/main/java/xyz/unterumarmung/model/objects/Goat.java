package xyz.unterumarmung.model.objects;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.events.MessageSender;
import xyz.unterumarmung.events.MessageSource;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.model.events.GoatMessage;
import xyz.unterumarmung.model.exceptions.NoEnoughStepsException;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.Pair;
import xyz.unterumarmung.utils.collections.ReadOnlyList;

import static xyz.unterumarmung.utils.collections.ReadOnlyList.empty;
import static xyz.unterumarmung.utils.collections.ReadOnlyList.of;

public class Goat extends MovableHookable implements SolidObject, MovableObject, MessageSource {
    static final int STEP_COST = 1;
    private final @NotNull MessageSender messageSender;
    private int steps;
    private Pair<Box, Direction> hookedBox;

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
