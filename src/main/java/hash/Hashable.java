package hash;

import org.jetbrains.annotations.NotNull;
import utils.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public interface Hashable {
    default int hash() {
        return Objects.hash();
    }
}

class UuidSingletonFactory {
    private static final List<Pair<Hashable, UUID>> ids = new LinkedList<>();

    static UUID get(@NotNull Hashable hashable) {
        var possible = ids.stream().filter(id -> id.first == hashable).findFirst();
        if (possible.isPresent())
            return possible.get().second;
        var id = randomUUID();
        ids.add(new Pair<>(hashable, id));
        return id;
    }
}

