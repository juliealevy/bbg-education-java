package com.play.java.bbgeducation.domain.valueobjects.firstname;

import com.play.java.bbgeducation.domain.valueobjects.ValueObject;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class FirstName implements ValueObject {
    private final String value;

    private FirstName(@NotNull String value){
        this.value = value;
    }

    public static FirstName from(@NotNull String value){
        return new FirstName(value);
    }

    @Override
    public @NotNull String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirstName firstName = (FirstName) o;
        return Objects.equals(value, firstName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
