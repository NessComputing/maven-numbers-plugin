package com.likeness.maven.plugins.numbers.beans;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.annotations.VisibleForTesting;

public class StringDefinition extends AbstractDefinition
{
    /** Values for this string. */
    private List<String> values;

    /** Whether a blank string is a valid value. */
    private boolean blankIsValid = true;

    /** Default action on missing value. */
    private String onMissingValue = "fail";

    @VisibleForTesting
    StringDefinition(final String id,
                     final boolean skip,
                     final List<String> values,
                     final boolean blankIsValid,
                     final String onMissingValue)
    {
        super(id, skip);

        this.values = values;
        this.blankIsValid = blankIsValid;
        this.onMissingValue = onMissingValue;
    }

    public StringDefinition()
    {
        super();
    }

    public List<String> getValues()
    {
        return values;
    }

    public void setValues(List<String> values)
    {
        this.values = values;
    }

    public boolean isBlankIsValid()
    {
        return blankIsValid;
    }

    public void setBlankIsValid(boolean blankIsValid)
    {
        this.blankIsValid = blankIsValid;
    }

    public String getOnMissingValue()
    {
        return onMissingValue;
    }

    public void setOnMissingValue(String onMissingValue)
    {
        this.onMissingValue = onMissingValue;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof StringDefinition))
            return false;
        StringDefinition castOther = (StringDefinition) other;
        return new EqualsBuilder().appendSuper(super.equals(other)).append(values, castOther.values).append(blankIsValid, castOther.blankIsValid).append(onMissingValue, castOther.onMissingValue).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(values).append(blankIsValid).append(onMissingValue).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("values", values).append("blankIsValid", blankIsValid).append("onMissingValue", onMissingValue).toString();
    }


}
