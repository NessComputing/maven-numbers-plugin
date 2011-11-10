package com.likeness.maven.plugins.numbers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.likeness.maven.plugins.numbers.beans.DateDefinition;

public class DateField implements PropertyElement
{
    private final DateDefinition dateDefinition;
    private final ValueProvider valueProvider;

    public DateField(final DateDefinition dateDefinition, final ValueProvider valueProvider)
    {
        this.dateDefinition = dateDefinition;
        this.valueProvider = valueProvider;
    }
    
    @Override
    public String getPropertyName()
    {
        return dateDefinition.getId();
    }

    @Override
    public String getPropertyValue()
    {
        final DateTimeZone timeZone = dateDefinition.getTimezone() == null ? DateTimeZone.getDefault() : DateTimeZone.forID(dateDefinition.getTimezone());

        DateTime date = getDateTime(valueProvider.getValue(), timeZone);

        if (date == null && dateDefinition.getValue() != null) {
            date = new DateTime(dateDefinition.getValue(), timeZone);
        }

        if (date == null) {
            date = new DateTime(timeZone);
        }

        final String format = dateDefinition.getFormat();

        if (format == null) {
            return date.toString();
        }
        else {
            final DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
            return formatter.print(date);
        }
    }

    private DateTime getDateTime(final String value, final DateTimeZone timeZone)
    {
        if (value == null) {
            return null;
        }
        try {
            return new DateTime(Long.parseLong(value), timeZone);
        }
        catch (NumberFormatException nfe) {
            return new DateTime(value, timeZone);
        }
    }

    @Override
    public boolean isExport()
    {
        return dateDefinition.isExport();
    }

    @Override
    public String toString()
    {
        return getPropertyValue();
    }
}