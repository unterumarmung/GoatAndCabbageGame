package model.objects;

import model.Cell;

public interface GameObject {
    Cell cell();

    boolean isSolid();
}
