package model;

import events.*;
import model.events.GameMessage;
import org.jetbrains.annotations.NotNull;

public class Game implements MessageListener, MessageSource {
    private final @NotNull FieldBuilder fieldBuilder;
    private final @NotNull SubscriptionHandler subscriptionHandler;
    private final @NotNull MessageSender messageSender;
    private final @NotNull GameField gameField;
    private GameState gameState;

    public Game(@NotNull FieldBuilder fieldBuilder, @NotNull SubscriptionHandler subscriptionHandler, @NotNull MessageSender messageSender) {
        this.fieldBuilder = fieldBuilder;
        this.subscriptionHandler = subscriptionHandler;
        this.messageSender = messageSender;
        gameField = this.fieldBuilder.build();
        setup();
    }

    private void setup() {
        for (var cell : gameField.cells()) {
            subscriptionHandler.subscribeTo(cell.cell, this);
        }
        gameState = GameState.CONTINUING;
    }

    private GameState determineOutcomeGame() {
        var goat = gameField.goat();
        var isGoatOnCellWithCabbage = goat.cell().objects().stream().anyMatch(gameObject -> gameObject instanceof Cabbage);
        var hasNoSteps = !goat.hasEnoughSteps();

        if (isGoatOnCellWithCabbage)
            return GameState.ENDED_SUCCESS_GOAT_REACHED_CABBAGE;
        if (hasNoSteps)
            return GameState.ENDED_FAILURE_STEPS_EXPIRED;

        return GameState.CONTINUING;
    }

    public GameState gameState() {
        return gameState;
    }

    public GameField gameField() {
        return gameField;
    }

    @Override
    public void handleMessage(MessageSource source, MessageData data) {
        gameState = determineOutcomeGame();
        messageSender.emitMessage(this, new GameMessage(gameState));
    }
}
