package model.objects;

import events.MessageSender;
import model.Cell;
import model.GameField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Point;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleBoxShould {
    private GameField gameField;
    private SolidObject solidObject;
    private GameObject notSolidGameObject;
    private Cell initialCell;

    @BeforeEach
    void beforeEach() {
        var messageSender = mock(MessageSender.class);
        gameField = new GameField(5, 5, new Point(0, 0), messageSender);

        solidObject = mock(SolidObject.class);
        when(solidObject.isSolid()).thenReturn(true);

        initialCell = gameField.cell(new Point(0, 0));

        // Настройка поведения мок объекта, который ведёт себя как нетвёрдый объект
        notSolidGameObject = mock(GameObject.class);
        when(notSolidGameObject.isSolid()).thenReturn(false);
    }

    @Test
    void haveNoHookedObjects() {
        // Arrange
        var simpleBox = new SimpleBox(initialCell);

        // Act & Assert
        assertTrue(simpleBox.hookedObjects().isEmpty());
    }

    @Test
    void beSolid() {
        // Arrange
        var simpleBox = new SimpleBox(initialCell);

        // Act & Assert
        assertTrue(simpleBox.isSolid());
    }

    @Test
    void beAbleToMove_toEmptyCell() {
        // Arrange
        var box = new SimpleBox(initialCell);

        // Act && Assert
        assertTrue(box.canMoveTo(Direction.EAST));
    }

    @Test
    void beAbleToMove_toCellWithNotSolidObject() {
        // Arrange
        var box = new SimpleBox(initialCell);
        gameField.cell(new Point(1, 0)).addObject(notSolidGameObject);

        // Act & Assert
        assertTrue(box.canMoveTo(Direction.EAST));
    }

    @Test
    void notBeAbleToMove_toCellWithSolidObject() {
        // Arrange
        var box = new SimpleBox(initialCell);
        gameField.cell(new Point(1, 0)).addObject(solidObject);

        // Act & Assert
        assertFalse(box.canMoveTo(Direction.EAST));
    }

    @Test
    void addItselfToCellObjects() {
        // Arrange
        var box = new SimpleBox(initialCell);

        // Act & Assert
        assertTrue(initialCell.objects().contains(box));
    }

    @Test
    void beAbleToReplaceSolidObject() {
        // Arrange
        var box = new SimpleBox(initialCell);
        gameField.cell(new Point(1, 0)).addObject(solidObject);

        // Act & Assert
        assertTrue(box.canReplace(solidObject, Direction.EAST));
    }
}