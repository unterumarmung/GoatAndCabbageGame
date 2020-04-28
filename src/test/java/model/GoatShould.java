package model;

import events.MessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoatShould {
    private MessageSender messageSender;
    private Cell cell1;
    private Cell cell2;
    private Direction direction;
    private GameObject solidGameObject;
    private GameObject notSolidGameObject;
    private StepCounter stepCounter;

    @BeforeEach
    void beforeEach() {
        stepCounter = new StepCounter(Goat.STEP_COST * 4);

        // Создание клеток поля для имитации движения козы
        messageSender = mock(MessageSender.class);
        cell1 = new Cell(messageSender);
        cell2 = new Cell(messageSender);
        direction = Direction.EAST;
        cell1.setNeighbor(cell2, direction);

        // Настройка поведения мок объекта, который ведёт себя как твёрдый объект
        solidGameObject = mock(GameObject.class);
        when(solidGameObject.isSolid()).thenReturn(true);

        // Настройка поведения мок объекта, который ведёт себя как нетвёрдый объект
        notSolidGameObject = mock(GameObject.class);
        when(notSolidGameObject.isSolid()).thenReturn(false);
    }

    @Test
    void haveEnoughSteps_whenStepCounterHasLargerOrEqualCount() {
        // Arrange
        var goat1 = new Goat(new StepCounter(Goat.STEP_COST * 4));
        var goat2 = new Goat(new StepCounter(Goat.STEP_COST + 1));
        var goat3 = new Goat(new StepCounter(Goat.STEP_COST));

        // Act & Assert
        assertTrue(goat1.hasEnoughSteps());
        assertTrue(goat2.hasEnoughSteps());
        assertTrue(goat3.hasEnoughSteps());
    }

    @Test
    void notHaveEnoughSteps_whenStepCounterHasSmallerThanStepCost() {
        // Arrange
        var goat = new Goat(new StepCounter(Goat.STEP_COST - 1));

        // Act & Assert
        assertFalse(goat.hasEnoughSteps());
    }

    @Test
    void move_toEmptyCell() {
        // Arrange
        var goat = new Goat(stepCounter);
        goat.setPosition(cell1);

        // Act
        goat.move(direction);

        // Assert
        assertSame(cell2, goat.cell());
    }

    @Test
    void move_toCellWithNotSolidObject() {
        // Arrange
        var goat = new Goat(stepCounter);
        goat.setPosition(cell1);
        cell2.addObject(notSolidGameObject);

        // Act
        goat.move(direction);

        // Assert
        assertSame(cell2, goat.cell());
    }

    @Test
    void notMove_toCellWithSolidObject() {
        // Arrange
        var goat = new Goat(stepCounter);
        goat.setPosition(cell1);
        cell2.addObject(solidGameObject);
        // Act
        goat.move(direction);

        // Assert
        assertSame(cell1, goat.cell());
    }

    @Test
    void addItselfToNewCell_whenMoveSuccessful() {
        // Arrange
        var goat = new Goat(stepCounter);
        goat.setPosition(cell1);
        var objectsBeforeMove = cell2.objects();

        // Act
        goat.move(direction);

        // Assert
        assertFalse(objectsBeforeMove.contains(goat));
        assertTrue(cell2.objects().contains(goat));
    }

    @Test
    void removeItselfFromLastCell_whenMoveSuccessful() {
        // Arrange
        var goat = new Goat(stepCounter);
        goat.setPosition(cell1);
        var objectsBeforeMove = cell1.objects();

        // Act
        goat.move(direction);

        // Assert
        assertTrue(objectsBeforeMove.contains(goat));
        assertFalse(cell1.objects().contains(goat));
    }

    @Test
    void notAddItselfToNewCell_whenMoveNotSuccessful() {
        // Arrange
        var goat = spy(new Goat(stepCounter));
        goat.setPosition(cell1);
        // Намеренно запрещаем козе куда-либо передвигаться
        when(goat.canMoveTo(direction)).thenReturn(false);

        // Act
        goat.move(direction);

        // Assert
        assertFalse(cell2.objects().contains(goat));
    }

    @Test
    void notRemoveItselfFromLastCell_whenMoveNotSuccessful() {
        // Arrange
        var goat = spy(new Goat(stepCounter));
        goat.setPosition(cell1);
        // Намеренно запрещаем козе куда-либо передвигаться
        when(goat.canMoveTo(direction)).thenReturn(false);

        // Act
        goat.move(direction);

        // Assert
        assertTrue(cell1.objects().contains(goat));
    }

    @Test
    void beSolid() {
        // Arrange & Act & Assert
        assertTrue(new Goat(stepCounter).isSolid());
    }

    @Test
    void haveGivenStepCounter() {
        // Arrange
        var goat = new Goat(stepCounter);

        // Act & Assert
        assertSame(stepCounter, goat.stepCounter());
    }
}