package com.play.java.bbgeducation.application.mapping;

public interface Mapper<A,B> extends MapTo<A, B>, MapFrom<B, A> {

    B mapTo(A a);
    A mapFrom(B b);

}
