package model;

import events.MessageSender;
import model.exceptions.IllegalDimensionException;
import model.exceptions.PointIsNotInFieldRangeException;
import model.objects.Cabbage;
import model.objects.Goat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Pair;
import utils.Point;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameFieldShould {
    private int width;
    private int height;
    private Point exitPoint;
    private MessageSender messageSender;

    @BeforeEach
    void beforeEach() {
        width = 2;
        height = 2;
        exitPoint = new Point(1, 1);
        messageSender = mock(MessageSender.class);
    }

    @Test
    void connectCellsCorrectly() {
        // Arrange & Act
        var field = new GameField(width, height, exitPoint, messageSender);
        var cell_00 = field.cell(new Point(0, 0));
        var cell_01 = field.cell(new Point(1, 0));
        var cell_10 = field.cell(new Point(0, 1));
        var cell_11 = field.cell(new Point(1, 1));

        // Assert
        assertEquals(Direction.SOUTH, cell_00.neighborDirection(cell_10));
        assertEquals(Direction.SOUTH, cell_01.neighborDirection(cell_11));
        assertEquals(Direction.NORTH, cell_11.neighborDirection(cell_01));
        assertEquals(Direction.NORTH, cell_10.neighborDirection(cell_00));
        assertEquals(Direction.EAST, cell_00.neighborDirection(cell_01));
        assertEquals(Direction.EAST, cell_10.neighborDirection(cell_11));
        assertEquals(Direction.WEST, cell_01.neighborDirection(cell_00));
        assertEquals(Direction.WEST, cell_11.neighborDirection(cell_10));
    }

    @Test
    void insertCabbageToExitPointCell() {
        // Arrange & Act
        var field = new GameField(width, height, exitPoint, messageSender);
        var exitPointCell = field.cell(exitPoint);

        // Assert
        assertTrue(exitPointCell.objects().stream().anyMatch(object -> object instanceof Cabbage));
    }

    @Test
    void throw_whenCreatedWithIncorrectDimensions() {
        // Arrange
        var dimensions = Arrays.asList(
                new Pair<>(0, 0),
                new Pair<>(-1, 0),
                new Pair<>(0, -1),
                new Pair<>(1, 0),
                new Pair<>(0, 1),
                new Pair<>(-1, 1),
                new Pair<>(1, -1)
        );

        // Act & Assert
        for (var dimension : dimensions) {
            assertThrows(IllegalDimensionException.class, () -> new GameField(dimension.first, dimension.second, exitPoint, messageSender));
        }
    }

    @Test
    void throw_whenExitPointIsOutOfField() {
        // Arrange
        var exitPoints = Arrays.asList(
                new Point(width, height - 1),
                new Point(width - 1, height),
                new Point(width, height),
                new Point(-1, 0),
                new Point(0, -1),
                new Point(-1, -1)
        );

        // Act & Assert
        for (var point : exitPoints)
            assertThrows(PointIsNotInFieldRangeException.class, () -> new GameField(width, height, point, messageSender));
    }

    @Test
    void haveGivenWidth() {
        // Arrange
        var field = new GameField(width, height, exitPoint, messageSender);

        // Act & Assert
        assertEquals(width, field.width());
    }

    @Test
    void haveGivenHeight() {
        // Arrange
        var field = new GameField(width, height, exitPoint, messageSender);

        // Act & Assert
        assertEquals(height, field.height());
    }

    @Test
    void haveActualGoat() {
        // Arrange
        var field = new GameField(4, 5, exitPoint, messageSender);
        assertNull(field.goat());
        var goat = new Goat(100, field.cell(new Point(0, 0)));
        assertSame(goat, field.goat());
    }

    @Test
    void returnNull_whenRequestedCellDoesntExist() {
        // Arrange
        var points = Arrays.asList(
                new Point(width, height - 1),
                new Point(width - 1, height),
                new Point(width, height),
                new Point(-1, 0),
                new Point(0, -1),
                new Point(-1, -1)
        );
        var field = new GameField(width, height, exitPoint, messageSender);
        // Act & Assert
        for (var point : points)
            assertNull(field.cell(point));
    }
}