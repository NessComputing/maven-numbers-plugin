package com.likeness.maven.plugins.numbers.beans;

import java.io.File;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.annotations.VisibleForTesting;

public class DateDefinition extends AbstractDefinition<DateDefinition>
{
    /** Format for this date. */
    private String format = null;

    /** Timezone for this date. */
    private String timezone = null;

    /** Value for this date. */
    private long value = 0L;

    @VisibleForTesting
    DateDefinition(final String id,
                   final boolean skip,
                   final boolean export,
                   final String initialValue,
                   final String format,
                   final String timezone,
                   final long value,
                   final String propertyName,
                   final File propertyFile,
                   final IWFCEnum onMissingFile,
                   final IWFCEnum onMissingProperty)
    {
        super(id, skip, export, initialValue, propertyName, propertyFile, onMissingFile, onMissingProperty);

        this.format = format;
        this.timezone = timezone;
        this.value = value;
    }

    public DateDefinition()
    {
        super();
    }

    public String getFormat()
    {
        return format;
    }

    public DateDefinition setFormat(String format)
    {
        this.format = format;
        return this;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public DateDefinition setTimezone(String timezone)
    {
        this.timezone = timezone;
        return this;
    }

    public long getValue()
    {
        return value;
    }

    public DateDefinition setValue(long value)
    {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof DateDefinition))
            return false;
        DateDefinition castOther = (DateDefinition) other;
        return new EqualsBuilder().append(format, castOther.format).append(timezone, castOther.timezone).append(value, castOther.value).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(format).append(timezone).append(value).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("format", format).append("timezone", timezone).append("value", value).toString();
    }


}
