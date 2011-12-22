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
package com.likeness.mojo.numbers;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.likeness.mojo.numbers.beans.PropertyGroup;

public class PropertyField implements PropertyElement
{
    private final String propertyName;
    private final String propertyValue;

    public static List<PropertyElement> createProperties(final Map<String, String> props, final PropertyGroup propertyGroup)
        throws IOException
    {
        final List<PropertyElement> result = Lists.newArrayList();

        for (Iterator<String> it = propertyGroup.getPropertyNames(); it.hasNext(); ) {
            final String name = it.next();
            final String value = propertyGroup.getPropertyValue(name, props);
            result.add(new PropertyField(name, value));
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




