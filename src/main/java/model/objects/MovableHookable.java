package model.objects;

import model.Cell;
import org.jetbrains.annotations.NotNull;
import utils.Direction;
import utils.Pair;
import utils.collections.ReadOnlyList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MovableHookable implements MovableObject, HookableObject {
    private Cell cell;

    public MovableHookable(Cell cell) {
        setCell(cell);
    }

    @Override
    public boolean move(@NotNull Direction direction) {
        return move(direction, new HashSet<>());
    }

    protected boolean move(@NotNull Direction direction, Set<MovableHookable> alreadyMoved) {
        if (canMoveTo(direction) && !alreadyMoved.contains(this)) {
            var hookedObjects = castHookedToMovableHookable();

            var neighborCell = cell.neighborCell(direction);
            setCell(neighborCell);

            alreadyMoved.add(this);

            for (var hookedObject : hookedObjects)
                hookedObject.move(direction, alreadyMoved);

            return true;
        }
        return false;
    }

    @NotNull
    private List<MovableHookable> castHookedToMovableHookable() {
        return hookedObjects().stream().map(pair -> (MovableHookable) pair.first).collect(Collectors.toList());
    }

    void setCell(Cell cell) {
        if (this.cell != null)
            this.cell.removeObject(this);
        if (cell != null)
            cell.addObject(this);
        this.cell = cell;
    }

    @Override
    public boolean canMoveTo(@NotNull Direction direction) {
        var hookedObjects = hookedObjects();
        if (hookedObjects.isEmpty() && canMoveToIndependent(direction))
            return true;

        if (!allHookedAreMovable(hookedObjects))
            return false;

        var movableHookedObjects = castHookedToMovable(hookedObjects);

        var canMove = allHookedCanMoveExceptOppositeObjects(movableHookedObjects, direction)
                && allOppositeObjectsCanReplaceThis(movableHookedObjects, direction);

        if (!isAnyOfHookedInDirection(hookedObjects, direction))
            canMove &= canMoveToIndependent(direction);

        return canMove;
    }

    private boolean isAnyOfHookedInDirection(@NotNull ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects, @NotNull Direction direction) {
        return hookedObjects.stream().anyMatch(hookedObject -> hookedObject.second == direction);
    }

    @NotNull
    private List<Pair<MovableObject, Direction>> castHookedToMovable(@NotNull ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects) {
        return hookedObjects.stream().map(Pair::<MovableObject>castFirst).collect(Collectors.toList());
    }

    private boolean allHookedAreMovable(@NotNull ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects) {
        return hookedObjects.stream().allMatch(hookedObject -> hookedObject.first instanceof MovableObject);
    }

    private boolean allHookedCanMoveExceptOppositeObjects(@NotNull List<Pair<MovableObject, Direction>> movableHookedObjects, @NotNull Direction direction) {
        return movableHookedObjects.stream()
                .filter(hookedObject -> hookedObject.second != direction.opposite())
                .allMatch(hookedObject -> hookedObject.first.canMoveTo(direction));
    }

    private boolean allOppositeObjectsCanReplaceThis(@NotNull List<Pair<MovableObject, Direction>> movableHookedObjects, @NotNull Direction direction) {
        return movableHookedObjects.stream()
                .filter(hookedObject -> hookedObject.second == direction.opposite())
                .allMatch(hookedObject -> hookedObject.first.canReplace(this, direction));
    }

    protected abstract boolean canMoveToIndependent(@NotNull Direction direction);

    @Override
    public Cell cell() {
        return cell;
    }

    @Override
    public int hashCode() {
        return hash();
    }
}
