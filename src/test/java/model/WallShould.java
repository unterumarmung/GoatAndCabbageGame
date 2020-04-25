package model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WallShould {
    @Test
    void addItselfToCellObjects() {
        // Arrange
        var cellMock = mock(Cell.class);

        // Act
        var wall = new Wall(cellMock);

        // Assert
        verify(cellMock).addObject(wall);
    }

    @Test
    void beSolid() {
        // Arrange
        var cellMock = mock(Cell.class);
        var wall = new Wall(cellMock);

        // Act & Assert
        assertTrue(wall.isSolid());
    }
}