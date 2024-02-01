package com.play.java.bbgeducation.application.common.oneof;

import java.util.function.Function;

public class OneOf2<T1,T2> {
    private final T1 _option1;
    private final T2 _option2;

    private OneOf2(T1 option1, T2 option2){
        this._option1 = option1;
        this._option2 = option2;

        //do i need to check this?  static instance creators prevent this?
        if (!this.hasOption1() && !this.hasOption2()) {
            throw new IllegalArgumentException("OneOf must contain at least one option");
        }

        if (this.hasOption1() && this.hasOption2()){
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
        return _option1 != null;
    }

    public boolean hasOption2(){
        return _option2 != null;
    }

    public T1 asOption1(){
        if (!this.hasOption1()){
            throw new RuntimeException("Option 1 has no value");
        }
        return _option1;
    }

    public T2 asOption2(){
        if (!this.hasOption2()){
            throw new RuntimeException("Option 2 has no value");
        }
        return _option2;
    }
    public <TResult> TResult match(final Function<T1,TResult> mapper1, final Function<T2,TResult> mapper2)
    {
        if (this.hasOption1()){
            return mapper1.apply(_option1);
        }
        if (this.hasOption2()){
            return mapper2.apply(_option2);
        }
        throw new RuntimeException("All values are null");

    }
}
