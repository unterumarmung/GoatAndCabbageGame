package model;

import events.MessageSender;
import model.exceptions.IllegalDimensionException;
import model.exceptions.PointIsNotInFieldRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Pair;
import utils.Point;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameFieldShould {
    private int _width;
    private int _height;
    private Point _exitPoint;
    private MessageSender _messageSender;

    @BeforeEach
    void beforeEach() {
        _width = 2;
        _height = 2;
        _exitPoint = new Point(1, 1);
        _messageSender = mock(MessageSender.class);
    }

    @Test
    void connectCellsCorrectly() {
        // Arrange & Act
        var field = new GameField(_width, _height, _exitPoint, _messageSender);
        var cell_00 = field.cell(new Point(0, 0));
        var cell_01 = field.cell(new Point(1, 0));
        var cell_10 = field.cell(new Point(0, 1));
        var cell_11 = field.cell(new Point(1, 1));

        // Assert
        assertEquals(Direction.SOUTH, cell_00.isNeighbor(cell_10));
        assertEquals(Direction.SOUTH, cell_01.isNeighbor(cell_11));
        assertEquals(Direction.NORTH, cell_11.isNeighbor(cell_01));
        assertEquals(Direction.NORTH, cell_10.isNeighbor(cell_00));
        assertEquals(Direction.EAST, cell_00.isNeighbor(cell_01));
        assertEquals(Direction.EAST, cell_10.isNeighbor(cell_11));
        assertEquals(Direction.WEST, cell_01.isNeighbor(cell_00));
        assertEquals(Direction.WEST, cell_11.isNeighbor(cell_10));
    }

    @Test
    void insertCabbageToExitPointCell() {
        // Arrange & Act
        var field = new GameField(_width, _height, _exitPoint, _messageSender);
        var exitPointCell = field.cell(_exitPoint);

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
            assertThrows(IllegalDimensionException.class, () -> new GameField(dimension.first, dimension.second, _exitPoint, _messageSender));
        }
    }

    @Test
    void throw_whenExitPointIsOutOfField() {
        // Arrange
        var exitPoints = Arrays.asList(
                new Point(_width, _height - 1),
                new Point(_width - 1, _height),
                new Point(_width, _height),
                new Point(-1, 0),
                new Point(0, -1),
                new Point(-1, -1)
        );

        // Act & Assert
        for (var exitPoint : exitPoints)
            assertThrows(PointIsNotInFieldRangeException.class, () -> new GameField(_width, _height, exitPoint, _messageSender));
    }

    @Test
    void haveGivenWidth() {
        // Arrange
        var field = new GameField(_width, _height, _exitPoint, _messageSender);

        // Act & Assert
        assertEquals(_width, field.width());
    }

    @Test
    void haveGivenHeight() {
        // Arrange
        var field = new GameField(_width, _height, _exitPoint, _messageSender);

        // Act & Assert
        assertEquals(_height, field.height());
    }

    @Test
    void haveActualGoat() {
        // Arrange
        var goat = new Goat(mock(StepCounter.class));
        var field = new GameField(4, 5, _exitPoint, _messageSender);

        assertNull(field.goat());
        for (int y = 0; y < field.height(); ++y) {
            for (int x = 0; x < field.width(); ++x) {
                goat.setPosition(field.cell(new Point(x, y)));
                // Act & Assert
                assertSame(goat, field.goat());
            }
        }
    }

    @Test
    void returnNull_whenRequestedCellDoesntExist() {
        // Arrange
        var points = Arrays.asList(
                new Point(_width, _height - 1),
                new Point(_width - 1, _height),
                new Point(_width, _height),
                new Point(-1, 0),
                new Point(0, -1),
                new Point(-1, -1)
        );
        var field = new GameField(_width, _height, _exitPoint, _messageSender);
        // Act & Assert
        for (var point : points)
            assertNull(field.cell(point));
    }
}