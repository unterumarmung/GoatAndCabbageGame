package model;

import events.*;
import model.events.GameMessage;
import org.jetbrains.annotations.NotNull;

public class Game implements MessageListener, MessageSource {
    private final @NotNull FieldBuilder _fieldBuilder;
    private final @NotNull SubscriptionHandler _subscriptionHandler;
    private final @NotNull MessageSender _messageSender;
    private final @NotNull GameField _gameField;
    private GameState _gameState;

    public Game(@NotNull FieldBuilder fieldBuilder, @NotNull SubscriptionHandler subscriptionHandler, @NotNull MessageSender messageSender) {
        _fieldBuilder = fieldBuilder;
        _subscriptionHandler = subscriptionHandler;
        _messageSender = messageSender;
        _gameField = _fieldBuilder.build();
        setup();
    }

    private void setup() {
        for (var cell : _gameField.cells()) {
            _subscriptionHandler.subscribeTo(cell.cell, this);
        }
        _gameState = GameState.CONTINUING;
    }

    private GameState determineOutcomeGame() {
        var goat = _gameField.goat();
        var isGoatOnCellWithCabbage = goat.cell().objects().stream().anyMatch(gameObject -> gameObject instanceof Cabbage);
        var hasNoSteps = !goat.hasEnoughSteps();

        if (isGoatOnCellWithCabbage)
            return GameState.ENDED_SUCCESS_GOAT_REACHED_CABBAGE;
        if (hasNoSteps)
            return GameState.ENDED_FAILURE_STEPS_EXPIRED;

        return GameState.CONTINUING;
    }

    public GameState gameState() {
        return _gameState;
    }

    @Override
    public void handleMessage(MessageSource source, MessageData data) {
        _gameState = determineOutcomeGame();
        _messageSender.emitMessage(this, new GameMessage(_gameState));
    }
}
