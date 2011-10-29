package com.likeness.maven.plugins.numbers.beans;

import java.util.Properties;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.annotations.VisibleForTesting;

public class PropertyGroup
{
    /** Property group id. */
    private String id;

    /** Activate the group if the current project version does not contain SNAPSHOT- */
    private boolean activeOnRelease = true;

    /** Activate the group if the current project version contains SNAPSHOT- */
    private boolean activeOnSnapshot = true;

    /** Action if this property group defines a duplicate property. */
    private String onDuplicateProperty = "fail";

    /** Action if any property from that group could not be defined. */
    private String onMissingProperty = "fail";

    /** Property definitions in this group. */
    private Properties properties = null;

    @VisibleForTesting
    PropertyGroup(final String id,
                  final boolean activeOnRelease,
                  final boolean activeOnSnapshot,
                  final String onDuplicateProperty,
                  final String onMissingProperty,
                  final Properties properties)
    {
        this.id = id;
        this.activeOnRelease = activeOnRelease;
        this.activeOnSnapshot = activeOnSnapshot;
        this.onDuplicateProperty = onDuplicateProperty;
        this.onMissingProperty = onMissingProperty;
        this.properties = properties;
    }

    public PropertyGroup()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isActiveOnRelease()
    {
        return activeOnRelease;
    }

    public void setActiveOnRelease(boolean activeOnRelease)
    {
        this.activeOnRelease = activeOnRelease;
    }

    public boolean isActiveOnSnapshot()
    {
        return activeOnSnapshot;
    }

    public void setActiveOnSnapshot(boolean activeOnSnapshot)
    {
        this.activeOnSnapshot = activeOnSnapshot;
    }

    public String getOnDuplicateProperty()
    {
        return onDuplicateProperty;
    }

    public void setOnDuplicateProperty(String onDuplicateProperty)
    {
        this.onDuplicateProperty = onDuplicateProperty;
    }

    public String getOnMissingProperty()
    {
        return onMissingProperty;
    }

    public void setOnMissingProperty(String onMissingProperty)
    {
        this.onMissingProperty = onMissingProperty;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof PropertyGroup))
            return false;
        PropertyGroup castOther = (PropertyGroup) other;
        return new EqualsBuilder().append(id, castOther.id)
        .append(activeOnRelease, castOther.activeOnRelease)
        .append(activeOnSnapshot, castOther.activeOnSnapshot)
        .append(onDuplicateProperty, castOther.onDuplicateProperty)
        .append(onMissingProperty, castOther.onMissingProperty)
        .append(properties, castOther.properties)
        .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(id).append(activeOnRelease).append(activeOnSnapshot).append(onDuplicateProperty).append(onMissingProperty).append(properties).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString())
        .append("id", id)
        .append("activeOnRelease", activeOnRelease)
        .append("activeOnSnapshot", activeOnSnapshot)
        .append("onDuplicateProperty", onDuplicateProperty)
        .append("onMissingProperty", onMissingProperty)
        .append("properties", properties)
        .toString();
    }
}
