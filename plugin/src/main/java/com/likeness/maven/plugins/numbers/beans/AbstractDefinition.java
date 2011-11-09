package com.likeness.maven.plugins.numbers.beans;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Preconditions;

public abstract class AbstractDefinition<T extends AbstractDefinition<T>>
{
    /** Name of the build property to define. */
    private String id;

    /** True skips the parsing of this definition. */
    private boolean skip = false;

    /** Whether to export this number directly. */
    private boolean export = false;

    /** Name of the property from the properties file. */
    private String propertyName = null;

    /** Name of the properties file to persist the count. */
    private File propertyFile = null;

    /** What to do when the property is missing from the file. */
    private IWFCEnum onMissingFile = IWFCEnum.FAIL;

    /** What to do when the property is missing from the file. */
    private IWFCEnum onMissingProperty = IWFCEnum.FAIL;

    /** The initial value for this field. */
    private String initialValue = null;

    /** Format for this element. */
    private String format = null;

    protected AbstractDefinition(final String id,
                                 final boolean skip,
                                 final boolean export,
                                 final String initialValue,
                                 final String propertyName,
                                 final File propertyFile,
                                 final IWFCEnum onMissingFile,
                                 final IWFCEnum onMissingProperty,
                                 final String format)
    {
        this.id = id;
        this.skip = skip;
        this.export = export;
        this.initialValue = initialValue;
        this.propertyName = propertyName;
        this.propertyFile = propertyFile;
        this.onMissingFile = onMissingFile;
        this.onMissingProperty = onMissingProperty;
        this.format = format;
    }

    public AbstractDefinition()
    {
    }

    public String getId()
    {
        return id;
    }

    public T setId(final String id)
    {
        this.id = id;
        return (T) this;
    }

    public boolean isSkip()
    {
        return skip;
    }

    public T setSkip(final boolean skip)
    {
        this.skip = skip;
        return (T) this;
    }

    public String getInitialValue()
    {
        return initialValue;
    }

    public T setInitialValue(final String initialValue)
    {
        this.initialValue = initialValue;
        return (T) this;
    }

    public boolean isExport()
    {
        return export;
    }

    public T setExport(final boolean export)
    {
        this.export = export;
        return (T) this;
    }

    public String getPropertyName()
    {
        return StringUtils.isNotBlank(propertyName) ? propertyName : getId();
    }

    public T setPropertyName(final String propertyName)
    {
        this.propertyName = propertyName;
        return (T) this;
    }

    public File getPropertyFile()
    {
        return propertyFile;
    }

    public T setPropertyFile(final File propertyFile)
    {
        this.propertyFile = propertyFile;
        return (T) this;
    }

    public IWFCEnum getOnMissingFile()
    {
        return onMissingFile;
    }

    public T setOnMissingFile(final String onMissingFile)
    {
        this.onMissingFile = IWFCEnum.forString(onMissingFile);
        return (T) this;
    }

    public IWFCEnum getOnMissingProperty()
    {
        return onMissingProperty;
    }

    public T setOnMissingProperty(final String onMissingProperty)
    {
        this.onMissingProperty = IWFCEnum.forString(onMissingProperty);
        return (T) this;
    }

    public String getFormat()
    {
        return format;
    }

    public T setFormat(final String format)
    {
        this.format = format;
        return (T) this;
    }

    public void check()
    {
        Preconditions.checkState(StringUtils.isNotBlank(id), "the id element must not be empty!");
    }
}
