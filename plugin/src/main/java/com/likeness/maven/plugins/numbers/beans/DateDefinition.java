package com.likeness.maven.plugins.numbers.beans;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.annotations.VisibleForTesting;

public class DateDefinition extends AbstractDefinition
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
                   final String format,
                   final String timezone,
                   final long value)
    {
        super(id, skip);

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

    public void setFormat(String format)
    {
        this.format = format;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public void setTimezone(String timezone)
    {
        this.timezone = timezone;
    }

    public long getValue()
    {
        return value;
    }

    public void setValue(long value)
    {
        this.value = value;
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
