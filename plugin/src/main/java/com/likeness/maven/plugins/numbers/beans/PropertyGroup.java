package com.likeness.maven.plugins.numbers.beans;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.codehaus.plexus.util.StringUtils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Functions;
import com.google.common.base.Objects;
import com.google.common.collect.Iterators;

public class PropertyGroup
{
    /** Property group id. */
    private String id;

    /** Activate the group if the current project version does not contain SNAPSHOT- */
    private boolean activeOnRelease = true;

    /** Activate the group if the current project version contains SNAPSHOT- */
    private boolean activeOnSnapshot = true;

    /** Action if this property group defines a duplicate property. */
    private IWFEnum onDuplicateProperty = IWFEnum.FAIL;

    /** Action if any property from that group could not be defined. */
    private IWFEnum onMissingProperty = IWFEnum.FAIL;

    /** Property definitions in this group. */
    private Properties properties = null;

    @VisibleForTesting
    PropertyGroup(final String id,
                  final boolean activeOnRelease,
                  final boolean activeOnSnapshot,
                  final IWFEnum onDuplicateProperty,
                  final IWFEnum onMissingProperty,
                  final Properties properties)
    {
        this();

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

    public void setActiveOnRelease(final boolean activeOnRelease)
    {
        this.activeOnRelease = activeOnRelease;
    }

    public boolean isActiveOnSnapshot()
    {
        return activeOnSnapshot;
    }

    public void setActiveOnSnapshot(final boolean activeOnSnapshot)
    {
        this.activeOnSnapshot = activeOnSnapshot;
    }

    public IWFEnum getOnDuplicateProperty()
    {
        return onDuplicateProperty;
    }

    public void setOnDuplicateProperty(final String onDuplicateProperty)
    {
        this.onDuplicateProperty = IWFEnum.forString(onDuplicateProperty);
    }

    public IWFEnum getOnMissingProperty()
    {
        return onMissingProperty;
    }

    public void setOnMissingProperty(final String onMissingProperty)
    {
        this.onMissingProperty = IWFEnum.forString(onMissingProperty);
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties(final Properties properties)
    {
        this.properties = properties;
    }

    public void check()
    {
    }

    public Iterator<String> getPropertyNames()
    {
        return Iterators.transform(properties.keySet().iterator(), Functions.toStringFunction());
    }

    public String getPropertyValue(final String propertyName, final Map<String, String> propElements)
    {
        String propertyValue = Objects.firstNonNull(properties.getProperty(propertyName), "");

        for (Map.Entry<String, String> entry : propElements.entrySet()) {
            final String key = "#{" + entry.getKey() + "}";
            propertyValue = StringUtils.replace(propertyValue, key, entry.getValue());
        }
        // Replace all remaining groups.
        final String result = propertyValue.replaceAll("\\#\\{.*\\}", "");
        IWFEnum.checkState(getOnMissingProperty(), StringUtils.equals(propertyValue, result), "property");
        return result;
    }
}
