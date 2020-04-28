package model;

import events.MessageSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoatShould {
    private MessageSender _messageSender;
    private Cell _cell1;
    private Cell _cell2;
    private Direction _direction;
    private GameObject _solidGameObject;
    private GameObject _notSolidGameObject;
    private StepCounter _stepCounter;

    @BeforeEach
    void beforeEach() {
        _stepCounter = new StepCounter(Goat.STEP_COST * 4);

        // Создание клеток поля для имитации движения козы
        _messageSender = mock(MessageSender.class);
        _cell1 = new Cell(_messageSender);
        _cell2 = new Cell(_messageSender);
        _direction = Direction.EAST;
        _cell1.setNeighbor(_cell2, _direction);

        // Настройка поведения мок объекта, который ведёт себя как твёрдый объект
        _solidGameObject = mock(GameObject.class);
        when(_solidGameObject.isSolid()).thenReturn(true);

        // Настройка поведения мок объекта, который ведёт себя как нетвёрдый объект
        _notSolidGameObject = mock(GameObject.class);
        when(_notSolidGameObject.isSolid()).thenReturn(false);
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
        var goat = new Goat(_stepCounter);
        goat.setPosition(_cell1);

        // Act
        goat.move(_direction);

        // Assert
        assertSame(_cell2, goat.cell());
    }

    @Test
    void move_toCellWithNotSolidObject() {
        // Arrange
        var goat = new Goat(_stepCounter);
        goat.setPosition(_cell1);
        _cell2.addObject(_notSolidGameObject);

        // Act
        goat.move(_direction);

        // Assert
        assertSame(_cell2, goat.cell());
    }

    @Test
    void notMove_toCellWithSolidObject() {
        // Arrange
        var goat = new Goat(_stepCounter);
        goat.setPosition(_cell1);
        _cell2.addObject(_solidGameObject);
        // Act
        goat.move(_direction);

        // Assert
        assertSame(_cell1, goat.cell());
    }

    @Test
    void addItselfToNewCell_whenMoveSuccessful() {
        // Arrange
        var goat = new Goat(_stepCounter);
        goat.setPosition(_cell1);
        var objectsBeforeMove = _cell2.objects();

        // Act
        goat.move(_direction);

        // Assert
        assertFalse(objectsBeforeMove.contains(goat));
        assertTrue(_cell2.objects().contains(goat));
    }

    @Test
    void removeItselfFromLastCell_whenMoveSuccessful() {
        // Arrange
        var goat = new Goat(_stepCounter);
        goat.setPosition(_cell1);
        var objectsBeforeMove = _cell1.objects();

        // Act
        goat.move(_direction);

        // Assert
        assertTrue(objectsBeforeMove.contains(goat));
        assertFalse(_cell1.objects().contains(goat));
    }

    @Test
    void notAddItselfToNewCell_whenMoveNotSuccessful() {
        // Arrange
        var goat = spy(new Goat(_stepCounter));
        goat.setPosition(_cell1);
        // Намеренно запрещаем козе куда-либо передвигаться
        when(goat.canMoveTo(_direction)).thenReturn(false);

        // Act
        goat.move(_direction);

        // Assert
        assertFalse(_cell2.objects().contains(goat));
    }

    @Test
    void notRemoveItselfFromLastCell_whenMoveNotSuccessful() {
        // Arrange
        var goat = spy(new Goat(_stepCounter));
        goat.setPosition(_cell1);
        // Намеренно запрещаем козе куда-либо передвигаться
        when(goat.canMoveTo(_direction)).thenReturn(false);

        // Act
        goat.move(_direction);

        // Assert
        assertTrue(_cell1.objects().contains(goat));
    }

    @Test
    void beSolid() {
        // Arrange & Act & Assert
        assertTrue(new Goat(_stepCounter).isSolid());
    }

    @Test
    void haveGivenStepCounter() {
        // Arrange
        var goat = new Goat(_stepCounter);

        // Act & Assert
        assertSame(_stepCounter, goat.stepCounter());
    }
}