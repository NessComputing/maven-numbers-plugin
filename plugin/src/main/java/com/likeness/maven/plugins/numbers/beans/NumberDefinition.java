package com.likeness.maven.plugins.numbers.beans;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

public class NumberDefinition extends AbstractDefinition<NumberDefinition>
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
    private IWFCEnum onMissingFile = IWFCEnum.FAIL;

    /** What to do when the property is missing from the file. */
    private IWFCEnum onMissingProperty = IWFCEnum.FAIL;

    /** Whether to export this number directly. */
    private boolean export = false;

    @VisibleForTesting
    NumberDefinition(final String id,
                     final boolean skip,
                     final String initialValue,
                     final int fieldNumber,
                     final int increment,
                     final String propertyName,
                     final File propertyFile,
                     final IWFCEnum onMissingFile,
                     final IWFCEnum onMissingProperty,
                     final boolean export)
    {
        super(id, skip);

        this.initialValue = initialValue;
        this.fieldNumber = fieldNumber;
        this.increment = increment;
        this.propertyName = propertyName;
        this.propertyFile = propertyFile;
        this.onMissingFile = onMissingFile;
        this.onMissingProperty = onMissingProperty;
        this.export = export;
    }

    public NumberDefinition()
    {
        super();
    }

    public String getInitialValue()
    {
        return initialValue;
    }

    public NumberDefinition setInitialValue(final String initialValue)
    {
        this.initialValue = initialValue;
        return this;
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

    public String getPropertyName()
    {
        return StringUtils.isNotBlank(propertyName) ? propertyName : getId();
    }

    public NumberDefinition setPropertyName(final String propertyName)
    {
        this.propertyName = propertyName;
        return this;
    }

    public File getPropertyFile()
    {
        return propertyFile;
    }

    public NumberDefinition setPropertyFile(final File propertyFile)
    {
        this.propertyFile = propertyFile;
        return this;
    }

    public IWFCEnum getOnMissingFile()
    {
        return onMissingFile;
    }

    public NumberDefinition setOnMissingFile(final String onMissingFile)
    {
        this.onMissingFile = IWFCEnum.forString(onMissingFile);
        return this;
    }

    public IWFCEnum getOnMissingProperty()
    {
        return onMissingProperty;
    }

    public NumberDefinition setOnMissingProperty(final String onMissingProperty)
    {
        this.onMissingProperty = IWFCEnum.forString(onMissingProperty);
        return this;
    }

    public void setExport(final boolean export)
    {
        this.export = export;
    }

    public boolean isExport()
    {
        return export;
    }

    @Override
    public void check()
    {
        super.check();

        Preconditions.checkState(StringUtils.isNotBlank(initialValue), "the initial value must not be empty");
        Preconditions.checkState(fieldNumber >= 0, "the field number must be >= 0");
    }
}
