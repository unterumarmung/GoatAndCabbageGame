package model;

import events.MessageSender;
import org.jetbrains.annotations.NotNull;
import utils.Point;

public abstract class FieldBuilder {
    private final @NotNull MessageSender _messageSender;
    protected GameField _gameField;

    protected FieldBuilder(@NotNull MessageSender messageSender) {
        _messageSender = messageSender;
    }

    public GameField build() {
        _gameField = new GameField(fieldWidth(), fieldHeight(), exitPoint(), _messageSender);
        addGoat();
        addWalls();
        return _gameField;
    }

    protected abstract int fieldHeight();

    protected abstract int fieldWidth();

    protected abstract Point exitPoint();

    protected abstract void addGoat();

    protected abstract void addWalls();
}
