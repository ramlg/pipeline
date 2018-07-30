package com.rg.pipe.validate;

@FunctionalInterface
public interface Validator<T, E extends Errors> {
    E validate(T value);
}
