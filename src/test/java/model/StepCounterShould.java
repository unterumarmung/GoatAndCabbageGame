package model;

import model.exceptions.NoEnoughStepsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StepCounterShould {
    @Test
    void initializeWithGivenSteps() {
        // Arrange
        var steps = 15;

        // Act
        var stepCounter = new StepCounter(steps);

        // Assert
        assertEquals(steps, stepCounter.steps());
    }

    @Test
    void decreaseCount_whenEnoughSteps() {
        // Arrange
        var initialSteps = 20;
        var stepToDecrease = 5;
        var stepCounter = new StepCounter(initialSteps);

        // Act
        stepCounter.decrease(stepToDecrease);

        // Assert
        assertEquals(initialSteps - stepToDecrease, stepCounter.steps());
    }

    @Test
    void throw_whenNotEnoughSteps() {
        // Arrange
        var initialSteps = 4;
        var stepToDecrease = 5;
        var stepCounter = new StepCounter(initialSteps);

        // Act & Assert
        assertThrows(NoEnoughStepsException.class, () -> stepCounter.decrease(stepToDecrease));
    }
}