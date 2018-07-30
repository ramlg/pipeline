package com.rg.pipe;

import com.rg.pipe.validate.Errors;
import com.rg.pipe.validate.NoErrors;
import com.rg.pipe.validate.Validator;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

public class Pipeline<T, E extends Errors> {

    private T value;
    private E errors;

    protected Pipeline(E errors) { this.errors = errors; }

    private Pipeline(T value, E errors) {
        this.value = Objects.requireNonNull(value);
        if(errors != null) {
            this.errors = errors;
        }
    }

    public static <T, E1 extends Errors> Pipeline<T, E1> of(T value) { return new Pipeline<>(value, (E1)new NoErrors()); }

    public T get() {
        if(value == null) {
            throw new NoSuchElementException("no value present");
        }
        return value;
    }

    public boolean hasErrors() {
        return errors.hasErrors();
    }

    public <U, E1 extends Errors> Pipeline<U, E1> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if(errors.hasErrors()) {
            return new Pipeline(errors);
        }
        return Pipeline.of(mapper.apply(value));
    }

    public  <E1 extends Errors> Pipeline<T, E1> validate(Validator<? super T, E1> validator) {
        Objects.requireNonNull(validator);
        return new Pipeline<>(value, validator.validate(value));
    }

    public <U, E1 extends Errors> BiPipeline<T, U, E1> collect(Function<? super T, ? extends U> mapper) {

        Objects.requireNonNull(mapper);
        if(errors.hasErrors()) {
            return new BiPipeline<>((E1)errors);
        }
        return new BiPipeline<>(value, mapper.apply(value));
    }
}
