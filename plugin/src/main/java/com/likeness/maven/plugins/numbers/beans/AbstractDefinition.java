package com.likeness.maven.plugins.numbers.beans;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractDefinition
{
    /** Name of the build property to define. */
    private String id;

    /** True skips the parsing of this definition. */
    private boolean skip = false;

    protected AbstractDefinition(final String id,
                                 final boolean skip)
    {
        this.id = id;
        this.skip = skip;
    }

    public AbstractDefinition()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public boolean isSkip()
    {
        return skip;
    }

    public void setSkip(final boolean skip)
    {
        this.skip = skip;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof AbstractDefinition))
            return false;
        AbstractDefinition castOther = (AbstractDefinition) other;
        return new EqualsBuilder().append(id, castOther.id).append(skip, castOther.skip).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(id).append(skip).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", id).append("skip", skip).toString();
    }
}
