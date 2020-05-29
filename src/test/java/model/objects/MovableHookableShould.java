package model.objects;

import events.MessageSender;
import model.GameField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Pair;
import utils.Point;
import utils.collections.ReadOnlyList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class MovableHookableShould {
    private MovableHookable obj1;
    private MovableHookable obj2;
    private MovableHookable obj3;
    private GameField gameField;

    @BeforeEach
    void beforeEach() {
        gameField = new GameField(10, 10, new Point(0, 0), mock(MessageSender.class));
        var cell1 = gameField.cell(new Point(2, 2));
        var cell2 = gameField.cell(new Point(3, 2));
        var cell3 = gameField.cell(new Point(4, 2));

        obj1 = mock(MovableHookable.class, withSettings().useConstructor(cell1).defaultAnswer(CALLS_REAL_METHODS));
        obj2 = mock(MovableHookable.class, withSettings().useConstructor(cell2).defaultAnswer(CALLS_REAL_METHODS));
        obj3 = mock(MovableHookable.class, withSettings().useConstructor(cell3).defaultAnswer(CALLS_REAL_METHODS));

        when(obj1.hookedObjects()).thenReturn(ReadOnlyList.of(new Pair<>(obj2, Direction.EAST)));
        when(obj2.hookedObjects()).thenReturn(ReadOnlyList.of(new Pair<>(obj1, Direction.WEST), new Pair<>(obj3, Direction.EAST)));
        when(obj3.hookedObjects()).thenReturn(ReadOnlyList.of(new Pair<>(obj2, Direction.WEST)));

        when(obj1.canMoveToIndependent(any())).thenReturn(true);
        when(obj2.canMoveToIndependent(any())).thenReturn(true);
        when(obj3.canMoveToIndependent(any())).thenReturn(true);

        when(obj1.canReplace(obj2, Direction.EAST)).thenReturn(true);
        when(obj2.canReplace(obj1, Direction.WEST)).thenReturn(true);
        when(obj2.canReplace(obj3, Direction.EAST)).thenReturn(true);
        when(obj3.canReplace(obj2, Direction.WEST)).thenReturn(true);
    }

    @Test
    void moveOnlyOnce() {
        obj1.move(Direction.WEST);

        verify(obj1, atMostOnce()).setCell(any());
        verify(obj2, atMostOnce()).setCell(any());
        verify(obj3, atMostOnce()).setCell(any());
    }

    @Test
    void notMove_whenOppositeCannotReplace() {
        when(obj2.canReplace(obj1, Direction.WEST)).thenReturn(false);

        var result = obj1.move(Direction.WEST);

        assertFalse(result);
    }

    @Test
    void notMove_whenLeadingCant() {
        when(obj1.canMoveToIndependent(Direction.WEST)).thenReturn(false);

        var result = obj2.move(Direction.WEST);

        assertFalse(result);
    }

    @Test
    void notMove_whenHasNoMovable() {
        var obj4 = mock(HookableObject.class);
        when(obj3.hookedObjects()).thenReturn(ReadOnlyList.of(new Pair<>(obj2, Direction.WEST), new Pair<>(obj4, Direction.SOUTH)));

        var result = obj1.move(Direction.WEST);

        assertFalse(result);
    }
}