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
