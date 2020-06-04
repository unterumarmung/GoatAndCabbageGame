package xyz.unterumarmung.model.objects;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.Cell;
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
        return noneSolidInCellExcept(cell().neighborCell(direction), gameObject);
    }

    @Override
    protected boolean canMoveToIndependent(@NotNull Direction direction) {
        return noneSolidInCell(cell().neighborCell(direction));
    }

    @Override
    public boolean isMagnitableTo(@NotNull MagneticObject magneticObject, @NotNull Direction direction) {
        return true;
    }
}
