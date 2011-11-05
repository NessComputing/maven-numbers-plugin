package com.likeness.maven.plugins.numbers;

import java.util.Properties;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Preconditions;

public class PropertyCache
{
    private final Properties props;

    private final boolean exists;

    private boolean create;

    public PropertyCache(@Nonnull final Properties props,
                         final boolean exists,
                         final boolean create)
    {
        Preconditions.checkNotNull(props, "Properties element can not be null!");
        this.props = props;
        this.exists = exists;
        this.create = create;
    }

    public Properties getProps()
    {
        return props;
    }

    public boolean isExists()
    {
        return exists;
    }

    public boolean isCreate()
    {
        return create;
    }

    public void doCreate()
    {
        this.create = true;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof PropertyCache))
            return false;
        PropertyCache castOther = (PropertyCache) other;
        return new EqualsBuilder().append(props, castOther.props).append(exists, castOther.exists).append(create, castOther.create).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(props).append(exists).append(create).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("props", props).append("exists", exists).append("create", create).toString();
    }



}
