package xyz.unterumarmung.model.objects;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.Pair;
import xyz.unterumarmung.utils.collections.ReadOnlyList;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableMap;
import static xyz.unterumarmung.model.objects.MagneticBox.Alignment.VERTICAL_NORTH_HORIZONTAL_SOUTH;
import static xyz.unterumarmung.utils.collections.ReadOnlyList.empty;
import static xyz.unterumarmung.utils.collections.ReadOnlyList.fromList;

public class MagneticBox extends MovableHookable implements MagneticObject, MagnitableObject {
    private final @NotNull Map<Direction, MagneticPole> magneticPoles;

    public MagneticBox(Cell cell) {
        this(cell, VERTICAL_NORTH_HORIZONTAL_SOUTH);
    }

    public MagneticBox(Cell cell, @NotNull Alignment alignment) {
        super(cell);
        magneticPoles = alignmentToPoles(alignment);
    }

    @NotNull
    private static Map<Direction, MagneticPole> alignmentToPoles(@NotNull Alignment alignment) {
        var map = new EnumMap<Direction, MagneticPole>(Direction.class);
        switch (alignment) {
            case VERTICAL_NORTH_HORIZONTAL_SOUTH -> {
                map.put(Direction.NORTH, MagneticPole.NORTH);
                map.put(Direction.EAST, MagneticPole.SOUTH);
                map.put(Direction.SOUTH, MagneticPole.NORTH);
                map.put(Direction.WEST, MagneticPole.SOUTH);
            }
            case VERTICAL_SOUTH_HORIZONTAL_NORTH -> {
                map.put(Direction.NORTH, MagneticPole.SOUTH);
                map.put(Direction.EAST, MagneticPole.NORTH);
                map.put(Direction.SOUTH, MagneticPole.SOUTH);
                map.put(Direction.WEST, MagneticPole.NORTH);
            }
        }

        return unmodifiableMap(map);
    }

    @Override
    public Map<Direction, MagneticPole> magneticPoles() {
        return magneticPoles;
    }

    @Override
    public boolean isMagnitableTo(@NotNull MagneticObject magneticObject, @NotNull Direction direction) {
        var objectPoleInDirection = magneticObject.magneticPoles().get(direction.opposite());
        if (objectPoleInDirection == null)
            return false;

        var minePoleInOppositeDirection = magneticPoles().get(direction);
        if (minePoleInOppositeDirection == null)
            return false;

        return objectPoleInDirection == minePoleInOppositeDirection.opposite();
    }

    @Override
    protected boolean canMoveToIndependent(@NotNull Direction direction) {
        var cellToMove = cell().neighborCell(direction);
        var noneSolidInDirection = noneSolidInCell(cellToMove);

        var neighbourObjectsWithDirection =
                getNeighbourObjectsWithDirectionExceptOpposite(cellToMove, direction);

        return noneSolidInDirection && noneOfNeighborCellHasWrongPole(neighbourObjectsWithDirection);
    }

    private boolean noneSolidInCell(Cell cellToMove) {
        return cellToMove.objects().stream().noneMatch(GameObject::isSolid);
    }

    private boolean noneOfNeighborCellHasWrongPole(List<Pair<GameObject, Direction>> neighbourObjectsWithDirection) {
        return neighbourObjectsWithDirection.stream()
                .filter(pair -> pair.first instanceof MagneticObject).map(Pair::<MagneticObject>castFirst)
                .allMatch(magneticObject -> isMagnitableTo(magneticObject.first, magneticObject.second));
    }

    @NotNull
    private List<Pair<GameObject, Direction>> getNeighbourObjectsWithDirectionExceptOpposite(Cell cellToMove, @NotNull Direction direction) {
        return cellToMove.neighbours().stream()
                .filter(cellWithDirection -> cellWithDirection.direction != direction.opposite())
                .flatMap(cellWithDirection -> cellWithDirection.cell.objects().stream()
                        .map(gameObject -> new Pair<>(gameObject, cellWithDirection.direction))
                ).collect(Collectors.toList());
    }

    @Override
    public @NotNull ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects() {
        if (cell() == null)
            return empty();
        var hooked = getAllPossibleHooked();
        return fromList(hooked);
    }

    @NotNull
    private List<Pair<HookableObject, Direction>> getAllPossibleHooked() {
        return cell().neighbours().stream()
                .flatMap(cellWithDirection -> cellWithDirection.cell.objects().stream()
                        .filter(o -> o instanceof MagnitableObject)
                        .filter(o -> ((MagnitableObject) o).isMagnitableTo(this, cellWithDirection.direction.opposite()))
                        .map(o -> new Pair<>((HookableObject) o, cellWithDirection.direction)))
                .collect(Collectors.toList());
    }

    @Override
    protected boolean canReplaceIndependent(@NotNull GameObject gameObject, @NotNull Direction direction) {
        var cellToMove = cell().neighborCell(direction);
        var noneSolidInDirection = cellToMove.objects().stream()
                .filter(gameObject1 -> gameObject1 != gameObject)
                .noneMatch(GameObject::isSolid);

        var neighbourObjectsWithDirection =
                getNeighborObjectsWithDirectionOnLateralSides(direction, cellToMove);

        return noneSolidInDirection && noneOfNeighborCellHasWrongPole(neighbourObjectsWithDirection);
    }

    @NotNull
    private List<Pair<GameObject, Direction>> getNeighborObjectsWithDirectionOnLateralSides(@NotNull Direction direction, Cell cellToMove) {
        return cellToMove.neighbours().stream()
                .filter(cellWithDirection -> cellWithDirection.direction != direction.opposite())
                .filter(cellWithDirection -> cellWithDirection.direction != direction)
                .flatMap(cellWithDirection -> cellWithDirection.cell.objects().stream()
                        .map(o -> new Pair<>(o, cellWithDirection.direction))
                ).collect(Collectors.toList());
    }

    public enum Alignment {
        VERTICAL_NORTH_HORIZONTAL_SOUTH,
        VERTICAL_SOUTH_HORIZONTAL_NORTH
    }
}
