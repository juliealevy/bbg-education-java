package com.play.java.bbgeducation.domain.valueobjects.password;

import com.play.java.bbgeducation.application.exceptions.InvalidPasswordException;
import com.play.java.bbgeducation.domain.valueobjects.ValueObject;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class Password implements ValueObject {
    private final String value;

    protected Password(@NotNull String value){
        this.value = value;
    }

    public static Password from(@NotNull String value){
        return new Password(validate(value));
    }

    //When auth code doesn't need or want to expose password
    public static Password empty(){
        return new Password("");
    }

    public static String validate(@NotNull String value){
        if (!isValid(value)){
            throw new InvalidPasswordException();
        }
        return value;
    }

    public static boolean isValid(@NotNull String value){
        //ToDO:  add any password validation (length, required characters, etc)
        return true;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
