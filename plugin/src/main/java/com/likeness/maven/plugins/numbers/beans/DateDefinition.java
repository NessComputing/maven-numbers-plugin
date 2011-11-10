package com.likeness.maven.plugins.numbers.beans;

import java.io.File;

import com.google.common.annotations.VisibleForTesting;

public class DateDefinition extends AbstractDefinition<DateDefinition>
{
    /** Timezone for this date. */
    private String timezone = null;

    /** Value for this date. */
    private Long value = null;

    @VisibleForTesting
    DateDefinition(final String id,
                   final boolean skip,
                   final boolean export,
                   final String initialValue,
                   final String timezone,
                   final Long value,
                   final String propertyName,
                   final File propertyFile,
                   final IWFCEnum onMissingFile,
                   final IWFCEnum onMissingProperty,
                   final String format)
    {
        super(id, skip, export, initialValue, propertyName, propertyFile, onMissingFile, onMissingProperty, format);

        this.timezone = timezone;
        this.value = value;
    }

    public DateDefinition()
    {
        super();
    }

    public String getTimezone()
    {
        return timezone;
    }

    public DateDefinition setTimezone(final String timezone)
    {
        this.timezone = timezone;
        return this;
    }

    public Long getValue()
    {
        return value;
    }

    public DateDefinition setValue(final Long value)
    {
        this.value = value;
        return this;
    }
}
