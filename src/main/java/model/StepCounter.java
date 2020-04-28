package model;

import model.exceptions.NoEnoughStepsException;

public class StepCounter {
    private int steps;

    public StepCounter(int steps) {
        this.steps = steps;
    }

    public int steps() {
        return steps;
    }

    public void decrease(int stepsToDecrease) {
        assertHasSteps(stepsToDecrease);
        steps -= stepsToDecrease;
    }

    private void assertHasSteps(int stepsToDecrease) {
        if (steps - stepsToDecrease < 0)
            throw new NoEnoughStepsException();
    }
}
