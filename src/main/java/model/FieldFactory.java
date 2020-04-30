package model;

import events.MessageSender;
import org.jetbrains.annotations.NotNull;
import utils.Point;

public abstract class FieldFactory {
    private final @NotNull MessageSender messageSender;
    protected GameField gameField;

    protected FieldFactory(@NotNull MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public GameField create() {
        gameField = new GameField(fieldWidth(), fieldHeight(), exitPoint(), messageSender);
        addGoat();
        addWalls();
        return gameField;
    }

    protected abstract int fieldHeight();

    protected abstract int fieldWidth();

    protected abstract Point exitPoint();

    protected abstract void addGoat();

    protected abstract void addWalls();
}
