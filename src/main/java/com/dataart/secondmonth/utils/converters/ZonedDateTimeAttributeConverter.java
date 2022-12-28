package com.dataart.secondmonth.utils.converters;

import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter
@Component
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zoneDateTime) {
        if (zoneDateTime == null) {
            return null;
        }

        LocalDateTime withoutTimezone = zoneDateTime.toLocalDateTime();
        return Timestamp.valueOf(withoutTimezone);
    }


    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        if (sqlTimestamp == null) {
            return null;
        }

        LocalDateTime withoutTimezone = sqlTimestamp.toLocalDateTime();
        return withoutTimezone.atZone(ZoneId.systemDefault());
    }
}