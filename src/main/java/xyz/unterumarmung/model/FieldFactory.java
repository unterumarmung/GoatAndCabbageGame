package xyz.unterumarmung.model;

import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.events.MessageSender;
import xyz.unterumarmung.utils.Point;

public abstract class FieldFactory {
    protected final @NotNull MessageSender messageSender;
    protected GameField gameField;

    protected FieldFactory(@NotNull MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public GameField create() {
        gameField = new GameField(fieldWidth(), fieldHeight(), exitPoint(), messageSender);
        addGoat();
        addWalls();
        addBoxes();
        return gameField;
    }

    protected abstract int fieldHeight();

    protected abstract int fieldWidth();

    protected abstract Point exitPoint();

    protected abstract void addGoat();

    protected abstract void addWalls();

    protected abstract void addBoxes();
}
