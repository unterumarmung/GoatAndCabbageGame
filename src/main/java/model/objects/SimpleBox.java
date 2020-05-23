package model.objects;

import model.Cell;
import org.jetbrains.annotations.NotNull;
import utils.Direction;

public class SimpleBox implements Box {
    private Cell cell;

    public SimpleBox(@NotNull Cell initialCell) {
        this.cell = initialCell;
    }

    public void move(@NotNull Direction direction) {
        if (canMoveTo(direction)) {
            setCell(cell.neighborCell(direction));
        }
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
        var neighbor = cell.neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().noneMatch(GameObject::isSolid);
    }

    @Override
    public boolean canReplace(@NotNull GameObject gameObject, @NotNull Direction direction) {
        var neighbor = cell.neighborCell(direction);
        return neighbor != null
                && neighbor.objects().stream().filter(o -> o != gameObject).noneMatch(GameObject::isSolid);
    }

    @Override
    public Cell cell() {
        return cell;
    }
}
