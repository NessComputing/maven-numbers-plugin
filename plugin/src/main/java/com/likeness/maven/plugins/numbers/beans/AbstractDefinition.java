package com.likeness.maven.plugins.numbers.beans;

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

    protected AbstractDefinition(final String id,
                                 final boolean skip,
                                 final boolean export)
    {
        this.id = id;
        this.skip = skip;
        this.export = export;
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

    public boolean isExport()
    {
        return export;
    }

    public T setExport(final boolean export)
    {
        this.export = export;
        return (T) this;
    }

    public void check()
    {
        Preconditions.checkState(StringUtils.isNotBlank(id), "the id element must not be empty!");
    }
}
