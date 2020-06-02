package xyz.unterumarmung.model.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.unterumarmung.events.MessageSender;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.model.GameField;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.Pair;
import xyz.unterumarmung.utils.Point;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codepoetics.protonpack.StreamUtils.zip;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MetalBoxShould {
    private GameField gameField;
    private SolidObject solidObject;
    private GameObject notSolidGameObject;
    private Cell initialCell;

    @BeforeEach
    void beforeEach() {
        var messageSender = mock(MessageSender.class);
        gameField = new GameField(5, 5, new Point(0, 0), messageSender);

        solidObject = mock(SolidObject.class);
        when(solidObject.isSolid()).thenReturn(true);

        initialCell = gameField.cell(new Point(0, 0));

        // Настройка поведения мок объекта, который ведёт себя как нетвёрдый объект
        notSolidGameObject = mock(GameObject.class);
        when(notSolidGameObject.isSolid()).thenReturn(false);
    }

    @Test
    void beSolid() {
        // Arrange
        var box = new MetalBox(initialCell);

        // Act & Assert
        assertTrue(box.isSolid());
    }

    @Test
    void beAbleToMove_toEmptyCell() {
        // Arrange
        var box = new MetalBox(initialCell);

        // Act && Assert
        assertTrue(box.canMoveTo(Direction.EAST));
    }

    @Test
    void beAbleToMove_toCellWithNotSolidObject() {
        // Arrange
        var box = new MetalBox(initialCell);
        gameField.cell(new Point(1, 0)).addObject(notSolidGameObject);

        // Act & Assert
        assertTrue(box.canMoveTo(Direction.EAST));
    }

    @Test
    void notBeAbleToMove_toCellWithSolidObject() {
        // Arrange
        var box = new MetalBox(initialCell);
        gameField.cell(new Point(1, 0)).addObject(solidObject);

        // Act & Assert
        assertFalse(box.canMoveTo(Direction.EAST));
    }

    @Test
    void addItselfToCellObjects() {
        // Arrange
        var box = new MetalBox(initialCell);

        // Act & Assert
        assertTrue(initialCell.objects().contains(box));
    }

    @Test
    void beAbleToReplaceSolidObject() {
        // Arrange
        var box = new MetalBox(initialCell);
        gameField.cell(new Point(1, 0)).addObject(solidObject);

        // Act & Assert
        assertTrue(box.canReplace(solidObject, Direction.EAST));
    }

    @Test
    void beMagnitableAlways() {
        // Arrange
        var box = new MetalBox(initialCell);

        // Act & Assert
        assertTrue(box.isMagnitableTo(mock(MagneticObject.class), Direction.EAST));
        assertTrue(box.isMagnitableTo(mock(MagneticObject.class), Direction.WEST));
        assertTrue(box.isMagnitableTo(mock(MagneticObject.class), Direction.NORTH));
        assertTrue(box.isMagnitableTo(mock(MagneticObject.class), Direction.SOUTH));
    }

    @Test
    void hookToMagneticObjectsOnly() {
        // Arrange
        var cell = gameField.cell(new Point(2, 2));
        var box = new MetalBox(cell);
        var objects = Stream.of(mock(MagneticObject.class), mock(HookableObject.class),
                mock(SolidObject.class), mock(MagneticObject.class));
        var neighbourCells = cell.neighbours().stream().map(cellWithDirection -> cellWithDirection.cell);
        var cellToObject = zip(neighbourCells, objects, Pair::new).collect(Collectors.toList());
        for (var pair : cellToObject) {
            pair.first.addObject(pair.second);
        }

        // Act
        var hookedObjects = box.hookedObjects();

        // Assert
        assertTrue(hookedObjects.stream().map(pair -> pair.first).allMatch(object -> object instanceof MagneticObject));
        assertEquals(2, hookedObjects.size());
    }

    @Test
    void haveNoHooked_whenNoCell() {
        // Arrange
        var box = new MetalBox(null);

        // Act
        var hookedObjects = box.hookedObjects();

        // Assert
        assertTrue(hookedObjects.isEmpty());
    }
}