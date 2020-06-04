package xyz.unterumarmung.model.objects;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.Pair;
import xyz.unterumarmung.utils.collections.ReadOnlyList;

import static xyz.unterumarmung.utils.collections.ReadOnlyList.empty;

public class SimpleBox extends MovableHookable implements Box {

    public SimpleBox(@NotNull Cell initialCell) {
        super(initialCell);
    }

    @Override
    /// Так как мы знаем, что простой ящик никогда не захватывает объекты сам, то смысла в коде из MovableHookable нет
    /// Так что override'им, чтобы упростить вычисления
    public boolean canMoveTo(@NotNull Direction direction) {
        return canMoveToIndependent(direction);
    }

    @Override
    protected boolean canMoveToIndependent(@NotNull Direction direction) {
        return noneSolidInCell(cell().neighborCell(direction));
    }

    @Override
    protected boolean canReplaceIndependent(@NotNull GameObject gameObject, @NotNull Direction direction) {
        return noneSolidInCellExcept(cell().neighborCell(direction), gameObject);
    }

    @Override
    public @NotNull ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects() {
        return empty();
    }
}
