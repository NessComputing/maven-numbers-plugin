/**
 * Copyright (C) 2011 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.likeness.maven.plugins.numbers;

import static java.lang.String.format;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.likeness.maven.plugins.numbers.beans.IWFEnum;
import com.likeness.maven.plugins.numbers.beans.StringDefinition;

public class StringField implements PropertyElement
{
    private final StringDefinition stringDefinition;
    private final ValueProvider valueProvider;

    public static List<StringField> createStrings(final PropertyCache propertyCache, final StringDefinition[] stringDefinitions)
        throws IOException
    {
        final List<StringField> result = Lists.newArrayList();

        if (!ArrayUtils.isEmpty(stringDefinitions)) {
            for (StringDefinition stringDefinition : stringDefinitions) {
                stringDefinition.check();
                final ValueProvider stringValue = propertyCache.getPropertyValue(stringDefinition);
                final StringField stringField = new StringField(stringDefinition, stringValue);
                result.add(stringField);
            }
        }
        return result;
    }

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

        final String format = stringDefinition.getFormat();
        return format == null ? result : format(format, result);
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
