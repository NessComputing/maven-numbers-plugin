package com.likeness.maven.plugins.numbers;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import com.likeness.maven.plugins.numbers.beans.IWFCEnum;
import com.likeness.maven.plugins.numbers.beans.NumberDefinition;

public class PropertyCache
{
    /** Cache for properties files loaded from disk */
    private Map<File, PropertyCacheEntry> propFiles = Maps.newHashMap();

    public String getPropertyValue(final NumberDefinition numberDefinition)
        throws IOException
    {
        final Properties props = getProperties(numberDefinition);
        if (props == null) {
            return numberDefinition.getInitialValue();
        }
        return findCurrentValue(props, numberDefinition);
    }

    private String findCurrentValue(final Properties props, final NumberDefinition numberDefinition)
    {
        final String propName = numberDefinition.getPropertyName();
        final boolean hasProperty = props.containsKey(propName);

        final boolean createProperty = IWFCEnum.checkState(numberDefinition.getOnMissingProperty(), hasProperty, propName);

        String currentValue = null;
        if (hasProperty) {
            currentValue = props.getProperty(propName);
        }
        else if (createProperty) {
            currentValue = numberDefinition.getInitialValue();
            props.setProperty(propName, currentValue);
        }

        return currentValue;
    }

    @VisibleForTesting
    Properties getProperties(final NumberDefinition numberDefinition)
        throws IOException
    {
        final File definitionPropertyFile = numberDefinition.getPropertyFile();

        // Ephemeral, so return null.
        if (definitionPropertyFile == null) {
            return null;
        }

        PropertyCacheEntry propertyCacheEntry;
        final File propertyFile = definitionPropertyFile.getCanonicalFile();

        // Throws an exception if the file must exist and does not.
        final boolean createFile = IWFCEnum.checkState(numberDefinition.getOnMissingFile(), propertyFile.exists(), definitionPropertyFile.getCanonicalPath());

        propertyCacheEntry = propFiles.get(propertyFile);

        if (propertyCacheEntry != null) {
            // If there is a cache hit, something either has loaded the file
            // or another property has already put in a creation order.
            // Make sure that if this number has a creation order it is obeyed.
            if (createFile) {
                propertyCacheEntry.doCreate();
            }
        }
        else {
            // Try loading or creating properties.
            final Properties props = new Properties();

            if (!propertyFile.exists()) {
                propertyCacheEntry = new PropertyCacheEntry(props, false, createFile); // does not exist
            }
            else {
                if (propertyFile.isFile() && propertyFile.canRead()) {
                    InputStream stream = null;
                    try {
                        stream = new FileInputStream(propertyFile);
                        props.load(stream);
                        propertyCacheEntry = new PropertyCacheEntry(props, true, createFile);
                        propFiles.put(propertyFile, propertyCacheEntry);
                    }
                    finally {
                        Closeables.closeQuietly(stream);
                    }
                }
                else {
                    throw new IllegalStateException(format("Can not load %s, not a file!", definitionPropertyFile));
                }
            }
        }

        Preconditions.checkState(propertyCacheEntry != null); // This can only be null for an ephemeral property (no backing properties file)
        return propertyCacheEntry.getProps();
    }

    public static class PropertyCacheEntry
    {
        private final Properties props;

        private final boolean exists;

        private boolean create;

        PropertyCacheEntry(@Nonnull final Properties props,
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
            if (!(other instanceof PropertyCacheEntry))
                return false;
            PropertyCacheEntry castOther = (PropertyCacheEntry) other;
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
}
