package model;

import events.MessageSender;
import model.exceptions.NoEnoughStepsException;
import model.objects.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoatShould {
    private Cell cell1;
    private Cell cell2;
    private Direction direction;
    private GameObject solidGameObject;
    private GameObject notSolidGameObject;

    @BeforeEach
    void beforeEach() {
        // Создание клеток поля для имитации движения козы
        var messageSender = mock(MessageSender.class);
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
    void haveEnoughSteps_whenStepsHasLargerOrEqualCount() {
        // Arrange
        var goat1 = new Goat(Goat.STEP_COST * 4);
        var goat2 = new Goat(Goat.STEP_COST + 1);
        var goat3 = new Goat(Goat.STEP_COST);

        // Act & Assert
        assertTrue(goat1.hasEnoughSteps());
        assertTrue(goat2.hasEnoughSteps());
        assertTrue(goat3.hasEnoughSteps());
    }

    @Test
    void notHaveEnoughSteps_whenStepCounterHasSmallerThanStepCost() {
        // Arrange
        var goat = new Goat(Goat.STEP_COST - 1);

        // Act & Assert
        assertFalse(goat.hasEnoughSteps());
    }

    @Test
    void move_toEmptyCell() {
        // Arrange
        var goat = new Goat(Goat.STEP_COST * 4);
        goat.setPosition(cell1);

        // Act
        goat.move(direction);

        // Assert
        assertSame(cell2, goat.cell());
    }

    @Test
    void move_toCellWithNotSolidObject() {
        // Arrange
        var goat = new Goat(Goat.STEP_COST * 4);
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
        var goat = new Goat(Goat.STEP_COST * 4);
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
        var goat = new Goat(Goat.STEP_COST * 4);
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
        var goat = new Goat(Goat.STEP_COST * 4);
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
        var goat = spy(new Goat(Goat.STEP_COST * 4));
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
        var goat = spy(new Goat(Goat.STEP_COST * 4));
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
        assertTrue(new Goat(Goat.STEP_COST * 4).isSolid());
    }

    @Test
    void initializeWithGivenSteps() {
        // Arrange
        var steps = 15;

        // Act
        var goat = new Goat(steps);

        // Assert
        assertEquals(steps, goat.steps());
    }

    @Test
    void throw_whenNotEnoughSteps() {
        // Arrange
        var initialSteps = 0;
        var goat = new Goat(initialSteps);

        // Act & Assert
        assertThrows(NoEnoughStepsException.class, goat::decreaseSteps);
    }
}