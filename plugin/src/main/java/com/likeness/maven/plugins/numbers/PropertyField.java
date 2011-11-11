package com.likeness.maven.plugins.numbers;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;
import com.likeness.maven.plugins.numbers.beans.PropertyGroup;

public class PropertyField implements PropertyElement
{
    private final String propertyName;
    private final String propertyValue;

    public static List<PropertyElement> createProperties(final Map<String, String> props, final PropertyGroup [] propertyGroups)
        throws IOException
    {
        final List<PropertyElement> result = Lists.newArrayList();

        if (!ArrayUtils.isEmpty(propertyGroups)) {
            for (PropertyGroup propertyGroup : propertyGroups) {
                for (Iterator<String> it = propertyGroup.getPropertyNames(); it.hasNext(); ) {
                    final String name = it.next();
                    final String value = propertyGroup.getPropertyValue(name, props);
                    result.add(new PropertyField(name, value));
                }
            }
        }
        return result;
    }

    PropertyField(final String propertyName, final String propertyValue)
    {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    @Override
    public String getPropertyName()
    {
        return propertyName;
    }

    @Override
    public String getPropertyValue()
    {
        return propertyValue;
    }

    @Override
    public boolean isExport()
    {
        return true;
    }
}




