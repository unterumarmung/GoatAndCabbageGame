package model.objects;

import utils.Direction;

import java.util.Map;

public interface MagneticObject extends HookableObject {
    Map<Direction, MagneticPole> magneticPoles();
}
