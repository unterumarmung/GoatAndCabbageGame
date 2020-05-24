package model.objects;

import model.Cell;
import org.jetbrains.annotations.NotNull;
import utils.Direction;
import utils.Pair;
import utils.collections.ReadOnlyList;

import java.util.stream.Collectors;

import static utils.collections.ReadOnlyList.*;

public class MetalBox implements Box, SolidObject, HookableObject {
    private Cell cell;

    public MetalBox(Cell initialCell) {
        setCell(initialCell);
    }

    @Override
    public ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects() {
        if (cell == null)
            return null;
        var hooked = cell.neighbours().stream()
                .flatMap(cellWithDirection -> cellWithDirection.cell.objects().stream()
                        .filter(o -> o instanceof MagneticObject)
                        .map(o -> new Pair<>((HookableObject)o, cellWithDirection.direction)))
                .collect(Collectors.toList());
        return fromList(hooked);
    }

    @Override
    public void move(@NotNull Direction direction) {

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
                .allMatch(movable -> movable.first.canMoveTo(direction));
        var allOppositeObjectsCanReplaceThis =
                movableHookedObjects
                .filter(hookedObject -> hookedObject.second == direction.opposite())
                .allMatch(movable -> movable.first.canReplace(this, direction));

        var canMove = allHookedCanMoveExceptOppositeObjects && allOppositeObjectsCanReplaceThis;

        var isAnyOfHookedInDirection = hookedObjects.stream().anyMatch(hookedObject -> hookedObject.second == direction);
        if (!isAnyOfHookedInDirection)
            canMove &= canMoveToIndependent(direction);

        return canMove;
    }

    @Override
    public boolean canReplace(@NotNull GameObject gameObject, @NotNull Direction direction) {
        var neighbor = cell.neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().filter(o -> o != gameObject).noneMatch(GameObject::isSolid);
    }

    private boolean canMoveToIndependent(@NotNull Direction direction) {
        var neighbor = cell.neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().noneMatch(GameObject::isSolid);
    }

    @Override
    public Cell cell() {
        return cell;
    }
}
