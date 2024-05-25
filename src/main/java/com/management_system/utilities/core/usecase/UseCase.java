package com.management_system.utilities.core.usecase;

public abstract class UseCase<In extends UseCase.InputValue, Out extends UseCase.OutputValue> {
    public abstract Out execute(In input);

    public interface InputValue {
    }

    public interface OutputValue {
    }
}
