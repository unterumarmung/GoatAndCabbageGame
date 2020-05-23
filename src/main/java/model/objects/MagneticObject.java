package model.objects;

import utils.collections.ReadOnlyList;

public interface MagneticObject extends HookableObject {
    ReadOnlyList<MagneticPoleWithDirection> magneticPoles();
}
