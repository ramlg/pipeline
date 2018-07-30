package com.rg.pipe;

import com.rg.pipe.validate.Errors;
import com.rg.pipe.validate.NoErrors;
import com.rg.pipe.validate.Validator;
import com.rg.pipe.validate.Validator2;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BiPipeline<T1, T2, E extends Errors> {

    private T1 value1;
    private T2 value2;
    private E errors;

    private BiPipeline() {}

    protected BiPipeline(T1 value1, T2 value2) {
        this.value1 = Objects.requireNonNull(value1);
        this.value2 = Objects.requireNonNull(value2);
        errors = (E)new NoErrors();

    }

    public BiPipeline(E errors) {
        this.errors = errors;
    }

    private BiPipeline(T1 value1, T2 value2, E errors) {
        this.value1 = Objects.requireNonNull(value1);
        this.value2 = Objects.requireNonNull(value2);
        if(errors != null) {
            this.errors = errors;
        }
    }

    public T1 _1() {
        if(value1 == null) {
            throw new NoSuchElementException("no value present");
        }
        return value1;
    }

    public T2 _2() {
        if(value2 == null) {
            throw new NoSuchElementException("no value present");
        }
        return value2;
    }

    public <U> Pipeline<U, E> map(BiFunction<? super T1,? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if(errors.hasErrors()) {
            return new Pipeline(errors);
        }
        return Pipeline.of(mapper.apply(value1, value2));
    }

    public  <E1 extends Errors> BiPipeline<T1,T2, E1> validate(Validator2<? super T1, ? super T2, E1> validator) {
        Objects.requireNonNull(validator);
        return new BiPipeline<>(value1, value2, validator.validate(value1, value2));
    }
}
