package model.objects;

import utils.collections.ReadOnlyList;

public interface MagneticObject extends GameObject {
    ReadOnlyList<MagneticPoleWithDirection> magneticPoles();
}
