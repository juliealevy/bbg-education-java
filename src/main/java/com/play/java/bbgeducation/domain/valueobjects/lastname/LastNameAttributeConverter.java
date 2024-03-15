package com.play.java.bbgeducation.domain.valueobjects.lastname;

import jakarta.persistence.AttributeConverter;

public class LastNameAttributeConverter implements AttributeConverter<LastName, String> {
    @Override
    public String convertToDatabaseColumn(LastName lastName) {
        return lastName == null ? null : lastName.toString();
    }

    @Override
    public LastName convertToEntityAttribute(String dbData) {
        return dbData == null ? null : LastName.from(dbData);
    }
}
