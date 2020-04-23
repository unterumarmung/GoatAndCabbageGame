package model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class WallShould {
    @Test
    void addItselfToCellObjects() {
        // Arrange
        var cellMock = Mockito.mock(Cell.class);

        // Act
        var wall = new Wall(cellMock);

        // Assert
        Mockito.verify(cellMock).addObject(wall);
    }

    @Test
    void beSolid() {
        // Arrange
        var cellMock = Mockito.mock(Cell.class);
        var wall = new Wall(cellMock);

        // Act & Assert
        assertTrue(wall.isSolid());
    }
}