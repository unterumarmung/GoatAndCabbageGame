package xyz.unterumarmung.model.objects;

import xyz.unterumarmung.utils.Direction;

import java.util.Map;

public interface MagneticObject extends HookableObject {
    Map<Direction, MagneticPole> magneticPoles();
}
