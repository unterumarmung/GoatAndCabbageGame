package model;

import events.MessageSender;
import model.events.CellMessage;
import model.exceptions.CellAlreadyHasNeighborForDirectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CellShould {
    private MessageSender _messageSender;
    private Cell _neighborCell;
    private Direction _direction;
    private GameObject _gameObject;

    @BeforeEach
    private void beforeEach() {
        _messageSender = mock(MessageSender.class);
        _neighborCell = mock(Cell.class);
        _direction = Direction.NORTH;
        _gameObject = mock(GameObject.class);
    }

    @Test
    void addNeighbor_whenNoNeighborInDirection() {
        // Arrange
        var cell = new Cell(_messageSender);

        // Act
        cell.setNeighbor(_neighborCell, _direction);

        // Assert
        assertEquals(_direction, cell.isNeighbor(_neighborCell));
        assertSame(_neighborCell, cell.neighborCell(_direction));
    }

    @Test
    void throw_whenAddNewNeighborInUsedDirection() {
        // Arrange
        var cell = new Cell(_messageSender);
        cell.setNeighbor(_neighborCell, _direction);
        var newCell = mock(Cell.class);

        // Act & Assert
        assertThrows(CellAlreadyHasNeighborForDirectionException.class, () -> cell.setNeighbor(newCell, _direction));
    }

    @Test
    void notThrow_whenAddSameNeighborInUsedDirection() {
        // Arrange
        var cell = new Cell(_messageSender);
        cell.setNeighbor(_neighborCell, _direction);

        // Act & Assert
        assertDoesNotThrow(() -> cell.setNeighbor(_neighborCell, _direction));
    }

    @Test
    void setItselfAsNeighbor_whenNewNeighborAdded() {
        // Arrange
        var cell = new Cell(_messageSender);

        // Act
        cell.setNeighbor(_neighborCell, _direction);

        // Assert
        verify(_neighborCell).setNeighbor(cell, _direction.opposite());
    }

    @Test
    void addObject() {
        // Arrange
        var cell = new Cell(_messageSender);

        // Act
        cell.addObject(_gameObject);

        // Assert
        assertTrue(cell.objects().contains(_gameObject));
    }

    @Test
    void removeObject() {
        // Arrange
        var cell = new Cell(_messageSender);
        cell.addObject(_gameObject);

        // Act
        cell.removeObject(_gameObject);

        // Assert
        assertFalse(cell.objects().contains(_gameObject));
    }

    @Test
    void sendMessage_whenObjectAdded() {
        // Arrange
        var cell = new Cell(_messageSender);
        var messageToBeSent = new CellMessage(CellMessage.Type.OBJECT_ENTERED, cell, _gameObject);
        // Act
        cell.addObject(_gameObject);

        // Assert
        verify(_messageSender).emitMessage(cell, messageToBeSent);
    }

    @Test
    void sendMessage_whenObjectRemoved() {
        // Arrange
        var cell = new Cell(_messageSender);
        var messageToBeSent = new CellMessage(CellMessage.Type.OBJECT_LEAVED, cell, _gameObject);
        cell.addObject(_gameObject);
        // Act
        cell.removeObject(_gameObject);

        // Assert
        verify(_messageSender).emitMessage(cell, messageToBeSent);
    }
}