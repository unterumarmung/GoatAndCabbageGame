package model.objects;

import hash.Hashable;
import model.Cell;

public interface GameObject extends Hashable {
    Cell cell();

    boolean isSolid();
}
