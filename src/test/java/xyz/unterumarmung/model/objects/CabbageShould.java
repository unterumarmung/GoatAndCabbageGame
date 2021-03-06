package xyz.unterumarmung.model.objects;

import org.junit.jupiter.api.Test;
import xyz.unterumarmung.model.Cell;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

    @Test
    void haveGivenCell() {
        // Arrange
        var cellMock = mock(Cell.class);
        var cabbage = new Cabbage(cellMock);

        // Act & Assert
        assertSame(cellMock, cabbage.cell());
    }
}