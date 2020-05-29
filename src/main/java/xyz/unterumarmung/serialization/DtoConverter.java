package xyz.unterumarmung.serialization;

import xyz.unterumarmung.Level;
import xyz.unterumarmung.events.MessageSender;
import xyz.unterumarmung.events.SubscriptionHandler;
import org.jetbrains.annotations.NotNull;
import xyz.unterumarmung.model.FieldFactory;
import xyz.unterumarmung.model.objects.*;
import xyz.unterumarmung.serialization.dto.FieldDto;
import xyz.unterumarmung.serialization.dto.LevelDto;
import xyz.unterumarmung.serialization.dto.PointDto;
import xyz.unterumarmung.utils.Point;

import static xyz.unterumarmung.serialization.DtoConverter.*;

class DtoConverter {
    private final @NotNull SubscriptionHandler subscriptionHandler;
    private final @NotNull MessageSender messageSender;

    public DtoConverter(@NotNull SubscriptionHandler subscriptionHandler, @NotNull MessageSender messageSender) {
        this.subscriptionHandler = subscriptionHandler;
        this.messageSender = messageSender;
    }

    public Level convert(LevelDto levelDto) {
        return new Level(levelDto.id, levelDto.description, messageSender, subscriptionHandler, new FieldDtoFactory(messageSender, levelDto.gameField));
    }

    static Point convert(PointDto pointDto) {
        return new Point(pointDto.x, pointDto.y);
    }
}

class FieldDtoFactory extends FieldFactory {
    private final @NotNull FieldDto fieldDto;

    protected FieldDtoFactory(@NotNull MessageSender messageSender, @NotNull FieldDto fieldDto) {
        super(messageSender);
        this.fieldDto = fieldDto;
    }

    @Override
    protected int fieldHeight() {
        return fieldDto.height;
    }

    @Override
    protected int fieldWidth() {
        return fieldDto.width;
    }

    @Override
    protected Point exitPoint() {
        return convert(fieldDto.exitPoint);
    }

    @Override
    protected void addGoat() {
        for (var goatDto : fieldDto.goat) {
            var goat = new Goat(goatDto.initialSteps, gameField.cell(convert(goatDto.position)), messageSender);
        }
    }

    @Override
    protected void addWalls() {
        if (fieldDto.wall == null)
            return;
        for (var wallDto : fieldDto.wall) {
            var wall = new Wall(gameField.cell(convert(wallDto.position)));
        }
    }

    @Override
    protected void addBoxes() {
        if (fieldDto.simpleBox != null)
            for (var simpleBoxDto : fieldDto.simpleBox) {
                var box = new SimpleBox(gameField.cell(convert(simpleBoxDto.position)));
            }
        if (fieldDto.metalBox != null)
            for (var metalBoxDto : fieldDto.metalBox) {
                var box = new MetalBox(gameField.cell(convert(metalBoxDto.position)));
            }
        if (fieldDto.magneticBox != null)
            for (var magneticBox : fieldDto.magneticBox) {
                var box = new MagneticBox(gameField.cell(convert(magneticBox.position)), magneticBox.alignment);
            }
    }
}