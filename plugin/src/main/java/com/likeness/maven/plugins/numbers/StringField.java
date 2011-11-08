package com.likeness.maven.plugins.numbers;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.likeness.maven.plugins.numbers.PropertyElement;
import com.likeness.maven.plugins.numbers.ValueProvider;
import com.likeness.maven.plugins.numbers.beans.IWFEnum;
import com.likeness.maven.plugins.numbers.beans.StringDefinition;
import org.apache.commons.lang3.StringUtils;

public class StringField implements PropertyElement
{
    private final StringDefinition stringDefinition;
    private final ValueProvider valueProvider;

    public StringField(final StringDefinition stringDefinition, final ValueProvider valueProvider)
    {
        this.stringDefinition = stringDefinition;
        this.valueProvider = valueProvider;
    }
    
    @Override
    public String getPropertyName()
    {
        // This is not the property name (because many definitions can map onto one prop) 
        // but the actual id.
        return stringDefinition.getId();
    }

    @Override
    public String getPropertyValue()
    {
        final List<String> values = Lists.newArrayList();

        final String propValue = valueProvider.getValue();
        final List<String> definedValues = stringDefinition.getValues();

        // Only add the value from the provider if it is not null.
        if (propValue != null) {
            values.add(propValue);
        }

        if (definedValues != null) {
            values.addAll(definedValues);
        }

        String result = null;
        for (String value : values) {
            result = value;
            if (!StringUtils.isBlank(value) || stringDefinition.isBlankIsValid()) {
                return Objects.firstNonNull(result, "");
            }
        }

        IWFEnum.checkState(stringDefinition.getOnMissingValue(), false, "value");
        return result;
    }

    @Override
    public boolean isExport()
    {
        return stringDefinition.isExport();
    }

    @Override
    public String toString()
    {
        return getPropertyValue();
    }
}
