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
        var neighbor = cell().neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().noneMatch(GameObject::isSolid);
    }

    @Override
    protected boolean canReplaceIndependent(@NotNull GameObject gameObject, @NotNull Direction direction) {
        var neighbor = cell().neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().filter(o -> o != gameObject).noneMatch(GameObject::isSolid);
    }

    @Override
    public @NotNull ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects() {
        return empty();
    }
}
