package com.likeness.maven.plugins.numbers;

import java.io.File;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class NumberDefinition
{
    /** Name of the build property to define. */
    private String name;

    /** Name of the properties file to persist the count. */
    private File propertiesFile;

    /** Increment of the property when changing it. */
    private int increment = 1;

    /** If a multi-number, which field to increment. */
    private int fieldNumber = 0;

    /** Whether to create an entry in the properties file or not. */
    private boolean createProperty = true;

    /** Whether to create the properties file if missing. */
    private boolean createFile = true;

    private String initialValue = "0";

    public NumberDefinition(final String name,
                       final File propertiesFile,
                       final int increment,
                       final int fieldNumber,
                       final boolean createProperty,
                       final boolean createFile,
                       final String initialValue)
    {
        this.name = name;
        this.propertiesFile = propertiesFile;
        this.increment = increment;
        this.fieldNumber = fieldNumber;
        this.createProperty = createProperty;
        this.createFile = createFile;
        this.initialValue = initialValue;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public File getPropertiesFile()
    {
        return propertiesFile;
    }

    public void setPropertiesFile(File propertiesFile)
    {
        this.propertiesFile = propertiesFile;
    }

    public int getIncrement()
    {
        return increment;
    }

    public void setIncrement(int increment)
    {
        this.increment = increment;
    }

    public int getFieldNumber()
    {
        return fieldNumber;
    }

    public void setFieldNumber(int fieldNumber)
    {
        this.fieldNumber = fieldNumber;
    }

    public boolean isCreateProperty()
    {
        return createProperty;
    }

    public void setCreateProperty(boolean createProperty)
    {
        this.createProperty = createProperty;
    }

    public boolean isCreateFile()
    {
        return createFile;
    }

    public void setCreateFile(boolean createFile)
    {
        this.createFile = createFile;
    }

    public String getInitialValue()
    {
        return initialValue;
    }

    public void setInitialValue(String initialValue)
    {
        this.initialValue = initialValue;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof NumberDefinition))
            return false;
        NumberDefinition castOther = (NumberDefinition) other;
        return new EqualsBuilder().append(name, castOther.name)
            .append(propertiesFile, castOther.propertiesFile)
            .append(increment, castOther.increment)
            .append(fieldNumber, castOther.fieldNumber)
            .append(createProperty, castOther.createProperty)
            .append(createFile, castOther.createFile)
            .append(initialValue, castOther.initialValue)
            .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(name).append(propertiesFile).append(increment).append(fieldNumber).append(createProperty).append(createFile).append(initialValue).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("name", name)
            .append("propertiesFile", propertiesFile)
            .append("increment", increment)
            .append("fieldNumber", fieldNumber)
            .append("createProperty", createProperty)
            .append("createFile", createFile)
            .append("initialValue", initialValue)
            .toString();
    }


}
