package model;

import events.MessageBridge;
import events.MessageSender;
import events.SubscriptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Point;

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
        var game = new Game(fieldFactory, subscriptionHandler, messageSender);

        // Act
        game.start();

        // Assert
        assertEquals(GameState.CONTINUING, game.gameState());
    }

    @Test
    void subscribeToEachCell_whenStarted() {
        // Arrange
        var game = new Game(fieldFactory, subscriptionHandler, messageSender);

        // Act
        game.start();

        // Assert
        for (var cellWithPosition : gameField.cells()) {
            verify(subscriptionHandler).subscribeTo(cellWithPosition.cell, game);
        }
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
        var game = new Game(new SimpleFieldFactory(messageBridge), messageBridge, messageBridge);
        game.start();
        var goat = game.gameField().goat();

        // Act
        goat.setPosition(game.gameField().cell(game.gameField().exitPoint()));

        // Assert
        assertEquals(GameState.ENDED_SUCCESS_GOAT_REACHED_CABBAGE, game.gameState());
    }

    @Test
    void endWithFailure_whenStepCounterEnded() {
        var messageBridge = new MessageBridge();
        var game = new Game(new SimpleFieldFactory(messageBridge), messageBridge, messageBridge);
        game.start();
        var field = game.gameField();
        var goat = field.goat();
        var direction = Direction.EAST;
        decreaseStepCounterToStepCost(goat);

        // Act
        goat.move(direction);

        // Assert
        assertEquals(GameState.ENDED_FAILURE_STEPS_EXPIRED, game.gameState());
    }

    @Test
    void endWithSuccess_whenReachedCabbage_andStepCounterEnded() {
        // Arrange
        var messageBridge = new MessageBridge();
        var game = new Game(new SimpleFieldFactory(messageBridge), messageBridge, messageBridge);
        game.start();
        var field = game.gameField();
        var goat = field.goat();
        var neighborDirection = Direction.EAST;
        var exitCell = field.cell(field.exitPoint());
        goat.setPosition(exitCell.neighborCell(neighborDirection));
        decreaseStepCounterToStepCost(goat);

        // Act
        goat.move(neighborDirection.opposite());

        // Assert
        assertEquals(GameState.ENDED_SUCCESS_GOAT_REACHED_CABBAGE, game.gameState());
    }

    private void decreaseStepCounterToStepCost(Goat goat) {
        var decreaseTimes = goat.steps() / Goat.STEP_COST - 1;
        while (decreaseTimes != 0) {
            goat.decreaseSteps();
            decreaseTimes--;
        }
    }
}