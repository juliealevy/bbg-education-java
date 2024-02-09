package com.play.java.bbgeducation.application.common.logging.scrubbers;

import com.google.common.reflect.TypeToken;

public interface LoggingBodyScrubber<T> {
    T scrub(T body);

    default boolean matches(T bodyType) {
        TypeToken<T> typeToken = new TypeToken<T>(getClass()) {
        };
        return typeToken.isSubtypeOf(bodyType.getClass());
    }
}
