package com.play.java.bbgeducation.domain.valueobjects.emailaddress;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailAddressAttributeConverter  implements AttributeConverter<EmailAddress, String> {


    @Override
    public String convertToDatabaseColumn(EmailAddress emailAddress) {
        return emailAddress == null ? null: emailAddress.toString();
    }

    @Override
    public EmailAddress convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EmailAddress.from(dbData);
    }
}
