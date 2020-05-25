package hash;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.*;

class UuidSingletonFactory {
    private static final Map<Hashable, UUID> ids = new HashMap<>();
    static UUID get(Hashable hashable) {
        ids.computeIfAbsent(hashable, hashable1 -> randomUUID());
        return ids.get(hashable);
    }
}

public interface Hashable {
    default int hash() {
        return Objects.hash(UuidSingletonFactory.get(this));
    }
}

