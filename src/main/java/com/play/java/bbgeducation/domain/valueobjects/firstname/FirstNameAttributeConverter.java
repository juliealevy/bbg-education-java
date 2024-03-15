package com.play.java.bbgeducation.domain.valueobjects.firstname;

import jakarta.persistence.AttributeConverter;

public class FirstNameAttributeConverter implements AttributeConverter<FirstName, String> {
    @Override
    public String convertToDatabaseColumn(FirstName firstName) {
        return firstName == null ? null : firstName.toString();
    }

    @Override
    public FirstName convertToEntityAttribute(String dbData) {
        return dbData == null ? null : FirstName.from(dbData);
    }
}
