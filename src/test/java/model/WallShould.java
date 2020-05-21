package model;

import model.objects.Wall;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    @Test
    void haveGivenCell() {
        // Arrange
        var cellMock = mock(Cell.class);
        var wall = new Wall(cellMock);

        // Act & Assert
        assertSame(cellMock, wall.cell());
    }
}