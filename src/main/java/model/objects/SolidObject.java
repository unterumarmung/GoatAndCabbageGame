package model.objects;

public interface SolidObject extends GameObject {
    @Override
    default boolean isSolid() { return true; }
}
