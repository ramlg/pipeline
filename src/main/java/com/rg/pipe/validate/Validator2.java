package com.rg.pipe.validate;

@FunctionalInterface
public interface Validator2<T1, T2, E extends Errors> {
    E validate(T1 value1, T2 value2);
}
