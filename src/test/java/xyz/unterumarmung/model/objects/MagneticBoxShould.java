package xyz.unterumarmung.model.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.unterumarmung.events.MessageSender;
import xyz.unterumarmung.model.Cell;
import xyz.unterumarmung.model.GameField;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.Point;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static xyz.unterumarmung.model.objects.MagneticBox.Alignment;

class MagneticBoxShould {
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
        var box = new MagneticBox(initialCell);

        // Act & Assert
        assertTrue(box.isSolid());
    }

    @Test
    void beAbleToMove_toEmptyCell() {
        // Arrange
        var box = new MagneticBox(initialCell);

        // Act && Assert
        assertTrue(box.canMoveTo(Direction.EAST));
    }

    @Test
    void beAbleToMove_toCellWithNotSolidObject() {
        // Arrange
        var box = new MagneticBox(initialCell);
        gameField.cell(new Point(1, 0)).addObject(notSolidGameObject);

        // Act & Assert
        assertTrue(box.canMoveTo(Direction.EAST));
    }

    @Test
    void notBeAbleToMove_toCellWithSolidObject() {
        // Arrange
        var box = new MagneticBox(initialCell);
        gameField.cell(new Point(1, 0)).addObject(solidObject);

        // Act & Assert
        assertFalse(box.canMoveTo(Direction.EAST));
    }

    @Test
    void notBeAbleToMove_whenHaveSamePoleInNeighbor() {
        // Arrange
        var cell = gameField.cell(new Point(2, 3));
        var box = new MagneticBox(cell);

        var moveToDirection = Direction.NORTH;
        var moveToCell = cell.neighborCell(moveToDirection);

        var moveToCellNeighbours = moveToCell.neighbours().stream()
                .filter(cellWithDirection -> cellWithDirection.direction != moveToDirection.opposite())
                .collect(Collectors.toList());

        // Act & Assert
        for (var neighbourCell : moveToCellNeighbours) {
            var magnetic = mock(MagneticObject.class);
            var poleDirection = neighbourCell.direction.opposite();
            when(magnetic.magneticPoles()).thenReturn(Map.of(poleDirection, box.magneticPoles().get(poleDirection)));
            neighbourCell.cell.addObject(magnetic);

            assertFalse(box.canMoveTo(moveToDirection));
        }
    }

    @Test
    void notBeAbleToReplace_whenHaveSamePoleInNeighbor() {
        // Arrange
        var cell = gameField.cell(new Point(2, 3));
        var box = new MagneticBox(cell);

        var moveToDirection = Direction.NORTH;
        var moveToCell = cell.neighborCell(moveToDirection);
        moveToCell.addObject(solidObject);
        var moveToCellNeighbours = moveToCell.neighbours().stream()
                .filter(cellWithDirection -> cellWithDirection.direction != moveToDirection.opposite())
                .filter(cellWithDirection -> cellWithDirection.direction != moveToDirection)
                .collect(Collectors.toList());

        // Act & Assert
        for (var neighbourCell : moveToCellNeighbours) {
            var magnetic = mock(MagneticObject.class);
            var poleDirection = neighbourCell.direction.opposite();
            when(magnetic.magneticPoles()).thenReturn(Map.of(poleDirection, box.magneticPoles().get(poleDirection)));
            neighbourCell.cell.addObject(magnetic);

            assertFalse(box.canReplace(solidObject, moveToDirection));
        }
    }

    @Test
    void beMagnitable_whenPolesMatch() {
        var box = new MagneticBox(initialCell);

        for (var direction : Direction.all()) {
            var magneticObject = mock(MagneticObject.class);
            var poleDirection = direction.opposite();
            var pole = box.magneticPoles().get(poleDirection).opposite();
            when(magneticObject.magneticPoles()).thenReturn(Map.of(poleDirection, pole));

            assertTrue(box.isMagnitableTo(magneticObject, direction));
        }
    }

    @Test
    void notBeMagnitable_whenPolesNotMatch() {
        var box = new MagneticBox(initialCell);

        for (var direction : Direction.all()) {
            var magneticObject = mock(MagneticObject.class);
            var poleDirection = direction.opposite();
            var pole = box.magneticPoles().get(poleDirection);
            when(magneticObject.magneticPoles()).thenReturn(Map.of(poleDirection, pole));

            assertFalse(box.isMagnitableTo(magneticObject, direction));
        }

        for (var direction : Direction.all()) {
            var magneticObject = mock(MagneticObject.class);
            assertFalse(box.isMagnitableTo(magneticObject, direction));
        }

        for (var direction : Direction.all()) {
            var magneticObject = mock(MagneticObject.class);
            var poleDirection = direction.opposite();
            var pole = MagneticPole.NORTH;
            when(magneticObject.magneticPoles()).thenReturn(Map.of(poleDirection, pole));

            var spyBox = spy(box);
            when(spyBox.magneticPoles()).thenReturn(new HashMap<>());

            assertFalse(spyBox.isMagnitableTo(magneticObject, direction));
        }
    }

    @Test
    void addItselfToCellObjects() {
        // Arrange
        var box = new MagneticBox(initialCell);

        // Act & Assert
        assertTrue(initialCell.objects().contains(box));
    }

    @Test
    void beAbleToReplaceSolidObject() {
        // Arrange
        var box = new MagneticBox(initialCell);
        gameField.cell(new Point(1, 0)).addObject(solidObject);

        // Act & Assert
        assertTrue(box.canReplace(solidObject, Direction.EAST));
    }

    @Test
    void haveNoHooked_whenNoCell() {
        // Arrange
        var box = new MagneticBox(null);

        // Act
        var hookedObjects = box.hookedObjects();

        // Assert
        assertTrue(hookedObjects.isEmpty());
    }

    @Test
    void haveCorrectPoles_forAlignment() {
        var alignment1 = Alignment.VERTICAL_NORTH_HORIZONTAL_SOUTH;
        var alignment2 = Alignment.VERTICAL_SOUTH_HORIZONTAL_NORTH;
        var magneticBox1 = new MagneticBox(initialCell, alignment1);
        var magneticBox2 = new MagneticBox(gameField.cell(new Point(3, 3)), alignment2);

        assertEquals(mapForAlignment(alignment1), magneticBox1.magneticPoles());
        assertEquals(mapForAlignment(alignment2), magneticBox2.magneticPoles());
    }

    private Map<Direction, MagneticPole> mapForAlignment(Alignment alignment) {
        var map = new EnumMap<Direction, MagneticPole>(Direction.class);
        switch (alignment) {
            case VERTICAL_NORTH_HORIZONTAL_SOUTH -> {
                map.put(Direction.NORTH, MagneticPole.NORTH);
                map.put(Direction.EAST, MagneticPole.SOUTH);
                map.put(Direction.SOUTH, MagneticPole.NORTH);
                map.put(Direction.WEST, MagneticPole.SOUTH);
            }
            case VERTICAL_SOUTH_HORIZONTAL_NORTH -> {
                map.put(Direction.NORTH, MagneticPole.SOUTH);
                map.put(Direction.EAST, MagneticPole.NORTH);
                map.put(Direction.SOUTH, MagneticPole.SOUTH);
                map.put(Direction.WEST, MagneticPole.NORTH);
            }
        }

        return unmodifiableMap(map);
    }
}