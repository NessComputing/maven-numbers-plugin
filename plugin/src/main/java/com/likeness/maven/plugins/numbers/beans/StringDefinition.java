package com.likeness.maven.plugins.numbers.beans;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.annotations.VisibleForTesting;

import javax.print.DocFlavor;

public class StringDefinition extends AbstractDefinition<StringDefinition>
{
    /**
     * Values for this string.
     */
    private List<String> values;

    /**
     * Whether a blank string is a valid value.
     */
    private boolean blankIsValid = true;

    /**
     * Default action on missing value.
     */
    private IWFEnum onMissingValue = IWFEnum.FAIL;

    @VisibleForTesting
    StringDefinition(
            final String id,
            final boolean skip,
            final boolean export,
            final String initialValue,
            final List<String> values,
            final boolean blankIsValid,
            final IWFEnum onMissingValue,
            final String propertyName,
            final File propertyFile,
            final IWFCEnum onMissingFile,
            final IWFCEnum onMissingProperty)
    {
        super(id, skip, export, initialValue, propertyName, propertyFile, onMissingFile, onMissingProperty);

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

    public StringDefinition setValues(final List<String> values)
    {
        this.values = values;
        return this;
    }

    public boolean isBlankIsValid()
    {
        return blankIsValid;
    }

    public StringDefinition setBlankIsValid(final boolean blankIsValid)
    {
        this.blankIsValid = blankIsValid;
        return this;
    }

    public IWFEnum getOnMissingValue()
    {
        return onMissingValue;
    }

    public StringDefinition setOnMissingValue(final String onMissingValue)
    {
        this.onMissingValue = IWFEnum.forString(onMissingValue);
        return this;
    }
}
