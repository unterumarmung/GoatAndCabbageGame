package model.events;

import events.MessageData;
import model.GameState;
import org.jetbrains.annotations.NotNull;

public class GameMessage extends MessageData {
    public final @NotNull GameState gameState;

    public GameMessage(@NotNull GameState gameState) {
        this.gameState = gameState;
    }
}
