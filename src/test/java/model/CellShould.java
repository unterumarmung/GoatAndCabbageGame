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
    private MessageSender messageSender;
    private Cell neighborCell;
    private Direction direction;
    private GameObject gameObject;

    @BeforeEach
    void beforeEach() {
        messageSender = mock(MessageSender.class);
        neighborCell = mock(Cell.class);
        direction = Direction.NORTH;
        gameObject = mock(GameObject.class);
    }

    @Test
    void addNeighbor_whenNoNeighborInDirection() {
        // Arrange
        var cell = new Cell(messageSender);

        // Act
        cell.setNeighbor(neighborCell, direction);

        // Assert
        assertEquals(direction, cell.isNeighbor(neighborCell));
        assertSame(neighborCell, cell.neighborCell(direction));
    }

    @Test
    void throw_whenAddNewNeighborInUsedDirection() {
        // Arrange
        var cell = new Cell(messageSender);
        cell.setNeighbor(neighborCell, direction);
        var newCell = mock(Cell.class);

        // Act & Assert
        assertThrows(CellAlreadyHasNeighborForDirectionException.class, () -> cell.setNeighbor(newCell, direction));
    }

    @Test
    void notThrow_whenAddSameNeighborInUsedDirection() {
        // Arrange
        var cell = new Cell(messageSender);
        cell.setNeighbor(neighborCell, direction);

        // Act & Assert
        assertDoesNotThrow(() -> cell.setNeighbor(neighborCell, direction));
    }

    @Test
    void setItselfAsNeighbor_whenNewNeighborAdded() {
        // Arrange
        var cell = new Cell(messageSender);

        // Act
        cell.setNeighbor(neighborCell, direction);

        // Assert
        verify(neighborCell).setNeighbor(cell, direction.opposite());
    }

    @Test
    void addObject() {
        // Arrange
        var cell = new Cell(messageSender);

        // Act
        cell.addObject(gameObject);

        // Assert
        assertTrue(cell.objects().contains(gameObject));
    }

    @Test
    void removeObject() {
        // Arrange
        var cell = new Cell(messageSender);
        cell.addObject(gameObject);

        // Act
        cell.removeObject(gameObject);

        // Assert
        assertFalse(cell.objects().contains(gameObject));
    }

    @Test
    void sendMessage_whenObjectAdded() {
        // Arrange
        var cell = new Cell(messageSender);
        var messageToBeSent = new CellMessage(CellMessage.Type.OBJECT_ENTERED, cell, gameObject);
        // Act
        cell.addObject(gameObject);

        // Assert
        verify(messageSender).emitMessage(cell, messageToBeSent);
    }

    @Test
    void sendMessage_whenObjectRemoved() {
        // Arrange
        var cell = new Cell(messageSender);
        var messageToBeSent = new CellMessage(CellMessage.Type.OBJECT_LEAVED, cell, gameObject);
        cell.addObject(gameObject);
        // Act
        cell.removeObject(gameObject);

        // Assert
        verify(messageSender).emitMessage(cell, messageToBeSent);
    }

    @Test
    void beEmpty_afterCreation() {
        // Arrange
        var cell = new Cell(messageSender);

        // Act & Assert
        assertTrue(cell.objects().isEmpty());
    }

    @Test
    void returnNull_whenNoNeighbor() {
        // Arrange
        var cell = new Cell(messageSender);

        // Act & Assert
        assertNull(cell.neighborCell(Direction.EAST));
        assertNull(cell.isNeighbor(mock(Cell.class)));
    }
}