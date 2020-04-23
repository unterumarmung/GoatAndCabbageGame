package model;

import model.exceptions.NoEnoughStepsException;

public class StepCounter {
    private int _steps;

    public int steps() {
        return _steps;
    }

    public StepCounter(int steps) {
        _steps = steps;
    }

    public void decrease(int stepsToDecrease) {
        assertHasSteps(stepsToDecrease);
        _steps -= stepsToDecrease;
    }

    private void assertHasSteps(int stepsToDecrease) {
        if (_steps - stepsToDecrease < 0)
            throw new NoEnoughStepsException();
    }
}
