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
package com.paywholesail.mojo.numbers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.paywholesail.mojo.numbers.beans.MacroDefinition;
import com.paywholesail.mojo.numbers.macros.MacroType;

public class MacroField implements PropertyElement
{
    private final MacroDefinition macroDefinition;
    private final ValueProvider valueProvider;
    private final AbstractNumbersMojo mojo;

    public static List<MacroField> createMacros(final PropertyCache propertyCache,
                                                final MacroDefinition[] macroDefinitions,
                                                final AbstractNumbersMojo mojo)
        throws IOException
    {
        final List<MacroField> result = Lists.newArrayList();

        if (!ArrayUtils.isEmpty(macroDefinitions)) {
            for (final MacroDefinition macroDefinition : macroDefinitions) {
                macroDefinition.check();
                final ValueProvider macroValue = propertyCache.getPropertyValue(macroDefinition);
                final MacroField macroField = new MacroField(macroDefinition, macroValue, mojo);
                result.add(macroField);
            }
        }
        return result;
    }

    public MacroField(final MacroDefinition macroDefinition,
                      final ValueProvider valueProvider,
                      final AbstractNumbersMojo mojo)
    {
        this.macroDefinition = macroDefinition;
        this.valueProvider = valueProvider;
        this.mojo = mojo;
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
            macroType = MacroType.class.cast(mojo.getContainer().lookup(MacroType.ROLE, type));
        }
        else {
            final String macroClassName = macroDefinition.getMacroClass();
            Preconditions.checkState(macroClassName != null, "No definition for macro '%s' found!", macroDefinition.getId());
            final Class<?> macroClass = Class.forName(macroClassName);
            macroType = MacroType.class.cast(macroClass.getConstructor().newInstance());
        }

        return macroType.getValue(macroDefinition, valueProvider, mojo);
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
        catch (final Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }
}
