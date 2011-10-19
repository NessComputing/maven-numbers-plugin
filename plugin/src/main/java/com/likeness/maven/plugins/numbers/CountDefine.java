package com.likeness.maven.plugins.numbers;

import java.io.File;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CountDefine
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
    private boolean create = false;

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public File getPropertiesFile()
    {
        return propertiesFile;
    }

    public void setPropertiesFile(final File propertiesFile)
    {
        this.propertiesFile = propertiesFile;
    }

    public int getIncrement()
    {
        return increment;
    }

    public void setIncrement(final int increment)
    {
        this.increment = increment;
    }

    public int getFieldNumber()
    {
        return fieldNumber;
    }

    public void setFieldNumber(final int fieldNumber)
    {
        this.fieldNumber = fieldNumber;
    }

    public boolean isCreate()
    {
        return create;
    }

    public void setCreate(final boolean create)
    {
        this.create = create;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof CountDefine))
            return false;
        CountDefine castOther = (CountDefine) other;
        return new EqualsBuilder().append(name, castOther.name).append(propertiesFile, castOther.propertiesFile).append(increment, castOther.increment).append(fieldNumber, castOther.fieldNumber).append(create, castOther.create).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(name).append(propertiesFile).append(increment).append(fieldNumber).append(create).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("name", name).append("propertiesFile", propertiesFile).append("increment", increment).append("fieldNumber", fieldNumber).append("create", create).toString();
    }


}
