package com.play.java.bbgeducation.domain.valueobjects.emailaddress;

import com.play.java.bbgeducation.application.exceptions.InvalidEmailFormatException;
import com.play.java.bbgeducation.domain.valueobjects.ValueObject;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailAddress implements ValueObject {
    private final String email;

    private EmailAddress(@NotNull String email){
        this.email = email;
    }

    public static EmailAddress from(String email){
        return new EmailAddress(validate(email));
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

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        EmailAddress that = (EmailAddress) other;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public @NotNull String toString() {
        return email;
    }
}
