package Domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Time;
import java.time.LocalTime;

@Converter(autoApply = true)
public class LocalDateAtributteConverter implements AttributeConverter<LocalTime, Time> {
    @Override
    public Time convertToDatabaseColumn(LocalTime localTime){
        return localTime == null? null : Time.valueOf(localTime);
    }
    
    @Override
    public LocalTime convertToEntityAttribute(Time time) {
        return time == null? null : time.toLocalTime();
    }
}
