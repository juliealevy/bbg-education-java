package com.play.java.bbgeducation.domain.valueobjects.emailaddress;

import an.awesome.pipelinr.repack.com.google.common.base.Strings;
import com.play.java.bbgeducation.application.exceptions.InvalidEmailFormatException;
import com.play.java.bbgeducation.domain.valueobjects.ValueObject;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

public class EmailAddress implements ValueObject {
    private final String value;

    private EmailAddress(@NotNull String email){
        this.value = email;
    }

    public static EmailAddress from(String email){
        return new EmailAddress(validate(email));
    }

    public static EmailAddress empty() {
        return new EmailAddress("");
    }

    public static @NotNull String validate(@NotNull String email){
        if (!isValid(email)){
            throw new InvalidEmailFormatException(email);
        }
        return email;
    }
    public static boolean isValid(@NotNull String email) {
        return Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,3}")
                .matcher(email)
                .matches();
    }

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(value);
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        EmailAddress that = (EmailAddress) other;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public @NotNull String toString() {
        return value;
    }
}
