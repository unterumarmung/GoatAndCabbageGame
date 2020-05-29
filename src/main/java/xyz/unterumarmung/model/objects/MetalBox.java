package xyz.unterumarmung.model.objects;

import xyz.unterumarmung.model.Cell;
import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.Pair;
import xyz.unterumarmung.utils.collections.ReadOnlyList;

import java.util.List;
import java.util.stream.Collectors;

import static xyz.unterumarmung.utils.collections.ReadOnlyList.empty;
import static xyz.unterumarmung.utils.collections.ReadOnlyList.fromList;

public class MetalBox extends MovableHookable implements Box, SolidObject, MagnitableObject {

    public MetalBox(Cell initialCell) {
        super(initialCell);
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
                        .filter(o -> o instanceof MagneticObject)
                        .map(o -> new Pair<>((HookableObject) o, cellWithDirection.direction)))
                .collect(Collectors.toList());
    }

    @Override
    protected boolean canReplaceIndependent(@NotNull GameObject gameObject, @NotNull Direction direction) {
        var neighbor = cell().neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().filter(o -> o != gameObject).noneMatch(GameObject::isSolid);
    }

    @Override
    protected boolean canMoveToIndependent(@NotNull Direction direction) {
        var neighbor = cell().neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().noneMatch(GameObject::isSolid);
    }

    @Override
    public boolean isMagnitableTo(@NotNull MagneticObject magneticObject, @NotNull Direction direction) {
        return true;
    }
}
