package com.likeness.maven.plugins.numbers.beans;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class NumberDefinition extends AbstractDefinition<NumberDefinition>
{
    public static final String INITIAL_VALUE = "0";

    /** If a multi-number, which field to increment. */
    private int fieldNumber = 0;

    /** Increment of the property when changing it. */
    private int increment = 1;

    @VisibleForTesting
    NumberDefinition(final String id,
                     final boolean skip,
                     final boolean export,
                     final String initialValue,
                     final int fieldNumber,
                     final int increment,
                     final String propertyName,
                     final File propertyFile,
                     final IWFCEnum onMissingFile,
                     final IWFCEnum onMissingProperty,
                     final String format)
    {
        super(id, skip, export, 
              Objects.firstNonNull(initialValue, INITIAL_VALUE),
              propertyName, propertyFile, onMissingFile, onMissingProperty, format);

        this.fieldNumber = fieldNumber;
        this.increment = increment;
    }

    public NumberDefinition()
    {
        super();
        setInitialValue(INITIAL_VALUE);
    }

    public int getFieldNumber()
    {
        return fieldNumber;
    }

    public NumberDefinition setFieldNumber(final int fieldNumber)
    {
        this.fieldNumber = fieldNumber; 
        return this;
    }

    public int getIncrement()
    {
        return increment;
    }

    public NumberDefinition setIncrement(final int increment)
    {
        this.increment = increment;
        return this;
    }

    @Override
    public void check()
    {
        super.check();

        Preconditions.checkState(StringUtils.isNotBlank(getInitialValue()), "the initial value must not be empty");
        Preconditions.checkState(fieldNumber >= 0, "the field number must be >= 0");
    }
}
