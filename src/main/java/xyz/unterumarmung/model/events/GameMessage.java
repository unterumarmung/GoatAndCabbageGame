package xyz.unterumarmung.model.events;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.events.MessageData;
import xyz.unterumarmung.model.GameState;

import java.util.Objects;

public class GameMessage extends MessageData {
    public final @NotNull GameState gameState;

    public GameMessage(@NotNull GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameMessage that = (GameMessage) o;
        return gameState == that.gameState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameState);
    }
}
