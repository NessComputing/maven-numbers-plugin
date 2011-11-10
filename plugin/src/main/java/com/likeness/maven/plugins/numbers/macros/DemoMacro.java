package com.likeness.maven.plugins.numbers.macros;

import com.likeness.maven.plugins.numbers.ValueProvider;
import com.likeness.maven.plugins.numbers.beans.MacroDefinition;

public class DemoMacro implements MacroType
{
    @Override
    public String getValue(final MacroDefinition macroDefinition, final ValueProvider valueProvider)
    {
        final String type = macroDefinition.getProperties().getProperty("type", "static");
        if ("static".equals(type)) {
            return "static-value";
        }
        else if ("property".equals(type)) {
            return valueProvider.getValue();
        }
        else {
            return macroDefinition.getProperties().getProperty("value");
        }
    }
}
