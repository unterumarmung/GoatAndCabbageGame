package model.objects;

import model.Cell;
import org.jetbrains.annotations.NotNull;
import utils.Direction;
import utils.Pair;

import java.util.HashSet;
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
            var hookedObjects = hookedObjects().stream().map(pair -> (MovableHookable) pair.first).collect(Collectors.toList());

            var neighborCell = cell.neighborCell(direction);
            setCell(neighborCell);

            alreadyMoved.add(this);

            for (var hookedObject : hookedObjects)
                hookedObject.move(direction, alreadyMoved);

            return true;
        }
        return false;
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

        var allHookedAreMovable = hookedObjects.stream().allMatch(hookedObject -> hookedObject.first instanceof MovableObject);
        if (!allHookedAreMovable)
            return false;

        var movableHookedObjects = hookedObjects.stream().map(Pair::<MovableObject>castFirst);

        var allHookedCanMoveExceptOppositeObjects =
                movableHookedObjects
                        .filter(hookedObject -> hookedObject.second != direction.opposite())
                        .allMatch(hookedObject -> hookedObject.first.canMoveTo(direction));
        var allOppositeObjectsCanReplaceThis =
                movableHookedObjects
                        .filter(hookedObject -> hookedObject.second == direction.opposite())
                        .allMatch(hookedObject -> hookedObject.first.canReplace(this, direction));

        var canMove = allHookedCanMoveExceptOppositeObjects && allOppositeObjectsCanReplaceThis;

        var isAnyOfHookedInDirection = hookedObjects.stream().anyMatch(hookedObject -> hookedObject.second == direction);
        if (!isAnyOfHookedInDirection)
            canMove &= canMoveToIndependent(direction);

        return canMove;
    }

    protected abstract boolean canMoveToIndependent(Direction direction);

    @Override
    public Cell cell() {
        return cell;
    }

    @Override
    public int hashCode() {
        return hash();
    }
}
