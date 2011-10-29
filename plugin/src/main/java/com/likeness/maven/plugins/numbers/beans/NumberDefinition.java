package com.likeness.maven.plugins.numbers.beans;

import java.io.File;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.annotations.VisibleForTesting;

public class NumberDefinition extends AbstractDefinition
{
    /** The initial value for this field. */
    private String initialValue = "0";

    /** If a multi-number, which field to increment. */
    private int fieldNumber = 0;

    /** Increment of the property when changing it. */
    private int increment = 1;

    /** Name of the property from the properties file. */
    private String propertyName = null;

    /** Name of the properties file to persist the count. */
    private File propertyFile = null;

    /** What to do when the property is missing from the file. */
    private String onMissingFile = "fail";

    /** What to do when the property is missing from the file. */
    private String onMissingProperty = "fail";

    @VisibleForTesting
    NumberDefinition(final String id,
                     final boolean skip,
                     final String initialValue,
                     final int fieldNumber,
                     final int increment,
                     final String propertyName,
                     final File propertyFile,
                     final String onMissingFile,
                     final String onMissingProperty)
    {
        super(id, skip);

        this.initialValue = initialValue;
        this.fieldNumber = fieldNumber;
        this.increment = increment;
        this.propertyName = propertyName;
        this.propertyFile = propertyFile;
        this.onMissingFile = onMissingFile;
        this.onMissingProperty = onMissingProperty;
    }

    public NumberDefinition()
    {
        super();
    }

    public String getInitialValue()
    {
        return initialValue;
    }

    public void setInitialValue(String initialValue)
    {
        this.initialValue = initialValue;
    }

    public int getFieldNumber()
    {
        return fieldNumber;
    }

    public void setFieldNumber(int fieldNumber)
    {
        this.fieldNumber = fieldNumber;
    }

    public int getIncrement()
    {
        return increment;
    }

    public void setIncrement(int increment)
    {
        this.increment = increment;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }

    public File getPropertyFile()
    {
        return propertyFile;
    }

    public void setPropertyFile(File propertyFile)
    {
        this.propertyFile = propertyFile;
    }

    public String getOnMissingFile()
    {
        return onMissingFile;
    }

    public void setOnMissingFile(String onMissingFile)
    {
        this.onMissingFile = onMissingFile;
    }

    public String getOnMissingProperty()
    {
        return onMissingProperty;
    }

    public void setOnMissingProperty(String onMissingProperty)
    {
        this.onMissingProperty = onMissingProperty;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof NumberDefinition))
            return false;
        NumberDefinition castOther = (NumberDefinition) other;
        return new EqualsBuilder().append(initialValue, castOther.initialValue)
            .append(fieldNumber, castOther.fieldNumber)
            .append(increment, castOther.increment)
            .append(propertyName, castOther.propertyName)
            .append(propertyFile, castOther.propertyFile)
            .append(onMissingFile, castOther.onMissingFile)
            .append(onMissingProperty, castOther.onMissingProperty)
            .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(initialValue).append(fieldNumber).append(increment).append(propertyName).append(propertyFile).append(onMissingFile).append(onMissingProperty).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString())
            .append("initialValue", initialValue)
            .append("fieldNumber", fieldNumber)
            .append("increment", increment)
            .append("propertyName", propertyName)
            .append("propertyFile", propertyFile)
            .append("onMissingFile", onMissingFile)
            .append("onMissingProperty", onMissingProperty)
            .toString();
    }
}
