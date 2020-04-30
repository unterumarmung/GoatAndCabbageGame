package model;

import events.*;
import model.events.GameMessage;
import org.jetbrains.annotations.NotNull;

public class Game implements MessageListener, MessageSource {
    private final @NotNull SubscriptionHandler subscriptionHandler;
    private final @NotNull MessageSender messageSender;
    private final @NotNull GameField gameField;
    private GameState gameState;

    public Game(@NotNull GameField gameField, @NotNull SubscriptionHandler subscriptionHandler, @NotNull MessageSender messageSender) {
        this.gameField = gameField;
        this.subscriptionHandler = subscriptionHandler;
        this.messageSender = messageSender;
    }

    public void start() {
        for (var cell : gameField.cells()) {
            subscriptionHandler.subscribeTo(cell.cell, this);
        }
        gameState = GameState.CONTINUING;
    }

    private GameState determineOutcomeGame() {
        if (gameField.goat() == null)
            return gameState;
        var goat = gameField.goat();
        var isGoatOnCellWithCabbage = gameField.cell(gameField.exitPoint()).objects().stream().anyMatch(gameObject -> gameObject == goat);
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

    public @NotNull GameField gameField() {
        return gameField;
    }

    @Override
    public void handleMessage(MessageSource source, MessageData data) {
        gameState = determineOutcomeGame();
        messageSender.emitMessage(this, new GameMessage(gameState));
    }
}
