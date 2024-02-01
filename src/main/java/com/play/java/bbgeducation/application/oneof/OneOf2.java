package com.play.java.bbgeducation.application.oneof;

import java.util.Optional;
import java.util.function.Function;

public class OneOf2<T1,T2> {
    private final Optional<T1> _opt1;
    private final Optional<T2> _opt2;

    public OneOf2(T1 option1, T2 option2){
        this._opt1 = Optional.ofNullable(option1);
        this._opt2 = Optional.ofNullable(option2);

        if (this._opt1.isEmpty() && this._opt2.isEmpty()){
            throw new IllegalArgumentException("OneOf must contain at least one option");
        }

        if (this._opt1.isPresent() && this._opt2.isPresent()){
            throw new IllegalArgumentException("OneOf may only contain one option");
        }
    }

    public static<T1, T2> OneOf2<T1, T2> fromOption1 (final T1 option){
        return new OneOf2<>(option, null);
    }

    public static<T1, T2> OneOf2<T1, T2> fromOption2 (final T2 option){
        return new OneOf2<>(null,option);
    }

    public boolean hasOption1(){
        return _opt1.isPresent();
    }

    public boolean hasOption2(){
        return _opt2.isPresent();
    }

    public T1 asOption1(){
        if (_opt1.isEmpty()){
            throw new RuntimeException("Option 1 has no value");
        }
        return _opt1.get();
    }

    public T2 asOption2(){
        if (_opt2.isEmpty()){
            throw new RuntimeException("Option 2 has no value");
        }
        return _opt2.get();
    }
    public <R> R match(final Function<T1,R> mapper1, final Function<T2,R> mapper2)
    {
        return _opt1.map(mapper1)
                .orElseGet(() -> _opt2.map(mapper2)
                    .orElseThrow(() -> new RuntimeException("All values are null")
                    )
                );
    }


}
