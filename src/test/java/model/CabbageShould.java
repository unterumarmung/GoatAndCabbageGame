package model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CabbageShould {
    @Test
    void addItselfToCellObjects() {
        // Arrange
        var cellMock = Mockito.mock(Cell.class);

        // Act
        var cabbage = new Cabbage(cellMock);

        // Assert
        Mockito.verify(cellMock).addObject(cabbage);
    }

    @Test
    void shouldNotBeSolid() {
        // Arrange
        var cellMock = Mockito.mock(Cell.class);
        var cabbage = new Cabbage(cellMock);

        // Act & Assert
        assertFalse(cabbage.isSolid());
    }
}