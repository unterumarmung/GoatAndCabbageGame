package model;

import events.MessageSender;
import events.SubscriptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Point;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class GameShould {
    private FieldBuilder fieldBuilder;
    private SubscriptionHandler subscriptionHandler;
    private MessageSender messageSender;
    private GameField gameField;

    @BeforeEach
    void beforeEach() {
        messageSender = mock(MessageSender.class);
        gameField = new GameField(5, 5, new Point(1, 1), messageSender);

        fieldBuilder = mock(FieldBuilder.class);
        when(fieldBuilder.build()).thenReturn(gameField);

        subscriptionHandler = mock(SubscriptionHandler.class);

    }

    @Test
    void beContinuing_afterCreation() {
        // Arrange
        var game = new Game(fieldBuilder, subscriptionHandler, messageSender);

        // Act & Assert
        assertEquals(GameState.CONTINUING, game.gameState());
    }

    @Test
    void buildField_usingBuilder() {
        // Arrange
        when(fieldBuilder.build()).thenReturn(gameField);
        var game = new Game(fieldBuilder, subscriptionHandler, messageSender);

        // Act & Assert
        verify(fieldBuilder).build();
        assertSame(gameField, game.gameField());
    }

    @Test
    void subscribeToEachCell_whenCreated() {
        var game = new Game(fieldBuilder, subscriptionHandler, messageSender);

        for (var cellWithPosition : gameField.cells()) {
            verify(subscriptionHandler).subscribeTo(cellWithPosition.cell, game);
        }
    }
}