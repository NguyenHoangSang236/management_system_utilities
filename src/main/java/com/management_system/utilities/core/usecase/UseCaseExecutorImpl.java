package com.management_system.utilities.core.usecase;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class UseCaseExecutorImpl implements UseCaseExecutor {
    @Override
    public <Any, In extends UseCase.InputValue, Out extends UseCase.OutputValue> CompletableFuture<Any> execute(
            UseCase<In, Out> useCase,
            In input,
            Function<Out, Any> outputMapper
    ) {
        return CompletableFuture
                .supplyAsync(() -> input)
                .thenApply(useCase::execute)
                .thenApply(outputMapper);
    }
}
