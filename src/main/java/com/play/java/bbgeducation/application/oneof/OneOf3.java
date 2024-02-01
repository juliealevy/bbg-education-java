package com.play.java.bbgeducation.application.oneof;

import java.util.function.Function;

public class OneOf3 <T1,T2,T3>{

    private final T1 _option1;
    private final T2 _option2;
    private final T3 _option3;

    private OneOf3(T1 option1, T2 option2, T3 option3){
        this._option1 = option1;
        this._option2 = option2;
        this._option3 = option3;

        //not sure these conditions can happen due to how its created...
        if (!this.hasOption1() && !this.hasOption2() && !this.hasOption3()){
            throw new IllegalArgumentException("OneOf must contain at least one option");
        }

        //do i need to check all possible combinations?  can this even happen?
        if (this.hasOption1() && this.hasOption2() && this.hasOption3()){
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
        return _option1 != null;
    }

    public boolean hasOption2(){
        return _option2 != null;
    }

    public boolean hasOption3(){
        return _option3 != null;
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

    public T3 asOption3(){
        if (!this.hasOption3()){
            throw new RuntimeException("Option 3 has no value");
        }
        return _option3;
    }
    public <R> R match(final Function<T1,R> mapper1, final Function<T2,R> mapper2, final Function<T3,R> mapper3)
    {
        if (this.hasOption1()){
            return mapper1.apply(_option1);
        }
        if (this.hasOption2()){
            return mapper2.apply(_option2);
        }
        if (this.hasOption3()){
            return mapper3.apply(_option3);
        }
        throw new RuntimeException("All values are null");
    }
}

