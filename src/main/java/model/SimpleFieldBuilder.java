package model;

import events.MessageSender;
import org.jetbrains.annotations.NotNull;
import utils.Point;

public class SimpleFieldBuilder extends FieldBuilder {
    private static final int FIELD_HEIGHT = 5;
    private static final int FIELD_WIDTH = 5;
    private static final int DEFAULT_STEPS_COUNT = 10;

    public SimpleFieldBuilder(@NotNull MessageSender messageSender) {
        super(messageSender);
    }

    @Override
    protected int fieldHeight() {
        return FIELD_HEIGHT;
    }

    @Override
    protected int fieldWidth() {
        return FIELD_WIDTH;
    }

    @Override
    protected Point exitPoint() {
        return new Point(3, 3);
    }

    @Override
    protected void addGoat() {
        var goat = new Goat(new StepCounter(DEFAULT_STEPS_COUNT));
        goat.setPosition(gameField.cell(new Point(0, 0)));
    }

    @Override
    protected void addWalls() {
        new Wall(gameField.cell(new Point(2, 3)));
        new Wall(gameField.cell(new Point(3, 2)));
    }
}
