package model.objects;

import utils.Direction;
import utils.Pair;
import utils.collections.ReadOnlyList;

public interface HookableObject extends SolidObject {
    ReadOnlyList<Pair<HookableObject, Direction>> hookedObjects();
}
