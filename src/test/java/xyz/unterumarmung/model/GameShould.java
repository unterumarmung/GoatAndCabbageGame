package xyz.unterumarmung.model;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.unterumarmung.events.*;
import xyz.unterumarmung.model.objects.Goat;
import xyz.unterumarmung.utils.Direction;
import xyz.unterumarmung.utils.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class GameShould {
    private FieldFactory fieldFactory;
    private SubscriptionHandler subscriptionHandler;
    private MessageSender messageSender;
    private GameField gameField;

    @BeforeEach
    void beforeEach() {
        fieldFactory = mock(FieldFactory.class);
        messageSender = mock(MessageSender.class);
        gameField = new GameField(5, 5, new Point(1, 1), messageSender);
        subscriptionHandler = mock(SubscriptionHandler.class);
        when(fieldFactory.create()).thenReturn(gameField);
    }

    @Test
    void beContinuing_afterStart() {
        // Arrange
        var game = new Game(new SimpleFieldFactory(messageSender), subscriptionHandler, messageSender);

        // Act
        game.start();

        // Assert
        assertEquals(GameState.CONTINUING, game.gameState());
    }

    @Test
    void subscribeToGoat_whenStarted() {
        // Arrange
        var game = new Game(fieldFactory, subscriptionHandler, messageSender);

        // Act
        game.start();

        // Assert
        verify(subscriptionHandler).subscribeTo(game.gameField().goat(), game);
    }

    @Test
    void buildField_usingBuilder_afterStart() {
        // Arrange
        var game = new Game(fieldFactory, subscriptionHandler, messageSender);

        // Act
        game.start();

        // Assert
        verify(fieldFactory).create();
        assertSame(gameField, game.gameField());
    }

    @Test
    void endWithSuccess_whenGoatOnCabbageCell() {
        // Arrange
        var messageBridge = new MessageBridge();
        var game = new Game(new TestFactoryWithSteps(messageBridge, null, 20), messageBridge, messageBridge);
        game.start();
        var goat = game.gameField().goat();

        // Assert
        assertEquals(GameState.ENDED_SUCCESS_GOAT_REACHED_CABBAGE, game.gameState());
    }

    @Test
    void endWithFailure_whenStepCounterEnded() {
        var messageBridge = new MessageBridge();
        var game = new Game(new TestFactoryWithSteps(messageBridge, new Point(0, 0), 0), messageBridge, messageBridge);
        game.start();
        var field = game.gameField();
        var goat = field.goat();
        var direction = Direction.EAST;

        // Act
        goat.move(direction);

        // Assert
        assertEquals(GameState.ENDED_FAILURE_STEPS_EXPIRED, game.gameState());
    }

    @Test
    void endWithSuccess_whenReachedCabbage_andStepCounterEnded() {
        // Arrange
        var messageBridge = new MessageBridge();
        var game = new Game(new TestFactoryWithSteps(messageBridge, null, 0), messageBridge, messageBridge);
        game.start();
        var field = game.gameField();
        var goat = field.goat();
        var neighborDirection = Direction.EAST;
        var exitCell = field.cell(field.exitPoint());

        // Act
        goat.move(neighborDirection.opposite());

        // Assert
        assertEquals(GameState.ENDED_SUCCESS_GOAT_REACHED_CABBAGE, game.gameState());
    }

    @Test
    void emitMessage_afterHandling() {
        // Arrange
        var game = new Game(fieldFactory, subscriptionHandler, messageSender);

        // Act
        game.start();
        game.handleMessage(mock(MessageSource.class), new MessageData());

        // Assert
        verify(messageSender).emitMessage(eq(game), any());
    }

    static class TestFactoryWithSteps extends SimpleFieldFactory {
        private final Point initialGoatPoint;
        private final int steps;

        public TestFactoryWithSteps(@NotNull MessageSender messageSender, Point initialGoatPoint, int steps) {
            super(messageSender);
            this.initialGoatPoint = initialGoatPoint;
            this.steps = steps;
        }

        @Override
        protected void addGoat() {
            Point point;
            if (initialGoatPoint == null)
                point = super.exitPoint();
            else
                point = initialGoatPoint;
            var goat = new Goat(steps, gameField.cell(point), messageSender);
        }
    }
}