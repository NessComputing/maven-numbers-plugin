package com.likeness.maven.plugins.numbers;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import com.likeness.maven.plugins.numbers.beans.IWFCEnum;
import com.likeness.maven.plugins.numbers.beans.NumberDefinition;
import com.likeness.maven.plugins.numbers.util.Log;


/**
 * Fetches the defined numbers and add properties.
 *
 * @goal get
 */
public class GetNumbersMojo extends AbstractNumbersMojo
{
    private final Log LOG = Log.findLog();

    /** Cache for properties files loaded from disk */
    private Map<File, PropertyCache> propFiles = Maps.newHashMap();

    protected void doExecute() throws Exception
    {
        LOG.debug("Running GetNumbers");
        final List<PropertyElement> propertyElements = Lists.newArrayList();
        final List<NumberField> numberFields = createNumbers(numbers);
        propertyElements.addAll(numberFields);

        for (PropertyElement pe : propertyElements) {
            LOG.info("Property name: %s, value: %s", pe.getPropertyName(), pe.getPropertyValue());
        }
    }

    private List<NumberField> createNumbers(final NumberDefinition [] numberDefinitions)
        throws IOException
    {
        final List<NumberField> result = Lists.newArrayList();

        if (!ArrayUtils.isEmpty(numberDefinitions)) {
            for (NumberDefinition numberDefinition : numberDefinitions) {

                numberDefinition.check();
                final PropertyCache propertyCache = getProperties(numberDefinition);
                final String currentValue = propertyCache == null ? numberDefinition.getInitialValue() : findCurrentValue(propertyCache, numberDefinition);
                final NumberField numberField = new NumberField(numberDefinition, currentValue);
                result.add(numberField);
            }
        }
        return result;
    }

    private String findCurrentValue(final PropertyCache propertyCache, final NumberDefinition numberDefinition)
    {
        String currentValue = null;
        final Properties props = propertyCache.getProps();
        final String propName = numberDefinition.getPropertyName();
        final boolean hasProperty = props.containsKey(propName);
        final boolean createProperty = IWFCEnum.checkState(numberDefinition.getOnMissingProperty(), hasProperty, propName);

        if (hasProperty) {
            currentValue = props.getProperty(propName);
        }
        else if (createProperty) {
            currentValue = numberDefinition.getInitialValue();
            props.setProperty(propName, currentValue);
        }

        return currentValue;
    }


    private PropertyCache getProperties(final NumberDefinition numberDefinition)
        throws IOException
    {

        final File definitionPropertyFile = numberDefinition.getPropertyFile();
        if (definitionPropertyFile == null) { // Not ephemeral
            return null;
        }

        PropertyCache propertyCache;
        final File propertyFile = definitionPropertyFile.getCanonicalFile();

        // Throws an exception if the file must exist and does not.
        final boolean createFile = IWFCEnum.checkState(numberDefinition.getOnMissingFile(), propertyFile.exists(), definitionPropertyFile.getCanonicalPath());

        propertyCache = propFiles.get(propertyFile);

        if (propertyCache != null) {
            // If there is a cache hit, something either has loaded the file
            // or another property has already put in a creation order.
            // Make sure that if this number has a creation order it is obeyed.
            if (createFile) {
                propertyCache.doCreate();
            }
        }
        else {
            // Try loading or creating properties.
            final Properties props = new Properties();

            if (!propertyFile.exists()) {
                propertyCache = new PropertyCache(props, false, createFile); // does not exist
            }
            else {
                if (propertyFile.isFile() && propertyFile.canRead()) {
                    InputStream stream = null;
                    try {
                        stream = new FileInputStream(propertyFile);
                        props.load(stream);
                        propertyCache = new PropertyCache(props, true, createFile);
                        propFiles.put(propertyFile, propertyCache);
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

        Preconditions.checkState(propertyCache != null); // This can only be null for an ephemeral property (no backing properties file)
        return propertyCache;
    }
}
