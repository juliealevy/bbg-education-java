package com.play.java.bbgeducation.application.oneof;

import java.util.Optional;
import java.util.function.Function;

public class OneOf3 <T1,T2,T3>{

    private final Optional<T1> _opt1;
    private final Optional<T2> _opt2;
    private final Optional<T3> _opt3;

    private OneOf3(T1 option1, T2 option2, T3 option3){
        this._opt1 = Optional.ofNullable(option1);
        this._opt2 = Optional.ofNullable(option2);
        this._opt3 = Optional.ofNullable(option3);

        //not sure these conditions can happen due to how its created...
        if (this._opt1.isEmpty() && this._opt2.isEmpty() && this._opt3.isEmpty()){
            throw new IllegalArgumentException("OneOf must contain at least one option");
        }

        //do i need to check all possible combinations?  can this even happen?
        if (this._opt1.isPresent() && this._opt2.isPresent() && this._opt3.isPresent()){
            throw new IllegalArgumentException("OneOf may only contain one option");
        }
    }

    public static<T1, T2,T3> OneOf3<T1, T2,T3> fromOption1 (final T1 option){
        return new OneOf3<>(option, null, null);
    }

    public static<T1, T2,T3> OneOf3<T1, T2,T3> fromOption2 (final T2 option){
        return new OneOf3<>(null, option, null);
    }

    public static<T1, T2,T3> OneOf3<T1, T2,T3> fromOption3 (final T3 option){
        return new OneOf3<>(null,null,option);
    }

    public boolean hasOption1(){
        return _opt1.isPresent();
    }

    public boolean hasOption2(){
        return _opt2.isPresent();
    }

    public boolean hasOption3(){
        return _opt3.isPresent();
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

    public T3 asOption3(){
        if (_opt3.isEmpty()){
            throw new RuntimeException("Option 3 has no value");
        }
        return _opt3.get();
    }
    public <R> R match(final Function<T1,R> mapper1, final Function<T2,R> mapper2, final Function<T3,R> mapper3)
    {
        return _opt1.map(mapper1)
                .orElseGet(() -> _opt2.map(mapper2)
                        .orElseGet(() -> _opt3.map(mapper3)
                            .orElseThrow(() -> new RuntimeException("All values are null")
                            )
                        )
                );
    }
}

