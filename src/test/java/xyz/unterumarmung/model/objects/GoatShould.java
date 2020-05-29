package xyz.unterumarmung.model.objects;

import xyz.unterumarmung.events.MessageSender;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.model.GameField;
import xyz.unterumarmung.model.events.GoatMessage;
import xyz.unterumarmung.model.exceptions.NoEnoughStepsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.Pair;
import xyz.unterumarmung.utils.Point;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoatShould {
    private Cell cell1;
    private Cell cell2;
    private Direction direction;
    private GameObject solidGameObject;
    private GameObject notSolidGameObject;
    private Box box;
    private GameField gameField5x5;
    private MessageSender messageSender;

    @BeforeEach
    void beforeEach() {
        // Создание клеток поля для имитации движения козы
        messageSender = mock(MessageSender.class);
        var gameField = new GameField(2, 1, new Point(1, 0), messageSender);
        cell1 = gameField.cell(new Point(0, 0));
        cell2 = gameField.cell(new Point(1, 0));
        direction = Direction.EAST;

        // Настройка поведения мок объекта, который ведёт себя как твёрдый объект
        solidGameObject = mock(GameObject.class);
        when(solidGameObject.isSolid()).thenReturn(true);

        // Настройка поведения мок объекта, который ведёт себя как нетвёрдый объект
        notSolidGameObject = mock(GameObject.class);
        when(notSolidGameObject.isSolid()).thenReturn(false);

        box = mock(Box.class);
        gameField5x5 = new GameField(5, 5, new Point(4, 4), messageSender);
    }

    @Test
    void haveEnoughSteps_whenStepsHasLargerOrEqualCount() {
        // Arrange
        var goat1 = new Goat(Goat.STEP_COST * 4, cell1, messageSender);
        var goat2 = new Goat(Goat.STEP_COST + 1, cell1, messageSender);
        var goat3 = new Goat(Goat.STEP_COST, cell1, messageSender);

        // Act & Assert
        assertTrue(goat1.hasEnoughSteps());
        assertTrue(goat2.hasEnoughSteps());
        assertTrue(goat3.hasEnoughSteps());
    }

    @Test
    void notHaveEnoughSteps_whenStepCounterHasSmallerThanStepCost() {
        // Arrange
        var goat = new Goat(Goat.STEP_COST - 1, cell1, messageSender);

        // Act & Assert
        assertFalse(goat.hasEnoughSteps());
    }

    @Test
    void move_toEmptyCell() {
        // Arrange
        var goat = new Goat(Goat.STEP_COST * 4, cell1, messageSender);

        // Act
        goat.move(direction);

        // Assert
        assertSame(cell2, goat.cell());
    }

    @Test
    void move_toCellWithNotSolidObject() {
        // Arrange
        var goat = new Goat(Goat.STEP_COST * 4, cell1, messageSender);
        cell2.addObject(notSolidGameObject);

        // Act
        goat.move(direction);

        // Assert
        assertSame(cell2, goat.cell());
    }

    @Test
    void notMove_toCellWithSolidObject() {
        // Arrange
        var goat = new Goat(Goat.STEP_COST * 4, cell1, messageSender);
        cell2.addObject(solidGameObject);
        // Act
        goat.move(direction);

        // Assert
        assertSame(cell1, goat.cell());
    }

    @Test
    void addItselfToNewCell_whenMoveSuccessful() {
        // Arrange
        var goat = new Goat(Goat.STEP_COST * 4, cell1, messageSender);
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
        var goat = new Goat(Goat.STEP_COST * 4, cell1, messageSender);
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
        var goat = spy(new Goat(Goat.STEP_COST * 4, cell1, messageSender));

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
        var goat = new Goat(Goat.STEP_COST * 4, cell1, messageSender);
        var spyGoat = spy(goat);
        // Намеренно запрещаем козе куда-либо передвигаться
        when(spyGoat.canMoveTo(direction)).thenReturn(false);

        // Act
        spyGoat.move(direction);

        // Assert
        var objects = cell1.objects();
        assertTrue(objects.contains(goat));
    }

    @Test
    void beSolid() {
        // Arrange & Act & Assert
        assertTrue(new Goat(Goat.STEP_COST * 4, cell1, messageSender).isSolid());
    }

    @Test
    void initializeWithGivenSteps() {
        // Arrange
        var steps = 15;

        // Act
        var goat = new Goat(steps, cell1, messageSender);

        // Assert
        assertEquals(steps, goat.steps());
    }

    @Test
    void throw_whenNotEnoughSteps() {
        // Arrange
        var initialSteps = 0;
        var goat = new Goat(initialSteps, cell1, messageSender);

        // Act & Assert
        assertThrows(NoEnoughStepsException.class, goat::decreaseSteps);
    }

    @Test
    void hookBox_whenInDirection() {
        // Arrange
        var goat = new Goat(10, gameField5x5.cell(new Point(0, 0)), messageSender);
        gameField5x5.cell(new Point(1, 0)).addObject(box);

        // Act
        var result = goat.hookBox(Direction.EAST);

        // Assert
        assertTrue(result);
        assertTrue(goat.hookedObjects().stream().anyMatch(pair -> pair.equals(new Pair<>(box, Direction.EAST))));
    }

    @Test
    void notHookBox_whenNoInDirection() {
        // Arrange
        var goat = new Goat(10, gameField5x5.cell(new Point(0, 0)), messageSender);

        // Act
        var result = goat.hookBox(Direction.EAST);

        // Assert
        assertFalse(result);
        assertTrue(goat.hookedObjects().isEmpty());
    }

    @Test
    void sendMessage_whenMoved() {
        // Arrange
        var goat = new Goat(10, gameField5x5.cell(new Point(0, 0)), messageSender);

        // Act
        goat.move(Direction.EAST);
        var expectedMessage = new GoatMessage(gameField5x5.cell(new Point(0, 0)), gameField5x5.cell(new Point(1, 0)));

        // Assert
        verify(messageSender).emitMessage(eq(goat), eq(expectedMessage));
    }

    @Test
    void notSendMessage_whenNotMoved() {
        // Arrange
        var goat = new Goat(10, gameField5x5.cell(new Point(0, 0)), messageSender);
        gameField5x5.cell(new Point(1, 0)).addObject(solidGameObject);

        // Act
        goat.move(Direction.EAST);

        // Assert
        verify(messageSender, never()).emitMessage(eq(goat), any());
    }

    @Test
    void beAbleToReplaceSolidObject() {
        // Arrange
        var box = new Goat(10, gameField5x5.cell(new Point(0, 0)), messageSender);
        gameField5x5.cell(new Point(1, 0)).addObject(solidGameObject);

        // Act & Assert
        assertTrue(box.canReplace(solidGameObject, Direction.EAST));
    }

    @Test
    void unhookBox() {
        // Arrange
        var goat = new Goat(10, gameField5x5.cell(new Point(0, 0)), messageSender);
        gameField5x5.cell(new Point(1, 0)).addObject(box);

        // Act
        goat.hookBox(Direction.EAST);
        goat.unhookBox();

        // Assert
        assertTrue(goat.hookedObjects().isEmpty());
    }
}