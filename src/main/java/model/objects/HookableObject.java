package model.objects;

import utils.collections.ReadOnlyList;

public interface HookableObject extends GameObject {
    ReadOnlyList<HookableObject> hookedObjects();
}
