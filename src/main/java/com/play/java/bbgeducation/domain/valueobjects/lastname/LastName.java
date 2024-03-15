package com.play.java.bbgeducation.domain.valueobjects.lastname;

import com.play.java.bbgeducation.domain.valueobjects.ValueObject;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class LastName implements ValueObject {
    private final String value;

    private LastName(@NotNull String value){
        this.value = value;
    }

    public static LastName from(@NotNull String value){
        return new LastName(value);
    }

    @Override
    public @NotNull String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastName firstName = (LastName) o;
        return Objects.equals(value, firstName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
