package model.objects;

import utils.Direction;
import utils.collections.ReadOnlyList;

import java.util.Map;

public interface MagneticObject extends HookableObject {
    Map<Direction, MagneticPole> magneticPoles();
}
