package com.play.java.bbgeducation.domain.valueobjects.password;

import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PasswordAttributeConverter implements AttributeConverter<Password, String> {


    @Override
    public String convertToDatabaseColumn(Password password) {
        return password == null ? null : password.toString();
    }

    @Override
    public Password convertToEntityAttribute(String dbData) {
        return dbData == null? null : Password.from(dbData);
    }
}
