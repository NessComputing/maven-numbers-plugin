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

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.likeness.maven.plugins.numbers.beans.MacroDefinition;
import com.likeness.maven.plugins.numbers.macros.MacroType;

public class MacroField implements PropertyElement
{
    private final MacroDefinition macroDefinition;
    private final ValueProvider valueProvider;

    public static List<MacroField> createMacros(final PropertyCache propertyCache, final MacroDefinition[] macroDefinitions)
        throws IOException
    {
        final List<MacroField> result = Lists.newArrayList();

        if (!ArrayUtils.isEmpty(macroDefinitions)) {
            for (MacroDefinition macroDefinition : macroDefinitions) {
                macroDefinition.check();
                final ValueProvider macroValue = propertyCache.getPropertyValue(macroDefinition);
                final MacroField macroField = new MacroField(macroDefinition, macroValue);
                result.add(macroField);
            }
        }
        return result;
    }

    public MacroField(final MacroDefinition macroDefinition, final ValueProvider valueProvider)
    {
        this.macroDefinition = macroDefinition;
        this.valueProvider = valueProvider;
    }

    @Override
    public String getPropertyName()
    {
        return macroDefinition.getId();
    }

    @Override
    public String getPropertyValue()
        throws Exception
    {
        final String type = macroDefinition.getMacroType();
        final MacroType macroType;

        if (type != null) {
            macroType = MacroType.EXISTING_MACROS.get(type);
            Preconditions.checkState(macroType != null, format("Could not load macro type '%s' for macro '%s'!", type, macroDefinition.getId()));
        }
        else {
            final String macroClassName = macroDefinition.getMacroClass();
            Preconditions.checkState(macroClassName != null,
                                     format("No definition for macro '%s' found!", macroDefinition.getId()));
            final Class<?> macroClass = Class.forName(macroClassName);
            macroType = MacroType.class.cast(macroClass.newInstance());
        }

        return macroType.getValue(macroDefinition, valueProvider);
    }

    @Override
    public boolean isExport()
    {
        return macroDefinition.isExport();
    }

    @Override
    public String toString()
    {
        try {
            return getPropertyValue();
        }
        catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
