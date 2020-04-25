package model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CabbageShould {
    @Test
    void addItselfToCellObjects() {
        // Arrange
        var cellMock = mock(Cell.class);

        // Act
        var cabbage = new Cabbage(cellMock);

        // Assert
        verify(cellMock).addObject(cabbage);
    }

    @Test
    void shouldNotBeSolid() {
        // Arrange
        var cellMock = mock(Cell.class);
        var cabbage = new Cabbage(cellMock);

        // Act & Assert
        assertFalse(cabbage.isSolid());
    }
}