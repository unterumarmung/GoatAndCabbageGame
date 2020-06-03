package xyz.unterumarmung.model.objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.Pair;
import xyz.unterumarmung.utils.collections.ReadOnlyList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MovableHookable implements MovableObject, HookableObject {
    private Cell cell;

    public MovableHookable(Cell cell) {
        setCell(cell);
    }

    @Override
    public boolean move(@NotNull Direction direction) {
        if (canMoveTo(direction))
        {
            var allHooked = collectAllHooked();
            for (var hooked: allHooked) {
                var newCell = hooked.cell().neighborCell(direction);
                hooked.setCell(newCell);
            }
            return true;
        }
        return false;
    }

    private Set<MovableHookable> collectAllHooked() {
        var allHooked = new HashSet<MovableHookable>();
        allHooked.add(this);
        collectAllHooked(allHooked);
        return allHooked;
    }

    private void collectAllHooked(Set<MovableHookable> hooked) {
        var objs = castHookedToMovableHookable();

        for (var obj : objs.stream().map(pair -> pair.first).filter(obj -> !hooked.contains(obj)).collect(Collectors.toList())) {
            hooked.add(obj);
            obj.collectAllHooked(hooked);
        }
    }

    private List<Pair<MovableHookable, Direction>> castHookedToMovableHookable() {
        return hookedObjects().stream().map(Pair::<MovableHookable>castFirst).collect(Collectors.toList());
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
        return canMoveTo(direction, new HashSet<>());
    }

    private <T extends HookableObject> List<Pair<T, Direction>> filterObjects(Stream<Pair<T, Direction>> objs, Set<MovableHookable> except) {
        return objs.filter(pair -> !except.contains(pair.first)).collect(Collectors.toList());
    }


    private boolean canMoveTo(@NotNull Direction direction, Set<MovableHookable> exceptObjects) {
        var hookedObjects = filterObjects(hookedObjects().stream(), exceptObjects);
        if (hookedObjects.isEmpty() && canMoveToIndependent(direction))
            return true;

        if (!allHookedAreMovable(hookedObjects))
            return false;

        var movableHookedObjects = castHookedToMovable(hookedObjects);
        exceptObjects.add(this);
        var canMove = allHookedCanMoveExceptOppositeObjects(movableHookedObjects, direction, exceptObjects)
                && allOppositeObjectsCanReplaceThis(movableHookedObjects, direction, exceptObjects);

        if (!isAnyOfHookedInDirection(hookedObjects, direction))
            canMove &= canMoveToIndependent(direction);

        return canMove;
    }

    @Override
    public boolean canReplace(@NotNull GameObject gameObject, @NotNull Direction direction) {
        return canReplace(gameObject, direction, new HashSet<>());
    }

    private boolean canReplace(@NotNull GameObject gameObject, @NotNull Direction direction, Set<MovableHookable> exceptObjects) {
        var hookedObjects = filterObjects(hookedObjects().stream(), exceptObjects);
        if (hookedObjects.isEmpty() && canReplaceIndependent(gameObject, direction))
            return true;

        if (!allHookedAreMovable(hookedObjects))
            return false;

        var movableHookedObjects = castHookedToMovable(hookedObjects);
        exceptObjects.add(this);
        return allOppositeObjectsCanReplaceThis(movableHookedObjects, direction, exceptObjects) && canReplaceIndependent(gameObject, direction);
    }

    protected abstract boolean canReplaceIndependent(@NotNull GameObject gameObject, @NotNull Direction direction);

    private boolean isAnyOfHookedInDirection(List<Pair<HookableObject, Direction>> hookedObjects, @NotNull Direction direction) {
        return hookedObjects.stream().anyMatch(hookedObject -> hookedObject.second == direction);
    }

    private List<Pair<MovableHookable, Direction>> castHookedToMovable(@NotNull List<Pair<HookableObject, Direction>> hookedObjects) {
        return hookedObjects.stream().map(Pair::<MovableHookable>castFirst).collect(Collectors.toList());
    }

    private boolean allHookedAreMovable(List<Pair<HookableObject, Direction>> hookedObjects) {
        return hookedObjects.stream().allMatch(hookedObject -> hookedObject.first instanceof MovableHookable);
    }

    private boolean allHookedCanMoveExceptOppositeObjects(@NotNull List<Pair<MovableHookable, Direction>> movableHookedObjects, @NotNull Direction direction, Set<MovableHookable> except) {
        return movableHookedObjects.stream()
                .filter(hookedObject -> hookedObject.second != direction.opposite())
                .allMatch(hookedObject -> hookedObject.first.canMoveTo(direction, except));
    }

    private boolean allOppositeObjectsCanReplaceThis(@NotNull List<Pair<MovableHookable, Direction>> movableHookedObjects, @NotNull Direction direction, Set<MovableHookable> except) {
        return movableHookedObjects.stream()
                .filter(hookedObject -> hookedObject.second == direction.opposite())
                .allMatch(hookedObject -> hookedObject.first.canReplace(this, direction, except));
    }

    protected abstract boolean canMoveToIndependent(@NotNull Direction direction);

    @Contract(pure = true)
    @Override
    public Cell cell() {
        return cell;
    }

    @Override
    public int hashCode() {
        return hash();
    }
}